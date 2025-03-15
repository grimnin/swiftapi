package com.test.swiftapi;

import com.test.swiftapi.domain.entities.Country;
import com.test.swiftapi.domain.entities.SwiftCode;
import com.test.swiftapi.repositories.CountryRepository;
import com.test.swiftapi.repositories.SwiftCodeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class SwiftCodeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @Autowired
    private CountryRepository countryRepository;

    private Country testCountry;
    private SwiftCode testSwiftCode;

    @BeforeEach
    void setup() {
        swiftCodeRepository.deleteAll();
        countryRepository.deleteAll();
        // 🔹 Sprawdzamy, czy kraj "PL" już istnieje
        testCountry = countryRepository.findByIso2("PL").orElseGet(() -> {
            Country newCountry = new Country();
            newCountry.setIso2("PL");
            newCountry.setName("Poland");
            return countryRepository.save(newCountry);
        });

        // 🔹 Sprawdzamy, czy kod SWIFT "ALBPPLP1XXX" już istnieje
        if (swiftCodeRepository.findBySwiftCode("ALBPPLP1XXX") == null) {
            testSwiftCode = new SwiftCode("ALBPPLP1XXX", "Alior Bank", "Warszawa", true, testCountry);
            swiftCodeRepository.save(testSwiftCode);
        }
    }

    @Test
    void getSwiftCode_shouldReturnDetails() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/ALBPPLP1XXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode", is("ALBPPLP1XXX")))
                .andExpect(jsonPath("$.bankName", is("Alior Bank")))
                .andExpect(jsonPath("$.countryISO2", is("PL")));
    }

    @Test
    void getSwiftCodesByCountry_shouldReturnList() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/PL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2", is("PL")))
                .andExpect(jsonPath("$.swiftCodes", hasSize(1)))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode", is("ALBPPLP1XXX")));
    }

    @Test
    void addSwiftCode_shouldReturnCreatedStatus() throws Exception {
        String jsonRequest = """
            {
                "address": "Kraków",
                "bankName": "PKO BP",
                "countryISO2": "PL",
                "countryName": "Poland",
                "isHeadquarter": true,
                "swiftCode": "PKOPPLPWXXX"
            }
        """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", containsString("Added SWIFT code")));
    }

    @Test
    void deleteSwiftCode_shouldReturnSuccessMessage() throws Exception {
        ResultActions deletedSwiftCode = mockMvc.perform(delete("/v1/swift-codes/ALBPPLP1XXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Deleted SWIFT code")));

        // Sprawdzamy, czy kod został usunięty
        mockMvc.perform(get("/v1/swift-codes/ALBPPLP1XXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
