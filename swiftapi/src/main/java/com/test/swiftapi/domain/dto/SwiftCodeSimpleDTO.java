package com.test.swiftapi.domain.dto;

public record SwiftCodeSimpleDTO(
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarter,
        String swiftCode
) {}
