package com.bai4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentForm extends JFrame {

    private JTextField txtAge;
    private JTextField txtResult;
    private JCheckBox chkMale, chkFemale, chkChild;
    private PaymentService service = new PaymentService();

    public PaymentForm() {
        setTitle("Payment Calculator");
        setSize(450, 250); // Kích thước khung
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Tự chỉnh vị trí (Absolute Layout)
        
        // Đặt màu nền xám giống trong ảnh
        getContentPane().setBackground(Color.LIGHT_GRAY);

        // --- 1. Tiêu đề ---
        JLabel lblTitle = new JLabel("Calculate the Payment for the Patient", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBounds(20, 10, 350, 30);
        add(lblTitle);

        // --- 2. Checkbox (Giới tính & Trẻ em) ---
        chkMale = new JCheckBox("Male");
        chkMale.setBackground(Color.LIGHT_GRAY);
        chkMale.setBounds(50, 50, 70, 20);
        add(chkMale);

        chkFemale = new JCheckBox("Female");
        chkFemale.setBackground(Color.LIGHT_GRAY);
        chkFemale.setBounds(130, 50, 80, 20);
        add(chkFemale);

        chkChild = new JCheckBox("Child (0 - 17 years)");
        chkChild.setBackground(Color.LIGHT_GRAY);
        chkChild.setBounds(220, 50, 150, 20);
        add(chkChild);

        // Logic: Chỉ được chọn 1 trong 3 cái (Giả lập RadioButton)
        ButtonGroup group = new ButtonGroup();
        group.add(chkMale);
        group.add(chkFemale);
        group.add(chkChild);
        chkMale.setSelected(true); // Mặc định chọn Male

        // --- 3. Nhập tuổi ---
        JLabel lblAge = new JLabel("Age (Years)");
        lblAge.setBounds(50, 100, 80, 25);
        add(lblAge);

        txtAge = new JTextField();
        txtAge.setBounds(130, 100, 80, 25);
        add(txtAge);

        // --- 4. Nút Calculate ---
        JButton btnCalculate = new JButton("Calculate");
        btnCalculate.setBounds(220, 100, 100, 25);
        add(btnCalculate);

        // --- 5. Kết quả ---
        JLabel lblPayment = new JLabel("Payment is");
        lblPayment.setBounds(50, 140, 80, 25);
        add(lblPayment);

        txtResult = new JTextField();
        txtResult.setBounds(130, 140, 80, 25);
        txtResult.setEditable(false); // Không cho sửa kết quả
        add(txtResult);

        JLabel lblEuro = new JLabel("euro €");
        lblEuro.setBounds(220, 140, 50, 25);
        add(lblEuro);

        // --- 6. Xử lý sự kiện khi bấm nút ---
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });

        // Căn giữa màn hình khi chạy
        setLocationRelativeTo(null);
    }

    private void calculate() {
        try {
            // Lấy tuổi từ ô nhập
            String ageText = txtAge.getText();
            if (ageText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tuổi!");
                return;
            }
            int age = Integer.parseInt(ageText);

            // Xác định giới tính gửi đi
            String gender = "";
            if (chkMale.isSelected()) gender = "Male";
            else if (chkFemale.isSelected()) gender = "Female";
            else if (chkChild.isSelected()) {
                // Nếu chọn Child nhưng nhập tuổi > 17 thì báo lỗi hoặc tự xử lý
                // Ở đây ta cứ gửi "Male" vì hàm PaymentService sẽ ưu tiên check tuổi <= 17 trước
                gender = "Male"; 
            }

            // Gọi hàm tính toán từ file PaymentService.java đã viết lúc nãy
            int cost = service.calculatePayment(age, gender);

            // Hiển thị kết quả
            txtResult.setText(String.valueOf(cost));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tuổi phải là số nguyên!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // Hàm main để chạy giao diện
    public static void main(String[] args) {
        // Chạy trên luồng giao diện (Swing Thread)
        SwingUtilities.invokeLater(() -> {
            new PaymentForm().setVisible(true);
        });
    }
}