package com.test.swiftapi.controllers;

import com.test.swiftapi.domain.dto.CountrySwiftCodesDTO;
import com.test.swiftapi.domain.dto.SwiftCodeSimpleDTO;
import com.test.swiftapi.domain.entities.SwiftCode;
import com.test.swiftapi.domain.dto.BranchDTO;
import com.test.swiftapi.domain.dto.SwiftCodeDTO;
import com.test.swiftapi.repositories.SwiftCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/swift-codes")

public class SwiftCodeController {

    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeController(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
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

}
