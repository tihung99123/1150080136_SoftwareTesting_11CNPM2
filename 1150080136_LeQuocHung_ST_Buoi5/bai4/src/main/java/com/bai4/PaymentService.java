package com.bai4;

public class PaymentService {

    // Hàm tính tiền dựa trên tuổi và giới tính
    // gender nhận vào: "Male" hoặc "Female"
    public int calculatePayment(int age, String gender) {
        
        // Kiểm tra tính hợp lệ của tuổi (dựa theo đề bài max là 145)
        if (age < 0 || age > 145) {
            throw new IllegalArgumentException("Tuổi không hợp lệ (0-145)");
        }

        // 1. Trường hợp Trẻ em (0 - 17 tuổi)
        // Đề bài ghi Child (0-17) giá 50 euro, không phân biệt nam nữ
        if (age >= 0 && age <= 17) {
            return 50;
        }

        // 2. Trường hợp Người lớn (>= 18 tuổi)
        if ("Male".equalsIgnoreCase(gender)) {
            // Logic cho Nam
            if (age <= 35) return 100;       // 18 - 35
            else if (age <= 50) return 120;  // 36 - 50
            else return 140;                 // 51 - 145
        } 
        else if ("Female".equalsIgnoreCase(gender)) {
            // Logic cho Nữ
            if (age <= 35) return 80;        // 18 - 35
            else if (age <= 50) return 110;  // 36 - 50
            else return 140;                 // 51 - 145
        } 
        else {
            throw new IllegalArgumentException("Giới tính không hợp lệ");
        }
    }
}