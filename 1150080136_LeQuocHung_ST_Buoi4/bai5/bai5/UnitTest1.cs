using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;

namespace bai5
{
    [TestClass]
    public class UnitTest1
    {
        // Test Case 1: Học viên ĐẠT học bổng (8, 9, 8 -> TB = 8.33, Ko môn nào < 5)
        [TestMethod]
        public void TestHocBong_Dat()
        {
            HocVien hv = new HocVien("HV01", "Nguyen Van A", "HCM", 8.0, 9.0, 8.0);
            bool result = hv.CheckHocBong();
            Assert.IsTrue(result, "Lẽ ra phải được học bổng");
        }

        // Test Case 2: Rớt do điểm trung bình thấp (7, 7, 7 -> TB = 7.0)
        [TestMethod]
        public void TestHocBong_Rot_DiemThap()
        {
            HocVien hv = new HocVien("HV02", "Le Van B", "HN", 7.0, 7.0, 7.0);
            bool result = hv.CheckHocBong();
            Assert.IsFalse(result, "TB dưới 8.0 nên phải rớt");
        }

        // Test Case 3: Rớt do có môn dưới 5 (dù TB = 8.0)
        // (10, 10, 4 -> TB = 8.0 nhưng có điểm 4)
        [TestMethod]
        public void TestHocBong_Rot_DiemLiet()
        {
            HocVien hv = new HocVien("HV03", "Tran Thi C", "DN", 10.0, 10.0, 4.0);
            bool result = hv.CheckHocBong();
            Assert.IsFalse(result, "Có môn dưới 5.0 nên phải rớt");
        }

        // Test Case 4: Kiểm thử chức năng lọc danh sách
        // Input: 3 người (1 đậu, 2 rớt như trên) => Output: Danh sách phải có 1 người.
        [TestMethod]
        public void TestLocDanhSach()
        {
            TrungTam tt = new TrungTam();
            List<HocVien> inputList = new List<HocVien>()
            {
                new HocVien("HV01", "A", "HCM", 8, 9, 8),   // Đậu
                new HocVien("HV02", "B", "HN", 7, 7, 7),    // Rớt
                new HocVien("HV03", "C", "DN", 10, 10, 4)   // Rớt
            };

            List<HocVien> outputList = tt.TimDSHocBong(inputList);

            int expectedCount = 1; // Chỉ có HV01 đậu
            Assert.AreEqual(expectedCount, outputList.Count, "Số lượng học viên được học bổng không đúng");
        }
    }
}