package com.test.swiftapi.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "swift_codes")
public class SwiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String swiftCode;

    @Column(nullable = false)
    private String bankName;




    @Column(nullable = false) // ✅ Dodajemy address
    private String address;

    @Column(nullable = false) // ✅ Czy to headquarters?
    private boolean isHeadquarter;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public SwiftCode() {
    }

    public SwiftCode(String swiftCode, String bankName, String address, boolean isHeadquarter, Country country) {
        this.swiftCode = swiftCode;
        this.bankName = bankName;
        this.address = address;
        this.isHeadquarter = isHeadquarter;
        this.country = country;
    }



    public Long getId() {
        return id;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public String getBankName() {
        return bankName;
    }





    public String getAddress() {
        return address;
    }

    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public Country getCountry() {
        return country;
    }

    public SwiftCode(String trim, String trim1, String trim2, String trim3) {
    }
}
