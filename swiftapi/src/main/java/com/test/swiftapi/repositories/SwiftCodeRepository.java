package com.test.swiftapi.repositories;

import com.test.swiftapi.domain.entities.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {

    SwiftCode findBySwiftCode(String swiftCode);

    List<SwiftCode> findByCountry_Iso2(String countryIso2); // Wyszukiwanie kodów SWIFT po kodzie kraju ISO2

}
