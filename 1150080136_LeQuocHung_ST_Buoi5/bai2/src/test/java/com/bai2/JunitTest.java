package com.bai2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// @RunWith: Báo cho JUnit biết class này dùng để chạy Suite
@RunWith(Suite.class)

// @SuiteClasses: Liệt kê các class test con cần chạy
@Suite.SuiteClasses({
    SuiteTest1.class,
    SuiteTest2.class
})
public class JunitTest {
    // Class này để trống, chỉ dùng để chứa Annotation bên trên
}