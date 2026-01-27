package com.bai5;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("--- BAT DAU KIEM THU BAI 5 ---");
        Result result = JUnitCore.runClasses(RegistrationServiceTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println("Loi: " + failure.toString());
        }

        System.out.println("So test da chay: " + result.getRunCount());
        System.out.println("So test that bai: " + result.getFailureCount());
        System.out.println("KET QUA (Success): " + result.wasSuccessful());
    }
}