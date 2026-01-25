using System;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Text.RegularExpressions;

// SQLite provider (NuGet: System.Data.SQLite.Core)
using System.Data.SQLite;

namespace canhan
{
    /// <summary>
    /// Data access + validation for ORGANIZATION. Supports SQL Server (legacy) and SQLite (file/in-memory).
    /// </summary>
    public class OrgManager
    {
        private readonly string connectionString;
        private readonly bool useSqlite;

        public OrgManager() : this(ConfigurationManager.ConnectionStrings["DefaultConnection"]?.ConnectionString)
        {
        }

        // Allow tests to inject a connection string (SQLite file recommended for tests)
        public OrgManager(string connectionString)
        {
            this.connectionString = connectionString ?? throw new ArgumentNullException(nameof(connectionString));
            var lower = connectionString.ToLowerInvariant();
            useSqlite = lower.Contains(".db") || lower.Contains("mode=memory") || (lower.Contains("datasource=") && lower.Contains(".db"));
        }

        /// <summary>
        /// Validate input according to specification. Returns standardized English messages
        /// expected by the UI. Success => "OK".
        /// </summary>
        public string ValidateOrg(string name, string phone, string email)
        {
            name = name?.Trim();

            if (string.IsNullOrWhiteSpace(name))
                return "Organization Name is required";

            if (name.Length < 3 || name.Length > 255)
                return "Organization Name must be 3–255 characters";

            if (!string.IsNullOrEmpty(phone))
            {
                // Phone: digits only, length 9-12
                if (!Regex.IsMatch(phone, "^\\d{9,12}$"))
                    return "Phone must contain 9–12 digits";
            }

            if (!string.IsNullOrEmpty(email))
            {
                // Simple but practical email regex for UI validation
                if (!Regex.IsMatch(email, @"^[^@\s]+@[^@\s]+\.[^@\s]+$"))
                    return "Email is not a valid address";
            }

            return "OK";
        }

        /// <summary>
        /// Case-insensitive existence check for OrgName (trims input first).
        /// Works for both SQL Server and SQLite.
        /// </summary>
        public bool IsOrgNameExists(string name)
        {
            if (string.IsNullOrWhiteSpace(name))
                return false;

            name = name.Trim();

            if (useSqlite)
            {
                using (var conn = new SQLiteConnection(connectionString))
                using (var cmd = conn.CreateCommand())
                {
                    conn.Open();
                    cmd.CommandText = "SELECT COUNT(*) FROM ORGANIZATION WHERE LOWER(OrgName) = LOWER(@Name)";
                    cmd.Parameters.Add(new SQLiteParameter("@Name", name));
                    return Convert.ToInt32(cmd.ExecuteScalar()) > 0;
                }
            }

            using (var connSql = new SqlConnection(connectionString))
            using (var cmdSql = connSql.CreateCommand())
            {
                connSql.Open();
                cmdSql.CommandText = "SELECT COUNT(*) FROM ORGANIZATION WHERE LOWER(OrgName) = LOWER(@Name)";
                cmdSql.Parameters.AddWithValue("@Name", name);
                return Convert.ToInt32(cmdSql.ExecuteScalar()) > 0;
            }
        }

        // Backward-compatible SaveOrg that returns success flag
        public bool SaveOrg(string name, string address, string phone, string email)
        {
            return SaveOrgAndGetId(name, address, phone, email) > 0;
        }

        /// <summary>
        /// Inserts an Organization and returns the generated OrgID; returns -1 on failure.
        /// Works with both SQL Server and SQLite.
        /// </summary>
        public int SaveOrgAndGetId(string name, string address, string phone, string email)
        {
            if (string.IsNullOrWhiteSpace(name))
                return -1;

            name = name.Trim();

            if (useSqlite)
            {
                using (var conn = new SQLiteConnection(connectionString))
                using (var cmd = conn.CreateCommand())
                {
                    conn.Open();
                    cmd.CommandText = @"INSERT INTO ORGANIZATION (OrgName, Address, Phone, Email)
                                        VALUES (@Name, @Address, @Phone, @Email);
                                        SELECT last_insert_rowid();";

                    cmd.Parameters.Add(new SQLiteParameter("@Name", name));
                    cmd.Parameters.Add(new SQLiteParameter("@Address", string.IsNullOrEmpty(address) ? (object)DBNull.Value : address));
                    cmd.Parameters.Add(new SQLiteParameter("@Phone", string.IsNullOrEmpty(phone) ? (object)DBNull.Value : phone));
                    cmd.Parameters.Add(new SQLiteParameter("@Email", string.IsNullOrEmpty(email) ? (object)DBNull.Value : email));

                    object o = cmd.ExecuteScalar();
                    return (o == null || o == DBNull.Value) ? -1 : Convert.ToInt32(o);
                }
            }

            using (var connSql = new SqlConnection(connectionString))
            using (var cmdSql = connSql.CreateCommand())
            {
                connSql.Open();
                cmdSql.CommandText = @"INSERT INTO ORGANIZATION (OrgName, Address, Phone, Email, CreatedDate)
                                        OUTPUT INSERTED.OrgID
                                        VALUES (@Name, @Address, @Phone, @Email, GETDATE())";

                cmdSql.Parameters.AddWithValue("@Name", name);
                cmdSql.Parameters.AddWithValue("@Address", string.IsNullOrEmpty(address) ? (object)DBNull.Value : address);
                cmdSql.Parameters.AddWithValue("@Phone", string.IsNullOrEmpty(phone) ? (object)DBNull.Value : phone);
                cmdSql.Parameters.AddWithValue("@Email", string.IsNullOrEmpty(email) ? (object)DBNull.Value : email);

                object result = cmdSql.ExecuteScalar();
                return (result == null) ? -1 : Convert.ToInt32(result);
            }
        }

        /// <summary>
        /// Helper for tests: returns OrgID for a given name (case-insensitive), or -1 if not found.
        /// </summary>
        public int GetOrgIdByName(string name)
        {
            if (string.IsNullOrWhiteSpace(name))
                return -1;

            name = name.Trim();

            if (useSqlite)
            {
                using (var conn = new SQLiteConnection(connectionString))
                using (var cmd = conn.CreateCommand())
                {
                    conn.Open();
                    cmd.CommandText = "SELECT OrgID FROM ORGANIZATION WHERE LOWER(OrgName) = LOWER(@Name)";
                    cmd.Parameters.Add(new SQLiteParameter("@Name", name));
                    object o = cmd.ExecuteScalar();
                    return (o == null || o == DBNull.Value) ? -1 : Convert.ToInt32(o);
                }
            }

            using (var connSql = new SqlConnection(connectionString))
            using (var cmdSql = connSql.CreateCommand())
            {
                connSql.Open();
                cmdSql.CommandText = "SELECT OrgID FROM ORGANIZATION WHERE LOWER(OrgName) = LOWER(@Name)";
                cmdSql.Parameters.AddWithValue("@Name", name);

                object o = cmdSql.ExecuteScalar();
                return (o == null || o == DBNull.Value) ? -1 : Convert.ToInt32(o);
            }
        }

        /// <summary>
        /// Helper for tests: deletes organizations by exact name (case-insensitive).
        /// Use with caution — intended for test cleanup only.
        /// </summary>
        public int DeleteOrgByName(string name)
        {
            if (string.IsNullOrWhiteSpace(name))
                return 0;

            name = name.Trim();

            if (useSqlite)
            {
                using (var conn = new SQLiteConnection(connectionString))
                using (var cmd = conn.CreateCommand())
                {
                    conn.Open();
                    cmd.CommandText = "DELETE FROM ORGANIZATION WHERE LOWER(OrgName) = LOWER(@Name)";
                    cmd.Parameters.Add(new SQLiteParameter("@Name", name));
                    return cmd.ExecuteNonQuery();
                }
            }

            using (var connSql = new SqlConnection(connectionString))
            using (var cmdSql = connSql.CreateCommand())
            {
                connSql.Open();
                cmdSql.CommandText = "DELETE FROM ORGANIZATION WHERE LOWER(OrgName) = LOWER(@Name)";
                cmdSql.Parameters.AddWithValue("@Name", name);
                return cmdSql.ExecuteNonQuery();
            }
        }

        /// <summary>
        /// Create ORGANIZATION table when running against an empty SQLite file (idempotent).
        /// Tests call this during setup.
        /// </summary>
        public void CreateSchemaIfNotExists()
        {
            if (!useSqlite)
                return; // only needed for SQLite test DB provisioning

            using (var conn = new SQLiteConnection(connectionString))
            using (var cmd = conn.CreateCommand())
            {
                conn.Open();
                cmd.CommandText = @"DROP TABLE IF EXISTS ORGANIZATION;
                                       CREATE TABLE ORGANIZATION (
                                        OrgID INTEGER PRIMARY KEY AUTOINCREMENT,
                                        OrgName TEXT NOT NULL,
                                        Address TEXT NULL,
                                        Phone TEXT NULL,
                                        Email TEXT NULL,
                                        CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP
                                       );
                                       CREATE UNIQUE INDEX UX_OrgName ON ORGANIZATION(LOWER(OrgName));";
                cmd.ExecuteNonQuery();
            }
        }
    }
}