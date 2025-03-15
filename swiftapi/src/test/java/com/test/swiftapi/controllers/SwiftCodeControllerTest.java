package com.test.swiftapi.controllers;

import com.test.swiftapi.domain.entities.Country;
import com.test.swiftapi.domain.entities.SwiftCode;
import com.test.swiftapi.repositories.CountryRepository;
import com.test.swiftapi.repositories.SwiftCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")  // Dodaj to
class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void getExistingSwiftCode_shouldReturnDetails() throws Exception {
        // 📌 Sprawdź, czy kraj już istnieje w bazie
        Country country = countryRepository.findByIso2("PL")
                .orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setIso2("PL");
                    newCountry.setName("Poland");
                    return countryRepository.save(newCountry);
                });

        // 📌 Tworzymy SwiftCode i przypisujemy istniejący kraj
        SwiftCode swiftCode = new SwiftCode("ALBPPLP1BMW", "Alior Bank", "Warszawa", true, country);

        // 📌 Sprawdź, czy dany SwiftCode już istnieje, aby uniknąć duplikacji
        if (swiftCodeRepository.findBySwiftCode(swiftCode.getSwiftCode()) == null) {
            swiftCodeRepository.save(swiftCode);
        }

        mockMvc.perform(get("/v1/swift-codes/ALBPPLP1BMW")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode", is("ALBPPLP1BMW")))
                .andExpect(jsonPath("$.bankName", is("ALIOR BANK SPOLKA AKCYJNA"))) // ✅ Poprawiona wartość
                .andExpect(jsonPath("$.countryISO2", is("PL")));

    }

    @Test
    void addSwiftCode_shouldReturnCreatedStatus() throws Exception {
        String jsonRequest = """
        {
            "address": "Warszawa",
            "bankName": "Alior Bank",
            "countryISO2": "PL",
            "countryName": "Poland",
            "isHeadquarter": true,
            "swiftCode": "ALBPPLP1XXX"
        }
    """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", containsString("Added SWIFT code")));
    }



    @Test
    void getNonExistingSwiftCode_shouldReturn404() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/NONEXISTENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
