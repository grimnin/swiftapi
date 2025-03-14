package com.test.swiftapi.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.test.swiftapi.domain.entities.Country;
import com.test.swiftapi.domain.entities.SwiftCode;
import com.test.swiftapi.repositories.CountryRepository;
import com.test.swiftapi.repositories.SwiftCodeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvImporterService {

    private final SwiftCodeRepository swiftCodeRepository;
    private final CountryRepository countryRepository;

    public CsvImporterService(SwiftCodeRepository swiftCodeRepository, CountryRepository countryRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.countryRepository = countryRepository;
    }

    @PostConstruct
    @Transactional
    public void importCsvData() {
        if (swiftCodeRepository.count() == 0) {
            List<SwiftCode> swiftCodes = loadCSV();
            swiftCodeRepository.saveAll(swiftCodes);
            System.out.println("📥 Załadowano " + swiftCodes.size() + " rekordów do bazy.");
        } else {
            System.out.println("✅ Dane już istnieją, nie importuję ponownie.");
        }
    }

    private List<SwiftCode> loadCSV() {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("swift_codes.csv"), StandardCharsets.UTF_8))) {

            List<String[]> records = csvReader.readAll();
            return records.stream()
                    .skip(1) // Pomijamy nagłówek
                    .map(this::mapToSwiftCode)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("❌ Błąd podczas wczytywania pliku CSV", e);
        }
    }

    private SwiftCode mapToSwiftCode(String[] fields) {
        if (fields.length < 7) {
            throw new RuntimeException("⚠ Błędny format pliku CSV! Zła liczba kolumn.");
        }

        String countryCode = fields[0].trim().toUpperCase();
        String countryName = fields[6].trim();
        String address = fields[4].trim(); // ✅ Pobieramy address

        boolean isHeadquarter = fields[1].trim().endsWith("XXX"); // ✅ Sprawdzamy czy headquarters

        Country country = countryRepository.findByIso2(countryCode)
                .orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setIso2(countryCode);
                    newCountry.setName(countryName);
                    return countryRepository.save(newCountry);
                });

        return new SwiftCode(
                fields[1].trim(), // swift_code
                fields[3].trim(), // bank_name
                 // branch_name
                address, // ✅ Zapisujemy address
                isHeadquarter, // ✅ Zapisujemy czy headquarters
                country
        );
    }

}
