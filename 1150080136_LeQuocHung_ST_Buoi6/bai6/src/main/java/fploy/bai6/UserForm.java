package fploy.bai6;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UserForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullname;
    private JTextField txtEmail;
    private UserDAO dao;

    public UserForm() {
        try {
            dao = new UserDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setTitle("Add User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 400);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        addLabel("Username", contentPane);
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        contentPane.add(txtUsername);
        contentPane.add(Box.createVerticalStrut(10));

        addLabel("Password", contentPane);
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        contentPane.add(txtPassword);
        contentPane.add(Box.createVerticalStrut(10));

        addLabel("Fullname", contentPane);
        txtFullname = new JTextField();
        txtFullname.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        contentPane.add(txtFullname);
        contentPane.add(Box.createVerticalStrut(10));

        addLabel("Email", contentPane);
        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        contentPane.add(txtEmail);
        contentPane.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAction();
            }
        });
        buttonPanel.add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnCancel);

        contentPane.add(buttonPanel);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (dao != null)
                        dao.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void addLabel(String text, Container container) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(new Color(80, 80, 80));
        container.add(label);
        container.add(Box.createVerticalStrut(5));
    }

    private void saveAction() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String fullname = txtFullname.getText();
        String email = txtEmail.getText();

        try {
            User user = new User(username, password, fullname, email);
            boolean ok = dao.insert(user);
            if (ok) {
                JOptionPane.showMessageDialog(this, "User saved");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                }
                new UserForm().setVisible(true);
            }
        });
    }
}