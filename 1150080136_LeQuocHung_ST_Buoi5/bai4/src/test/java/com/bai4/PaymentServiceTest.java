package com.bai4;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PaymentServiceTest {
    
    private PaymentService service;

    @Before
    public void setUp() {
        service = new PaymentService();
    }

    // --- TEST TRƯỜNG HỢP TRẺ EM (0-17) ---
    @Test
    public void testChild() {
        // Biên dưới
        assertEquals(50, service.calculatePayment(0, "Male"));
        // Giá trị giữa
        assertEquals(50, service.calculatePayment(10, "Female")); 
        // Biên trên (quan trọng)
        assertEquals(50, service.calculatePayment(17, "Male")); 
    }

    // --- TEST TRƯỜNG HỢP NAM (Male) ---
    @Test
    public void testMaleAdult() {
        // Nhóm 18 - 35 (Mong đợi: 100)
        assertEquals(100, service.calculatePayment(18, "Male")); // Biên dưới
        assertEquals(100, service.calculatePayment(35, "Male")); // Biên trên

        // Nhóm 36 - 50 (Mong đợi: 120)
        assertEquals(120, service.calculatePayment(36, "Male")); // Biên dưới
        assertEquals(120, service.calculatePayment(50, "Male")); // Biên trên

        // Nhóm 51 - 145 (Mong đợi: 140)
        assertEquals(140, service.calculatePayment(51, "Male")); // Biên dưới
        assertEquals(140, service.calculatePayment(145, "Male")); // Biên trên
    }

    // --- TEST TRƯỜNG HỢP NỮ (Female) ---
    @Test
    public void testFemaleAdult() {
        // Nhóm 18 - 35 (Mong đợi: 80)
        assertEquals(80, service.calculatePayment(18, "Female"));
        assertEquals(80, service.calculatePayment(35, "Female"));

        // Nhóm 36 - 50 (Mong đợi: 110)
        assertEquals(110, service.calculatePayment(36, "Female"));
        assertEquals(110, service.calculatePayment(50, "Female"));

        // Nhóm 51 - 145 (Mong đợi: 140)
        assertEquals(140, service.calculatePayment(51, "Female"));
        assertEquals(140, service.calculatePayment(145, "Female"));
    }

    // --- TEST NGOẠI LỆ (Exception) ---
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAgeNegative() {
        service.calculatePayment(-1, "Male");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAgeTooHigh() {
        service.calculatePayment(146, "Female");
    }
}