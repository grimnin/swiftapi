package com.test.swiftapi.repositories;

import com.test.swiftapi.domain.entities.Country;
import com.test.swiftapi.domain.entities.SwiftCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 🔹 Nie zastępujemy bazy testową!
@TestPropertySource(locations = "classpath:application-test.properties") // 🔹 Wskazujemy testową konfigurację bazy
class SwiftCodeRepositoryTest {

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void shouldSaveAndFindSwiftCode() {
        // 📌 Tworzymy nowy kraj i zapisujemy go
        Country country = new Country();
        country.setIso2("PL");
        country.setName("Poland");
        country = countryRepository.save(country);

        // 📌 Tworzymy i zapisujemy kod SWIFT
        SwiftCode swiftCode = new SwiftCode("ALBPPLP1XXX", "Alior Bank", "Warszawa", true, country);
        swiftCodeRepository.save(swiftCode);

        // 📌 Pobieramy SWIFT code z bazy i sprawdzamy, czy się zgadza
        Optional<SwiftCode> foundSwiftCode = Optional.ofNullable(swiftCodeRepository.findBySwiftCode("ALBPPLP1XXX"));

        assertTrue(foundSwiftCode.isPresent());
        assertEquals("Alior Bank", foundSwiftCode.get().getBankName());
    }

    @Test
    void shouldReturnEmptyForNonExistingSwiftCode() {
        Optional<SwiftCode> foundSwiftCode = Optional.ofNullable(swiftCodeRepository.findBySwiftCode("NONEXISTENT"));

        assertFalse(foundSwiftCode.isPresent());
    }

    @Test
    void shouldFindSwiftCodesByCountry() {
        Country country = new Country();
        country.setIso2("DE");
        country.setName("Germany");
        country = countryRepository.save(country);

        SwiftCode swiftCode1 = new SwiftCode("DEUTDEFF", "Deutsche Bank", "Berlin", true, country);
        SwiftCode swiftCode2 = new SwiftCode("DEUTDEHH", "Deutsche Bank", "Hamburg", false, country);
        swiftCodeRepository.save(swiftCode1);
        swiftCodeRepository.save(swiftCode2);

        List<SwiftCode> codes = swiftCodeRepository.findByCountry_Iso2("DE");

        assertEquals(2, codes.size());
    }

    @Test
    void shouldDeleteSwiftCode() {
        Country country = new Country();
        country.setIso2("FR");
        country.setName("France");
        country = countryRepository.save(country);

        SwiftCode swiftCode = new SwiftCode("BNPAFRPP", "BNP Paribas", "Paris", true, country);
        swiftCodeRepository.save(swiftCode);

        swiftCodeRepository.delete(swiftCode);

        Optional<SwiftCode> deletedSwiftCode = Optional.ofNullable(swiftCodeRepository.findBySwiftCode("BNPAFRPP"));
        assertFalse(deletedSwiftCode.isPresent());
    }
}
