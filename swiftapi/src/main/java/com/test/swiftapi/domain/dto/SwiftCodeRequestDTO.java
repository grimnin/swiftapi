package com.test.swiftapi.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SwiftCodeRequestDTO(
        String address,
        String bankName,
        @JsonProperty("countryISO2") String countryIso2,
        @JsonProperty("countryName") String countryName,
        boolean isHeadquarter,
        String swiftCode
) {}
