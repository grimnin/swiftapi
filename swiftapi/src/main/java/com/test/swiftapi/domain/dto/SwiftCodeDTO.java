package com.test.swiftapi.domain.dto;

import com.test.swiftapi.domain.entities.Country;

public record SwiftCodeDTO(
        String swiftCode,
        String bankName,
        String address,
        String city,
        String countryISO2,
        String countryName,
        boolean isHeadquarter
) {}