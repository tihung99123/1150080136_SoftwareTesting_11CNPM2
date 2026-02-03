package fploy.bai5;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

public class JobForm extends JFrame {

    private JTextField txtJobTitle;
    private JTextArea txtDescription;
    private JTextField txtSpecification;
    private JTextArea txtNote;
    private JobDAO dao;

    public JobForm() {
        try {
            dao = new JobDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setTitle("Add Job Title");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 550);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        // Header
        JLabel lblHeader = new JLabel("Add Job Title");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setForeground(new Color(50, 100, 150));
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(lblHeader);
        contentPane.add(Box.createVerticalStrut(20));

        // Job Title
        addLabel("Job Title*", contentPane);
        txtJobTitle = new JTextField();
        txtJobTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtJobTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(txtJobTitle);
        contentPane.add(Box.createVerticalStrut(10));

        // Job Description
        addLabel("Job Description", contentPane);
        txtDescription = new JTextArea(4, 20);
        txtDescription.setText("Type description here");
        txtDescription.setForeground(Color.GRAY);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(scrollDesc);
        contentPane.add(Box.createVerticalStrut(10));

        // Job Specification (File Upload)
        addLabel("Job Specification", contentPane);
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnBrowse = new JButton("Browse");
        txtSpecification = new JTextField();
        txtSpecification.setEditable(false);
        txtSpecification.setText("No file chosen");
        txtSpecification.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(JobForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    txtSpecification.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        filePanel.add(btnBrowse);
        filePanel.add(Box.createHorizontalStrut(10));
        filePanel.add(txtSpecification);
        contentPane.add(filePanel);

        JLabel lblLimit = new JLabel("Accepts up to 1MB");
        lblLimit.setFont(new Font("SansSerif", Font.ITALIC, 10));
        lblLimit.setForeground(Color.GRAY);
        lblLimit.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(lblLimit);
        contentPane.add(Box.createVerticalStrut(10));

        // Note
        addLabel("Note", contentPane);
        txtNote = new JTextArea(3, 20);
        txtNote.setText("Add note");
        txtNote.setForeground(Color.GRAY);
        JScrollPane scrollNote = new JScrollPane(txtNote);
        scrollNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(scrollNote);
        contentPane.add(Box.createVerticalStrut(20));

        // Required Text
        JLabel lblRequired = new JLabel("* Required");
        lblRequired.setForeground(Color.GRAY);
        lblRequired.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(lblRequired);

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

        // Close DB connection on exit
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
        String title = txtJobTitle.getText();
        String desc = txtDescription.getText();
        String specPath = txtSpecification.getText();
        String note = txtNote.getText();

        // Handle placeholder text
        if ("Type description here".equals(desc))
            desc = "";
        if ("No file chosen".equals(specPath))
            specPath = "";
        if ("Add note".equals(note))
            note = "";

        try {
            JobTitle job = new JobTitle(title, desc, specPath, note);
            boolean saved = dao.save(job);
            if (saved) {
                JOptionPane.showMessageDialog(this, "Job Title Saved Successfully!");
                txtJobTitle.setText("");
                txtDescription.setText("Type description here");
                txtSpecification.setText("No file chosen");
                txtNote.setText("Add note");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(this, "Job Title already exists!", "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JobForm frame = new JobForm();
            frame.setVisible(true);
        });
    }
}