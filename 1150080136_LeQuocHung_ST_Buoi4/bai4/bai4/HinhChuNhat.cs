using System;

namespace bai4
{
    public class HinhChuNhat
    {
        public Diem TopLeft { get; set; }     // Điểm trên bên trái
        public Diem BottomRight { get; set; } // Điểm dưới bên phải

        public HinhChuNhat(Diem topLeft, Diem bottomRight)
        {
            TopLeft = topLeft;
            BottomRight = bottomRight;
        }

        // 1. Phương thức tính diện tích
        public double TinhDienTich()
        {
            // Chiều dài = trị tuyệt đối hiệu 2 tọa độ X
            int width = Math.Abs(BottomRight.X - TopLeft.X);
            // Chiều rộng = trị tuyệt đối hiệu 2 tọa độ Y
            int height = Math.Abs(BottomRight.Y - TopLeft.Y);

            return width * height;
        }

        // 2. Phương thức kiểm tra giao nhau
        public bool CheckGiaoNhau(HinhChuNhat other)
        {
            // Xác định khung giới hạn (min/max) của hình chữ nhật hiện tại (rect1)
            int r1_minX = Math.Min(this.TopLeft.X, this.BottomRight.X);
            int r1_maxX = Math.Max(this.TopLeft.X, this.BottomRight.X);
            int r1_minY = Math.Min(this.TopLeft.Y, this.BottomRight.Y);
            int r1_maxY = Math.Max(this.TopLeft.Y, this.BottomRight.Y);

            // Xác định khung giới hạn của hình chữ nhật kia (rect2)
            int r2_minX = Math.Min(other.TopLeft.X, other.BottomRight.X);
            int r2_maxX = Math.Max(other.TopLeft.X, other.BottomRight.X);
            int r2_minY = Math.Min(other.TopLeft.Y, other.BottomRight.Y);
            int r2_maxY = Math.Max(other.TopLeft.Y, other.BottomRight.Y);

            // Điều kiện KHÔNG giao nhau: nằm hoàn toàn sang trái, phải, trên, hoặc dưới
            if (r1_maxX < r2_minX || r1_minX > r2_maxX || r1_maxY < r2_minY || r1_minY > r2_maxY)
            {
                return false; // Không giao nhau
            }

            return true; // Có giao nhau
        }
    }
}