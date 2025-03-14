package com.test.swiftapi.domain.dto;



import java.util.List;

public record CountrySwiftCodesDTO(
        String countryISO2,
        String countryName,
        List<SwiftCodeSimpleDTO> swiftCodes
) {}



