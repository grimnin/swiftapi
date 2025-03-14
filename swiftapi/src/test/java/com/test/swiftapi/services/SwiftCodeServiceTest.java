//package com.test.swiftapi.services;
//
//import com.test.swiftapi.domain.entities.Country;
//import com.test.swiftapi.domain.entities.SwiftCode;
//import com.test.swiftapi.repositories.CountryRepository;
//import com.test.swiftapi.repositories.SwiftCodeRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class SwiftCodeServiceTest {
//
//    @Mock
//    private SwiftCodeRepository swiftCodeRepository;
//
//    @Mock
//    private CountryRepository countryRepository;
//
//    @InjectMocks
//    private SwiftCodeService swiftCodeService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void findSwiftCodeByExistingCode_shouldReturnSwiftCode() {
//        SwiftCode mockSwiftCode = new SwiftCode("AAISALTRXXX", "UNITED BANK", null, null, true);
//        when(swiftCodeRepository.findBySwiftCode("AAISALTRXXX")).thenReturn(mockSwiftCode);
//
//        SwiftCode result = swiftCodeService.findSwiftCode("AAISALTRXXX");
//
//        assertNotNull(result);
//        assertEquals("AAISALTRXXX", result.getSwiftCode());
//        verify(swiftCodeRepository, times(1)).findBySwiftCode("AAISALTRXXX");
//    }
//
//    @Test
//    void findSwiftCodeByNonExistingCode_shouldReturnNull() {
//        when(swiftCodeRepository.findBySwiftCode("NONEXISTENT")).thenReturn(null);
//
//        SwiftCode result = swiftCodeService.findSwiftCode("NONEXISTENT");
//
//        assertNull(result);
//        verify(swiftCodeRepository, times(1)).findBySwiftCode("NONEXISTENT");
//    }
//}
