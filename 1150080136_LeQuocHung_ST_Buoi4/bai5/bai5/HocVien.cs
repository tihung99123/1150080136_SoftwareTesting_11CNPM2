using System;

namespace bai5
{
    public class HocVien
    {
        public string MaSo { get; set; }
        public string HoTen { get; set; }
        public string QueQuan { get; set; }
        public double Diem1 { get; set; }
        public double Diem2 { get; set; }
        public double Diem3 { get; set; }

        public HocVien(string ma, string ten, string que, double d1, double d2, double d3)
        {
            MaSo = ma; HoTen = ten; QueQuan = que;
            Diem1 = d1; Diem2 = d2; Diem3 = d3;
        }

        public bool CheckHocBong()
        {
            if (Diem1 < 5.0 || Diem2 < 5.0 || Diem3 < 5.0)
                return false;

            double diemTB = (Diem1 + Diem2 + Diem3) / 3.0;
            if (diemTB >= 8.0)
                return true;

            return false;
        }
    }
}