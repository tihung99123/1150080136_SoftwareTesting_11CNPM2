using System;
using System.Windows.Forms;

namespace canhan
{
    public partial class Form1 : Form
    {
        // Khởi tạo đối tượng xử lý Logic
        OrgManager manager = new OrgManager();
        private int createdOrgId = -1;

        public Form1()
        {
            InitializeComponent();
        }

        // Xử lý sự kiện khi nhấn nút Save — updated to show inline errors and prevent double-submit
        private void btnSave_Click(object sender, EventArgs e)
        {
            // clear previous inline errors
            lblOrgNameError.Visible = lblPhoneError.Visible = lblEmailError.Visible = false;
            lblOrgNameError.Text = lblPhoneError.Text = lblEmailError.Text = string.Empty;

            // 1. Lấy và trim dữ liệu từ giao diện
            string name = txtOrgName.Text.Trim();
            string addr = txtAddress.Text.Trim();
            string phone = txtPhone.Text.Trim();
            string email = txtEmail.Text.Trim();

            // 2. Per-field validation (so UI can show specific messages)
            bool hasError = false;

            if (string.IsNullOrWhiteSpace(name))
            {
                lblOrgNameError.Text = "Organization Name is required";
                lblOrgNameError.Visible = true;
                hasError = true;
                txtOrgName.Focus();
            }
            else if (name.Length < 3 || name.Length > 255)
            {
                lblOrgNameError.Text = "Organization Name must be 3–255 characters";
                lblOrgNameError.Visible = true;
                hasError = true;
                txtOrgName.Focus();
            }

            if (!string.IsNullOrEmpty(phone) && !System.Text.RegularExpressions.Regex.IsMatch(phone, "^\\d{9,12}$"))
            {
                if (!hasError) txtPhone.Focus();
                lblPhoneError.Text = "Phone must contain 9–12 digits";
                lblPhoneError.Visible = true;
                hasError = true;
            }

            if (!string.IsNullOrEmpty(email) && !System.Text.RegularExpressions.Regex.IsMatch(email, @"^[^@\s]+@[^@\s]+\.[^@\s]+$"))
            {
                if (!hasError) txtEmail.Focus();
                lblEmailError.Text = "Email is not a valid address";
                lblEmailError.Visible = true;
                hasError = true;
            }

            if (hasError)
                return; // don't proceed to persistence

            // disable Save to prevent double-submit
            btnSave.Enabled = false;

            try
            {
                // 3. Uniqueness check (case-insensitive)
                if (manager.IsOrgNameExists(name))
                {
                    lblOrgNameError.Text = "Organization Name already exists";
                    lblOrgNameError.Visible = true;
                    MessageBox.Show("Organization Name already exists", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }

                // 4. Persist and get generated id
                int newId = manager.SaveOrgAndGetId(name, addr, phone, email);
                if (newId > 0)
                {
                    createdOrgId = newId;
                    MessageBox.Show("Save successfully", "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    btnDirector.Enabled = true;
                }
                else
                {
                    MessageBox.Show("Save failed. Please try again.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Database error: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            finally
            {
                btnSave.Enabled = true;
            }
        }

        // Xử lý sự kiện khi nhấn nút Back
        private void btnBack_Click(object sender, EventArgs e)
        {
            // Đóng form hiện tại
            this.Close();
        }

        // Xử lý sự kiện khi nhấn nút Director
        private void btnDirector_Click(object sender, EventArgs e)
        {
            if (createdOrgId <= 0)
            {
                MessageBox.Show("No organization selected. Please save an organization first.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // If Director form exists use it; otherwise show placeholder message
            try
            {
                // DirectorForm directorForm = new DirectorForm(createdOrgId, txtOrgName.Text.Trim());
                // directorForm.ShowDialog();
                MessageBox.Show($"Open Director Management for OrgID={createdOrgId}", "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (Exception)
            {
                MessageBox.Show("Unable to open Director Management.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        // Optional: expose created OrgID for UI tests
        internal int GetCreatedOrgId() => createdOrgId;
    }
}