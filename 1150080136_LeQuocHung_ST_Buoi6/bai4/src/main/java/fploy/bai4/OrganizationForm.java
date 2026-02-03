package fploy.bai4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class OrganizationForm extends JFrame {

    private JTextField txtUnitId;
    private JTextField txtName;
    private JTextArea txtDescription;
    private OrganizationDAO dao;

    public OrganizationForm() {
        try {
            dao = new OrganizationDAO();
        } catch (SQLException e) {
            String userMessage = "Database Error: " + e.getMessage();
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                userMessage += "\nPlease ensure the SQLite JDBC driver is available on the classpath.\n"
                        + "If you run with 'java', put sqlite-jdbc-*.jar inside the project's lib/ folder, or \n"
                        + "use Maven: 'mvn -pl bai4 -am exec:java -Dexec.mainClass=fploy.bai4.OrganizationForm' to run with dependencies.";
            }
            JOptionPane.showMessageDialog(this, userMessage, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setTitle("Add Organization Unit");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 450);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        // Header
        JLabel lblHeader = new JLabel("Add Organization Unit");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setForeground(new Color(50, 100, 150));
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(lblHeader);
        contentPane.add(Box.createVerticalStrut(20));

        // Unit Id
        addLabel("Unit Id", contentPane);
        txtUnitId = new JTextField();
        txtUnitId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtUnitId.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(txtUnitId);
        contentPane.add(Box.createVerticalStrut(10));

        // Name
        addLabel("Name*", contentPane);
        txtName = new JTextField();
        txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtName.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(txtName);
        contentPane.add(Box.createVerticalStrut(10));

        // Description
        addLabel("Description", contentPane);
        txtDescription = new JTextArea(5, 20);
        txtDescription.setText("Type description here");
        txtDescription.setForeground(Color.GRAY);
        JScrollPane scrollPane = new JScrollPane(txtDescription);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(scrollPane);
        contentPane.add(Box.createVerticalStrut(10));

        // Footer Text
        JLabel lblFooter = new JLabel("This unit will be added under Organization");
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(lblFooter);

        JLabel lblRequired = new JLabel("* Required");
        lblRequired.setForeground(Color.GRAY);
        lblRequired.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(lblRequired);
        contentPane.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(Color.WHITE);

        JButton btnSave = new JButton("Save");
        btnSave.setBackground(new Color(100, 180, 50)); // Green color
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAction();
            }
        });

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        contentPane.add(buttonPanel);

        // Window Listener to close DB connection
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
        String unitId = txtUnitId.getText();
        String name = txtName.getText();
        String desc = txtDescription.getText();

        // Placeholder handling
        if ("Type description here".equals(desc)) {
            desc = "";
        }

        try {
            OrganizationUnit unit = new OrganizationUnit(unitId, name, desc);
            boolean saved = dao.save(unit);
            if (saved) {
                JOptionPane.showMessageDialog(this, "Organization Unit Saved Successfully!");
                // Clear fields
                txtUnitId.setText("");
                txtName.setText("");
                txtDescription.setText("Type description here");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            if (ex.getMessage().contains("PRIMARY KEY")) {
                JOptionPane.showMessageDialog(this, "Unit ID already exists!", "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Set System Look and Feel for better appearance
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                OrganizationForm frame = new OrganizationForm();
                frame.setVisible(true);
            }
        });
    }
}