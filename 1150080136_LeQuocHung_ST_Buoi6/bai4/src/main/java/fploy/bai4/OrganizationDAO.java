package fploy.bai4;

import java.sql.*;

public class OrganizationDAO {
    private Connection connection;

    public OrganizationDAO() throws SQLException {
        // Kết nối SQLite database
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:organization.db");
            createTable();
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found");
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS organization_unit (" +
                "unit_id TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "description TEXT)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public boolean save(OrganizationUnit unit) throws SQLException {
        unit.validate(); // Validate before saving

        String sql = "INSERT INTO organization_unit (unit_id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, unit.getUnitId());
            pstmt.setString(2, unit.getName());
            pstmt.setString(3, unit.getDescription());
            return pstmt.executeUpdate() > 0;
        }
    }

    public OrganizationUnit findById(String unitId) throws SQLException {
        String sql = "SELECT * FROM organization_unit WHERE unit_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, unitId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new OrganizationUnit(
                        rs.getString("unit_id"),
                        rs.getString("name"),
                        rs.getString("description"));
            }
        }
        return null;
    }

    public boolean delete(String unitId) throws SQLException {
        String sql = "DELETE FROM organization_unit WHERE unit_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, unitId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM organization_unit";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}