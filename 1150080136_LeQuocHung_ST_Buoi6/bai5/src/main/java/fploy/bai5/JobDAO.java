package fploy.bai5;

import java.sql.*;

public class JobDAO {
    private Connection connection;

    public JobDAO() throws SQLException {
        // Kết nối SQLite database
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:organization.db"); // Use same DB file
            createTable();
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found");
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS job_title (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL UNIQUE," +
                "description TEXT," +
                "specification_path TEXT," +
                "note TEXT)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public boolean save(JobTitle job) throws SQLException {
        job.validate();

        String sql = "INSERT INTO job_title (title, description, specification_path, note) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, job.getTitle());
            pstmt.setString(2, job.getDescription());
            pstmt.setString(3, job.getSpecificationPath());
            pstmt.setString(4, job.getNote());
            return pstmt.executeUpdate() > 0;
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}