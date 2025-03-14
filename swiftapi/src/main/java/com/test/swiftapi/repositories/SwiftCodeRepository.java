package com.test.swiftapi.repositories;

import com.test.swiftapi.domain.entities.Country;
import com.test.swiftapi.domain.entities.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {

    SwiftCode findBySwiftCode(String swiftCode);

    List<SwiftCode> findByCountry_Iso2(String countryIso2); // Wyszukiwanie kodów SWIFT po kodzie kraju ISO2

    // 🔹 Znajduje wszystkie branchy powiązane z bankiem
    @Query("SELECT s FROM SwiftCode s WHERE s.bankName = :bankName AND s.isHeadquarter = false")
    List<SwiftCode> findBranchesByHeadquarter(String bankName);


    boolean existsByCountry(Country country);
}
