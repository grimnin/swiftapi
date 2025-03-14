package com.test.swiftapi.controllers;

import com.test.swiftapi.domain.dto.*;
import com.test.swiftapi.domain.entities.Country;
import com.test.swiftapi.domain.entities.SwiftCode;
import com.test.swiftapi.repositories.CountryRepository;
import com.test.swiftapi.repositories.SwiftCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/swift-codes")

public class SwiftCodeController {

    private final SwiftCodeRepository swiftCodeRepository;
    private final CountryRepository countryRepository;
    public SwiftCodeController(SwiftCodeRepository swiftCodeRepository,CountryRepository countryRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCode(@PathVariable String swiftCode) {
        Optional<SwiftCode> swiftCodeOpt = Optional.ofNullable(swiftCodeRepository.findBySwiftCode(swiftCode));

        return swiftCodeOpt.map(code -> {
            List<BranchDTO> branches = null;

            // 🔹 Pobieramy branchy TYLKO jeśli to headquarters
            if (code.isHeadquarter()) {
                branches = swiftCodeRepository.findBranchesByHeadquarter(code.getBankName())
                        .stream()
                        .map(branch -> new BranchDTO(
                                branch.getAddress(),
                                branch.getBankName(),
                                branch.getCountry().getIso2(),
                                branch.isHeadquarter(),
                                branch.getSwiftCode()
                        ))
                        .collect(Collectors.toList());
            }

            // 🔹 Tworzymy DTO w zależności od tego, czy to headquarters czy branch
            SwiftCodeDTO dto = code.isHeadquarter()
                    ? new SwiftCodeDTO(
                    code.getAddress(),
                    code.getBankName(),
                    code.getCountry().getIso2(),
                    code.getCountry().getName(),
                    code.isHeadquarter(),
                    code.getSwiftCode(),
                    branches // ✅ Tylko dla headquarters, dla branch będzie pominięte
            )
                    : new SwiftCodeDTO(
                    code.getAddress(),
                    code.getBankName(),
                    code.getCountry().getIso2(),
                    code.getCountry().getName(),
                    code.isHeadquarter(),
                    code.getSwiftCode(),
                    null // 🔥 W ten sposób `branches` NIE POJAWI SIĘ w JSON!
            );

            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<?> getSwiftCodesByCountry(@PathVariable String countryISO2) {
        List<SwiftCode> swiftCodes = swiftCodeRepository.findByCountry_Iso2(countryISO2.toUpperCase());

        if (swiftCodes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CountrySwiftCodesDTO response = new CountrySwiftCodesDTO(
                countryISO2.toUpperCase(),
                swiftCodes.get(0).getCountry().getName(), // Pobieramy nazwę kraju z pierwszego wyniku
                swiftCodes.stream()
                        .map(code -> new SwiftCodeSimpleDTO(
                                code.getAddress(),
                                code.getBankName(),
                                code.getCountry().getIso2(),  // 🔹 Uwzględniamy pole address
                                code.isHeadquarter(),
                                code.getSwiftCode()
                        ))
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<?> addSwiftCode(@RequestBody SwiftCodeDTO request) {
        // Sprawdzamy, czy kod już istnieje
        if (swiftCodeRepository.findBySwiftCode(request.swiftCode()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "SWIFT code '" + request.swiftCode() + "' already exists."));
        }

        // Pobieramy kraj lub tworzymy nowy
        Country country = countryRepository.findByIso2(request.countryISO2())
                .orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setIso2(request.countryISO2());
                    newCountry.setName(request.countryName());
                    return countryRepository.save(newCountry);
                });

        // Tworzymy nowy SWIFT code
        SwiftCode swiftCode = new SwiftCode(
                request.swiftCode(),
                request.bankName(),
                request.address(),
                request.isHeadquarter(),
                country
        );

        swiftCodeRepository.save(swiftCode);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Added SWIFT code '" + request.swiftCode() + "' for bank '" + request.bankName() + "' in '" + request.countryName() + "'."));
    }


    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteSwiftCode(@PathVariable String swiftCode) {
        Optional<SwiftCode> swiftCodeOpt = Optional.ofNullable(swiftCodeRepository.findBySwiftCode(swiftCode));

        if (swiftCodeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "SWIFT code '" + swiftCode + "' not found."));
        }

        SwiftCode codeToDelete = swiftCodeOpt.get();
        Country country = codeToDelete.getCountry();

        swiftCodeRepository.delete(codeToDelete);

        // Jeśli to ostatni kod SWIFT dla kraju, usuwamy również kraj
        if (!swiftCodeRepository.existsByCountry(country)) {
            countryRepository.delete(country);
            return ResponseEntity.ok(Map.of("message", "Deleted SWIFT code '" + swiftCode + "' and removed country '" + country.getName() + "' as it had no more SWIFT codes."));
        }

        return ResponseEntity.ok(Map.of("message", "Deleted SWIFT code '" + swiftCode + "' successfully."));
    }




}
