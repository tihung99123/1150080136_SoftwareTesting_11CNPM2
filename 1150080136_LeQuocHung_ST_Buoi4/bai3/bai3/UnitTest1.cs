using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;

namespace bai3
{
    [TestClass]
    public class UnitTest1
    {
        // Test Case 1: Chuyển đổi sang Nhị phân (Binary - Base 2)
        [TestMethod]
        public void TestConvert_Binary_Base2()
        {
            int number = 10;
            Radix r = new Radix(number);

            string expected = "1010";
            string actual = r.ConvertDecimalToAnother(2);

            Assert.AreEqual(expected, actual, "Sai khi chuyển số 10 sang nhị phân");
        }

        // Test Case 2: Chuyển đổi sang Thập lục phân (Hex - Base 16)
        [TestMethod]
        public void TestConvert_Hex_Base16()
        {
            int number = 255;
            Radix r = new Radix(number);

            string expected = "FF";
            string actual = r.ConvertDecimalToAnother(16);

            Assert.AreEqual(expected, actual, "Sai khi chuyển số 255 sang Hex");
        }

        // Test Case 3: Kiểm tra lỗi khi nhập số âm (Constructor)
        [TestMethod]
        [ExpectedException(typeof(ArgumentException))]
        public void TestConstructor_NegativeNumber()
        {
            int number = -5;
            new Radix(number);
        }

        // Test Case 4: Kiểm tra lỗi khi nhập cơ số k sai (k > 16)
        [TestMethod]
        [ExpectedException(typeof(ArgumentException))]
        public void TestConvert_InvalidRadix()
        {
            int number = 100;
            Radix r = new Radix(number);

            r.ConvertDecimalToAnother(20);
        }
    }
}