using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;

namespace bai1
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestPower_N_Equals_Zero()
        {
            // Arrange (Chuẩn bị dữ liệu)
            double x = 10.5;
            int n = 0;
            double expected = 1.0;

            // Act (Thực thi hàm)
            double actual = MyMath.Power(x, n);

            // Assert (Kiểm tra kết quả)
            Assert.AreEqual(expected, actual, "Kết quả sai khi n = 0");
        }

        [TestMethod]
        public void TestPower_N_Positive()
        {
            double x = 2.0;
            int n = 3;
            double expected = 8.0;

            double actual = MyMath.Power(x, n);

            Assert.AreEqual(expected, actual, "Kết quả sai khi n dương");
        }

        [TestMethod]
        public void TestPower_N_Negative()
        {
            // Arrange
            double x = 2.0;
            int n = -1;
            double expected = 0.5;

            // Act
            double actual = MyMath.Power(x, n);

            // Assert
            Assert.AreEqual(expected, actual, "Kết quả sai khi n âm");
        }
    }
}