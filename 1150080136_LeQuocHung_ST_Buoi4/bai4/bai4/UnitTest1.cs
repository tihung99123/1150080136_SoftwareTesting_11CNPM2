using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace bai4
{
    [TestClass]
    public class UnitTest1
    {
        // Test Case 1: Kiểm thử tính diện tích
        // Hình chữ nhật từ (0,5) đến (4,0) => Rộng = 4, Cao = 5 => Diện tích = 20
        [TestMethod]
        public void TestTinhDienTich()
        {
            Diem p1 = new Diem(0, 5);
            Diem p2 = new Diem(4, 0);
            HinhChuNhat hcn = new HinhChuNhat(p1, p2);

            double expected = 20;
            double actual = hcn.TinhDienTich();

            Assert.AreEqual(expected, actual, "Diện tích tính sai");
        }

        // Test Case 2: Kiểm thử hai hình giao nhau
        // HCN 1: (0,5) -> (5,0)
        // HCN 2: (2,7) -> (4,2) (Nằm chồng lên phần giữa của HCN 1)
        [TestMethod]
        public void TestGiaoNhau_True()
        {
            HinhChuNhat hcn1 = new HinhChuNhat(new Diem(0, 5), new Diem(5, 0));
            HinhChuNhat hcn2 = new HinhChuNhat(new Diem(2, 7), new Diem(4, 2));

            bool result = hcn1.CheckGiaoNhau(hcn2);

            Assert.IsTrue(result, "Lẽ ra phải giao nhau nhưng kết quả lại là False");
        }

        // Test Case 3: Kiểm thử hai hình KHÔNG giao nhau
        // HCN 1: (0,5) -> (2,0)
        // HCN 2: (3,5) -> (5,0) (Nằm hoàn toàn bên phải HCN 1)
        [TestMethod]
        public void TestGiaoNhau_False()
        {
            HinhChuNhat hcn1 = new HinhChuNhat(new Diem(0, 5), new Diem(2, 0));
            HinhChuNhat hcn2 = new HinhChuNhat(new Diem(3, 5), new Diem(5, 0));

            bool result = hcn1.CheckGiaoNhau(hcn2);

            Assert.IsFalse(result, "Lẽ ra không giao nhau nhưng kết quả lại là True");
        }
    }
}