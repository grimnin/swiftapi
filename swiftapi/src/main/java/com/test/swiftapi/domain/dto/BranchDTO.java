package com.test.swiftapi.domain.dto;

public record BranchDTO(
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarter,
        String swiftCode
) {}
