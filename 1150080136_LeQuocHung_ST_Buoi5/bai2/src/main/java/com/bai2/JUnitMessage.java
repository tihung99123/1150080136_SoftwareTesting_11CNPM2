package com.bai2;

public class JUnitMessage {
    private String message;

    public JUnitMessage(String message) {
        this.message = message;
    }

    public void printMessage() {
        System.out.println(message);
        // Quan trọng: Dòng này cố tình tạo lỗi chia cho 0
        // để thỏa mãn Test Case yêu cầu ArithmeticException
        int divideByZero = 1 / 0; 
    }

    public String printHiMessage() {
        return "Hi! " + message;
    }
}