//package com.test.swiftapi.controllers;
//
//import com.test.swiftapi.domain.entities.SwiftCode;
//import com.test.swiftapi.services.SwiftCodeService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class SwiftCodeControllerTest {
//
//    @Mock
//    private SwiftCodeService swiftCodeService;
//
//    @InjectMocks
//    private SwiftCodeController swiftCodeController;
//
//    @Test
//    void testGetSwiftCodeById_WhenExists() {
//        // Given
//        String swiftCode = "AAISALTRXXX";
//        SwiftCode mockCode = new SwiftCode();
//        mockCode.setSwiftCode(swiftCode);
//        mockCode.setBankName("UNITED BANK OF ALBANIA SH.A");
//
//        when(swiftCodeService.getSwiftCodeById(swiftCode)).thenReturn(Optional.of(mockCode));
//
//        // When
//        ResponseEntity<?> response = swiftCodeController.getSwiftCodeById(swiftCode);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        verify(swiftCodeService, times(1)).getSwiftCodeById(swiftCode);
//    }
//
//    @Test
//    void testGetSwiftCodeById_WhenNotExists() {
//        // Given
//        String swiftCode = "INVALIDCODE";
//        when(swiftCodeService.getSwiftCodeById(swiftCode)).thenReturn(Optional.empty());
//
//        // When
//        ResponseEntity<?> response = swiftCodeController.getSwiftCodeById(swiftCode);
//
//        // Then
//        assertEquals(404, response.getStatusCodeValue());
//        verify(swiftCodeService, times(1)).getSwiftCodeById(swiftCode);
//    }
//}
