package com.bai4;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("--- BAT DAU KIEM THU BAI 4 ---");
        
        Result result = JUnitCore.runClasses(PaymentServiceTest.class);

        // In ra chi tiết các lỗi nếu có
        for (Failure failure : result.getFailures()) {
            System.out.println("Loi: " + failure.toString());
        }

        System.out.println("So luong test chay: " + result.getRunCount());
        System.out.println("So luong test that bai: " + result.getFailureCount());
        System.out.println("KET QUA CUOI CUNG (Success): " + result.wasSuccessful());
    }
}