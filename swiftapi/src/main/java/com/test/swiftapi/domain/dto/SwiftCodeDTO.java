package com.test.swiftapi.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.swiftapi.domain.entities.Country;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SwiftCodeDTO(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode,
        List<BranchDTO> branches  // ✅ Lista branchy dla headquarters
) {}