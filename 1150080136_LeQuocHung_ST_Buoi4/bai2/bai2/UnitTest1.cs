using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;

namespace bai2
{
    [TestClass]
    public class UnitTest1
    {
        // Test Case 1: Kiểm tra tính toán đúng
        // Đa thức bậc 2 (n=2): P(x) = 1 + 2x + 3x^2
        // Với x = 2 => P(2) = 1 + 2(2) + 3(4) = 1 + 4 + 12 = 17
        [TestMethod]
        public void TestCal_ValidInput()
        {
            int n = 2;
            List<int> a = new List<int> { 1, 2, 3 }; // 3 hệ số cho bậc 2
            double x = 2.0;
            int expected = 17;

            Polynomial poly = new Polynomial(n, a);
            int actual = poly.Cal(x);

            Assert.AreEqual(expected, actual, "Kết quả tính đa thức sai");
        }

        // Test Case 2: Kiểm tra lỗi khi n âm (n < 0)
        // Mong đợi ném ra ArgumentException với message "Invalid Data"
        [TestMethod]
        [ExpectedException(typeof(ArgumentException))]
        public void TestConstructor_NegativeN_ThrowsException()
        {
            int n = -1;
            List<int> a = new List<int> { 1, 2 }; // Dữ liệu giả

            // Dòng này sẽ gây ra lỗi và Test sẽ Pass nếu lỗi đó là ArgumentException
            new Polynomial(n, a);
        }

        // Test Case 3: Kiểm tra lỗi khi số lượng hệ số không khớp ( != n + 1)
        // n = 2 thì cần 3 hệ số, nhưng ở đây chỉ truyền vào 2
        [TestMethod]
        [ExpectedException(typeof(ArgumentException))]
        public void TestConstructor_InvalidCoefficientCount_ThrowsException()
        {
            int n = 2;
            List<int> a = new List<int> { 1, 2 }; // Thiếu 1 hệ số (cần 3)

            // Dòng này sẽ gây ra lỗi
            new Polynomial(n, a);
        }
    }
}