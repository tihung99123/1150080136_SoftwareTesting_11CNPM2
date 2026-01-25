using System;
using System.Collections.Generic;
using System.Linq;

namespace bai2
{
    public class Polynomial
    {
        private int n;
        private List<int> a;

        public Polynomial(int n, List<int> a)
        {
            // Kiểm tra 2 điều kiện lỗi theo đề bài:
            // 1. Số lượng hệ số không đủ (a.Count != n + 1)
            // 2. n là số âm (n < 0)
            if (n < 0 || a.Count != n + 1)
            {
                throw new ArgumentException("Invalid Data");
            }

            this.n = n;
            this.a = a;
        }

        public int Cal(double x)
        {
            int result = 0;
            // Tính giá trị đa thức: a[0]*x^0 + a[1]*x^1 + ...
            for (int i = 0; i <= this.n; i++)
            {
                result += (int)(a[i] * Math.Pow(x, i));
            }
            return result;
        }
    }
}