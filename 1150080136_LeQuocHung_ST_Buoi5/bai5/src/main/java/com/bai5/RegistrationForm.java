package com.bai5;

import javax.swing.*;
import java.awt.*;

public class RegistrationForm extends JFrame {
    
    // Khai báo các component
    JTextField txtId, txtName, txtEmail, txtPhone, txtDob;
    JTextArea txtAddress;
    JPasswordField txtPass, txtConfirmPass;
    JRadioButton rdoMale, rdoFemale, rdoOther;
    JCheckBox chkTerms;
    JButton btnRegister, btnReset;
    RegistrationService service = new RegistrationService();

    public RegistrationForm() {
        setTitle("ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // --- Header ---
        JLabel lblHeader = new JLabel("ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 16));
        lblHeader.setForeground(Color.DARK_GRAY);
        lblHeader.setBounds(50, 10, 400, 30);
        add(lblHeader);

        int y = 50;
        int h = 25;
        int lblW = 120;
        int txtW = 300;
        int gap = 40;

        // 1. Mã KH
        addLabel("Mã Khách Hàng *", 30, y);
        txtId = new JTextField();
        txtId.setBounds(160, y, txtW, h);
        add(txtId);
        
        y += gap;
        // 2. Họ tên
        addLabel("Họ và Tên *", 30, y);
        txtName = new JTextField();
        txtName.setBounds(160, y, txtW, h);
        add(txtName);

        y += gap;
        // 3. Email
        addLabel("Email *", 30, y);
        txtEmail = new JTextField();
        txtEmail.setBounds(160, y, txtW, h);
        add(txtEmail);

        y += gap;
        // 4. SĐT
        addLabel("Số điện thoại *", 30, y);
        txtPhone = new JTextField();
        txtPhone.setBounds(160, y, txtW, h);
        add(txtPhone);

        y += gap;
        // 5. Địa chỉ (TextArea)
        addLabel("Địa chỉ *", 30, y);
        txtAddress = new JTextArea();
        txtAddress.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtAddress.setLineWrap(true);
        txtAddress.setBounds(160, y, txtW, 60);
        add(txtAddress);

        y += 75; // Nhảy xa hơn do địa chỉ to
        // 6. Mật khẩu
        addLabel("Mật khẩu *", 30, y);
        txtPass = new JPasswordField();
        txtPass.setBounds(160, y, txtW, h);
        add(txtPass);

        y += gap;
        // 7. Xác nhận MK
        addLabel("Xác nhận MK *", 30, y);
        txtConfirmPass = new JPasswordField();
        txtConfirmPass.setBounds(160, y, txtW, h);
        add(txtConfirmPass);

        y += gap;
        // 8. Ngày sinh
        addLabel("Ngày sinh", 30, y);
        txtDob = new JTextField();
        txtDob.setToolTipText("mm/dd/yyyy");
        txtDob.setBounds(160, y, txtW, h);
        add(txtDob);

        y += gap;
        // 9. Giới tính
        addLabel("Giới tính", 30, y);
        rdoMale = new JRadioButton("Nam");
        rdoFemale = new JRadioButton("Nữ");
        rdoOther = new JRadioButton("Khác");
        rdoMale.setBounds(160, y, 60, h);
        rdoFemale.setBounds(230, y, 60, h);
        rdoOther.setBounds(300, y, 60, h);
        rdoMale.setBackground(Color.WHITE);
        rdoFemale.setBackground(Color.WHITE);
        rdoOther.setBackground(Color.WHITE);
        
        ButtonGroup grp = new ButtonGroup();
        grp.add(rdoMale); grp.add(rdoFemale); grp.add(rdoOther);
        add(rdoMale); add(rdoFemale); add(rdoOther);

        y += gap;
        // 10. Checkbox Terms
        chkTerms = new JCheckBox("Tôi đồng ý với các điều khoản dịch vụ *");
        chkTerms.setBounds(160, y, 300, h);
        chkTerms.setBackground(Color.WHITE);
        add(chkTerms);

        y += 50;
        // --- BUTTONS ---
        btnRegister = new JButton("Đăng ký");
        btnRegister.setBackground(new Color(0, 123, 255)); // Màu xanh dương
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBounds(120, y, 100, 35);
        
        btnReset = new JButton("Nhập lại");
        btnReset.setBackground(Color.GRAY);
        btnReset.setForeground(Color.WHITE);
        btnReset.setBounds(240, y, 100, 35);

        add(btnRegister);
        add(btnReset);

        // --- ACTION LISTENERS ---
        btnRegister.addActionListener(e -> handleRegister());
        btnReset.addActionListener(e -> resetForm());
        
        setLocationRelativeTo(null);
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setBounds(x, y, 120, 25);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(lbl);
    }

    private void handleRegister() {
        String result = service.validateRegistration(
            txtId.getText(), txtName.getText(), txtEmail.getText(), 
            txtPhone.getText(), txtAddress.getText(), 
            new String(txtPass.getPassword()), new String(txtConfirmPass.getPassword()), 
            txtDob.getText(), chkTerms.isSelected()
        );

        if ("OK".equals(result)) {
            JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, result, "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        txtId.setText(""); txtName.setText(""); txtEmail.setText("");
        txtPhone.setText(""); txtAddress.setText(""); txtPass.setText("");
        txtConfirmPass.setText(""); txtDob.setText("");
        chkTerms.setSelected(false);
        rdoMale.setSelected(false); rdoFemale.setSelected(false); rdoOther.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm().setVisible(true));
    }
}