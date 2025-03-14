package com.test.swiftapi.services;

import com.test.swiftapi.domain.entities.SwiftCode;
import com.test.swiftapi.repositories.SwiftCodeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class DataLoader {

    private final SwiftCodeRepository swiftCodeRepository;

    public DataLoader(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }


    @PostConstruct
    public void loadData() {
        if (swiftCodeRepository.count() == 0) { // Unikamy duplikacji danych
            List<SwiftCode> swiftCodes = loadCSV();
            swiftCodeRepository.saveAll(swiftCodes);
            System.out.println("📥 Załadowano " + swiftCodes.size() + " rekordów do bazy.");
        }
    }

    private List<SwiftCode> loadCSV() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("swift_codes.csv"), StandardCharsets.UTF_8))) {

            return reader.lines()
                    .skip(1) // Pomijamy nagłówek
                    .map(this::mapToSwiftCode)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("❌ Błąd podczas wczytywania pliku CSV", e);
        }
    }

    private SwiftCode mapToSwiftCode(String line) {
        String[] fields = line.split(",");
        return new SwiftCode(fields[1].trim(), fields[3].trim(), fields[2].trim(), fields[5].trim());

    }

}
