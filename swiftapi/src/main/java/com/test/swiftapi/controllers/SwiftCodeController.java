package com.test.swiftapi.controllers;

import com.test.swiftapi.domain.dto.SwiftCodeDTO;
import com.test.swiftapi.domain.entities.SwiftCode;
import com.test.swiftapi.repositories.SwiftCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeController(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    // 🔹 Endpoint 1: Pobranie szczegółów pojedynczego SWIFT code
    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCode(@PathVariable String swiftCode) {
        Optional<SwiftCode> swiftCodeOpt = Optional.ofNullable(swiftCodeRepository.findBySwiftCode(swiftCode));

        return swiftCodeOpt.map(code -> {
            SwiftCodeDTO dto = new SwiftCodeDTO(
                    code.getSwiftCode(),
                    code.getBankName(),
                    code.getAddress(),  // ✅ Zwracamy address
                    code.getCity(),     // ✅ Zwracamy city
                    code.getCountry().getIso2(),
                    code.getCountry().getName(),
                    code.isHeadquarter() // ✅ Czy to headquarters?
            );
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }
}
