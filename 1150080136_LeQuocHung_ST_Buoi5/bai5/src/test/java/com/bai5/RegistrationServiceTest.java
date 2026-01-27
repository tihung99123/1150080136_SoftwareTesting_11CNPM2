package com.bai5;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegistrationServiceTest {
    
    private RegistrationService service;

    @Before
    public void setUp() {
        service = new RegistrationService();
    }

    // 1. Test trường hợp ĐÚNG (Happy Path)
    @Test
    public void testValidRegistration() {
        String result = service.validateRegistration(
            "KH0001", "Nguyen Van A", "a@gmail.com", "0912345678", 
            "Ha Noi", "12345678", "12345678", "01/01/2000", true
        );
        assertEquals("OK", result);
    }

    // 2. Test Mã KH sai (quá ngắn)
    @Test
    public void testInvalidIdShort() {
        String result = service.validateRegistration(
            "ABC", "Nguyen Van A", "a@gmail.com", "0912345678", 
            "Ha Noi", "12345678", "12345678", "01/01/2000", true
        );
        assertTrue(result.contains("Mã Khách Hàng"));
    }

    // 3. Test Email sai định dạng
    @Test
    public void testInvalidEmail() {
        String result = service.validateRegistration(
            "KH0001", "Nguyen Van A", "invalid-email", "0912345678", 
            "Ha Noi", "12345678", "12345678", "01/01/2000", true
        );
        assertTrue(result.contains("Email"));
    }

    // 4. Test SĐT sai (không bắt đầu bằng 0)
    @Test
    public void testInvalidPhone() {
        String result = service.validateRegistration(
            "KH0001", "Nguyen Van A", "a@gmail.com", "9912345678", 
            "Ha Noi", "12345678", "12345678", "01/01/2000", true
        );
        assertTrue(result.contains("Số điện thoại"));
    }

    // 5. Test Mật khẩu không khớp
    @Test
    public void testPasswordMismatch() {
        String result = service.validateRegistration(
            "KH0001", "Nguyen Van A", "a@gmail.com", "0912345678", 
            "Ha Noi", "12345678", "87654321", "01/01/2000", true
        );
        assertEquals("Xác nhận mật khẩu không khớp.", result);
    }

    // 6. Test chưa đủ 18 tuổi
    @Test
    public void testUnderAge() {
        // Giả sử năm nay là 2026, sinh năm 2010 là mới 16 tuổi
        String result = service.validateRegistration(
            "KH0001", "Nguyen Van A", "a@gmail.com", "0912345678", 
            "Ha Noi", "12345678", "12345678", "01/01/2010", true
        );
        assertEquals("Bạn chưa đủ 18 tuổi.", result);
    }

    // 7. Test chưa check điều khoản
    @Test
    public void testTermsNotAccepted() {
        String result = service.validateRegistration(
            "KH0001", "Nguyen Van A", "a@gmail.com", "0912345678", 
            "Ha Noi", "12345678", "12345678", "01/01/2000", false
        );
        assertEquals("Bạn chưa đồng ý với điều khoản dịch vụ.", result);
    }
}