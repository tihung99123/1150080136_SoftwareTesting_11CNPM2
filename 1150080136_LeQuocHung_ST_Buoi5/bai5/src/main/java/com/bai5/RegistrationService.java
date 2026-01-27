package com.bai5;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class RegistrationService {

    public String validateRegistration(String id, String name, String email, String phone, 
                                     String address, String password, String confirmPassword, 
                                     String dobStr, boolean isTermsAccepted) {
        
        // 1. Validate Mã Khách Hàng (6-10 ký tự, chỉ chữ và số)
        if (id == null || !id.matches("^[a-zA-Z0-9]{6,10}$")) {
            return "Mã Khách Hàng không hợp lệ (6-10 ký tự, chỉ gồm chữ và số).";
        }

        // 2. Validate Họ tên (5-50 ký tự)
        if (name == null || name.length() < 5 || name.length() > 50) {
            return "Họ và tên phải từ 5 đến 50 ký tự.";
        }

        // 3. Validate Email (Định dạng email)
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (email == null || !email.matches(emailRegex)) {
            return "Email không đúng định dạng.";
        }

        // 4. Validate Số điện thoại (Bắt đầu bằng 0, 10-12 số)
        if (phone == null || !phone.matches("^0\\d{9,11}$")) {
            return "Số điện thoại phải bắt đầu bằng 0 và có 10-12 số.";
        }

        // 5. Validate Địa chỉ (Max 255)
        if (address == null || address.trim().isEmpty()) {
            return "Địa chỉ không được để trống.";
        }
        if (address.length() > 255) {
            return "Địa chỉ quá dài (tối đa 255 ký tự).";
        }

        // 6. Validate Mật khẩu (Min 8 ký tự)
        if (password == null || password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự.";
        }

        // 7. Validate Xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            return "Xác nhận mật khẩu không khớp.";
        }

        // 8. Validate Ngày sinh (Nếu nhập thì phải >= 18 tuổi)
        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                LocalDate now = LocalDate.now();
                int age = Period.between(dob, now).getYears();
                if (age < 18) {
                    return "Bạn chưa đủ 18 tuổi.";
                }
            } catch (DateTimeParseException e) {
                return "Ngày sinh không đúng định dạng (mm/dd/yyyy).";
            }
        }

        // 10. Điều khoản dịch vụ
        if (!isTermsAccepted) {
            return "Bạn chưa đồng ý với điều khoản dịch vụ.";
        }

        return "OK"; // Tất cả đều hợp lệ
    }
}