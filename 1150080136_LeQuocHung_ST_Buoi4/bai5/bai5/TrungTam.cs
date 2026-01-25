using System.Collections.Generic;

namespace bai5
{
    public class TrungTam
    {
        public List<HocVien> TimDSHocBong(List<HocVien> danhSachGoc)
        {
            List<HocVien> dsHocBong = new List<HocVien>();

            foreach (var hv in danhSachGoc)
            {
                if (hv.CheckHocBong())
                {
                    dsHocBong.Add(hv);
                }
            }
            return dsHocBong;
        }
    }
}