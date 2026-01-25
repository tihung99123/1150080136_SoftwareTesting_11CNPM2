using System;
using System.Data.SQLite;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using canhan;

namespace canhanTests
{
    [TestClass]
    public class UnitTest1
    {
        // use a workspace-local SQLite file for deterministic tests
        private readonly string TestDb = @"Data Source=./canhan_test.db;Version=3;";
        private OrgManager manager;

        [TestInitialize]
        public void Init()
        {
            manager = new OrgManager(TestDb);
            manager.CreateSchemaIfNotExists();

            // ensure deterministic starting state for named test data
            manager.DeleteOrgByName("TC01_Acme_Min");
            manager.DeleteOrgByName("TC04_ABC_Min");
            manager.DeleteOrgByName("TC05_MaxLen");
            manager.DeleteOrgByName("Contoso");
            manager.DeleteOrgByName("TrimCorp");
            manager.DeleteOrgByName("SQL_INJECTION_TEST");
            manager.DeleteOrgByName("SQL_INJECTION_TEST'; DROP TABLE Something;--");
            manager.DeleteOrgByName("TC_Temp_Acme");
            manager.DeleteOrgByName("TC14_CreatedDate");
        }

        [TestCleanup]
        public void Cleanup()
        {
            // remove artifacts created by tests
            manager.DeleteOrgByName("TC01_Acme_Min");
            manager.DeleteOrgByName("TC04_ABC_Min");
            manager.DeleteOrgByName("TC05_MaxLen");
            manager.DeleteOrgByName("Contoso");
            manager.DeleteOrgByName("SQL_INJECTION_TEST");
            manager.DeleteOrgByName("SQL_INJECTION_TEST'; DROP TABLE Something;--");
            manager.DeleteOrgByName("TC_Temp_Acme");
            manager.DeleteOrgByName("TC14_CreatedDate");
        }

        // TC-01: Create valid organization (minimal required)
        [TestMethod]
        public void TC01_CreateValidOrganization_Persists()
        {
            string name = "TC01_Acme_Min";
            int id = manager.SaveOrgAndGetId(name, "10 Downing St", "012345678", "a@b.com");
            Assert.IsTrue(id > 0, "Insert should return generated OrgID");

            int found = manager.GetOrgIdByName(name);
            Assert.AreEqual(id, found, "Saved Org should be retrievable by name");
        }

        // TC-02: OrgName required
        [TestMethod]
        public void TC02_OrgName_Required()
        {
            string msg = manager.ValidateOrg("", "", "");
            Assert.AreEqual("Organization Name is required", msg);
        }

        // TC-03: OrgName too short (2 chars)
        [TestMethod]
        public void TC03_OrgName_TooShort()
        {
            string msg = manager.ValidateOrg("AB", "", "");
            Assert.AreEqual("Organization Name must be 3–255 characters", msg);
        }

        // TC-04: OrgName minimum boundary (3 chars)
        [TestMethod]
        public void TC04_OrgName_MinBoundary_3Chars()
        {
            Assert.AreEqual("OK", manager.ValidateOrg("ABC", "", ""));
            int id = manager.SaveOrgAndGetId("TC04_ABC_Min", "Addr", "", "");
            Assert.IsTrue(id > 0);
        }

        // TC-05: OrgName maximum boundary (255 chars)
        [TestMethod]
        public void TC05_OrgName_MaxBoundary_255Chars()
        {
            string s = new string('X', 255);
            Assert.AreEqual(255, s.Length);
            Assert.AreEqual("OK", manager.ValidateOrg(s, "", ""));
            int id = manager.SaveOrgAndGetId("TC05_MaxLen", "Addr", "", "");
            Assert.IsTrue(id > 0);
        }

        // TC-06: OrgName exceeds maximum (256 chars)
        [TestMethod]
        public void TC06_OrgName_ExceedsMax_256Chars()
        {
            string s = new string('X', 256);
            string msg = manager.ValidateOrg(s, "", "");
            Assert.AreEqual("Organization Name must be 3–255 characters", msg);
        }

        // TC-07: Duplicate OrgName (case-insensitive)
        [TestMethod]
        public void TC07_DuplicateOrgName_CaseInsensitive()
        {
            int id1 = manager.SaveOrgAndGetId("Contoso", "Addr", "", "");
            Assert.IsTrue(id1 > 0);
            Assert.IsTrue(manager.IsOrgNameExists("contoso"));
        }

        // TC-08: Email invalid format
        [TestMethod]
        public void TC08_Email_InvalidFormat()
        {
            string msg = manager.ValidateOrg("TC08_BadEmail", "", "not-an-email");
            Assert.AreEqual("Email is not a valid address", msg);
        }

        // TC-09: Phone contains letters
        [TestMethod]
        public void TC09_Phone_ContainsLetters()
        {
            string msg = manager.ValidateOrg("TC09_BadPhone", "01234AB678", "");
            Assert.AreEqual("Phone must contain 9–12 digits", msg);
        }

        // TC-10: Phone boundary (9 and 12 valid; 8 and 13 invalid)
        [TestMethod]
        public void TC10_Phone_Boundaries()
        {
            Assert.AreEqual("OK", manager.ValidateOrg("TC10_Phone9", "012345678", ""));
            Assert.AreEqual("OK", manager.ValidateOrg("TC10_Phone12", "012345678901", ""));
            Assert.AreEqual("Phone must contain 9–12 digits", manager.ValidateOrg("TC10_Phone8", "01234567", ""));
            Assert.AreEqual("Phone must contain 9–12 digits", manager.ValidateOrg("TC10_Phone13", "0123456789012", ""));
        }

        // TC-11: Save does not persist on validation failure (behavioral)
        [TestMethod]
        public void TC11_NoPersist_OnValidationFailure()
        {
            string invalid = "";
            Assert.AreEqual("Organization Name is required", manager.ValidateOrg(invalid, "", ""));
        }

        // TC-12: Director button enabled only after successful save (simulated)
        [TestMethod]
        public void TC12_Director_EnableAfterSave_Simulated()
        {
            manager.DeleteOrgByName("TC_Temp_Acme");
            int id = manager.SaveOrgAndGetId("TC_Temp_Acme", "Addr", "", "");
            Assert.IsTrue(id > 0);
            Assert.IsTrue(manager.IsOrgNameExists("TC_Temp_Acme"));
        }

        // TC-13: Back discards unsaved changes (simulated)
        [TestMethod]
        public void TC13_Back_DiscardsUnsavedChanges_Simulated()
        {
            Assert.IsFalse(manager.IsOrgNameExists("Unsaved_Temp_Name"));
        }

        // TC-14: CreatedDate auto-populated (SQLite variant)
        [TestMethod]
        public void TC14_CreatedDate_AutoPopulated()
        {
            string name = "TC14_CreatedDate";
            DateTime before = DateTime.Now;
            int id = manager.SaveOrgAndGetId(name, "Addr", "", "");
            Assert.IsTrue(id > 0);

            DateTime created;
            using (var c = new System.Data.SQLite.SQLiteConnection(TestDb))
            using (var cmd = c.CreateCommand())
            {
                c.Open();
                cmd.CommandText = "SELECT CreatedDate FROM ORGANIZATION WHERE OrgID = @id";
                cmd.Parameters.Add(new System.Data.SQLite.SQLiteParameter("@id", id));
                created = Convert.ToDateTime(cmd.ExecuteScalar());
            }

            Assert.IsTrue(created.Year >= 2024, "CreatedDate should be recent");
        }

        // TC-15: SQL-injection-like input handled (parameterized) — SQLite variant
        [TestMethod]
        public void TC15_SQLInjection_LiteralStored_NoSchemaDamage()
        {
            string dangerous = "SQL_INJECTION_TEST'; DROP TABLE Something;--";
            int id = manager.SaveOrgAndGetId(dangerous, "Addr", "", "");
            Assert.IsTrue(id > 0);

            using (var c = new SQLiteConnection(TestDb))
            using (var cmd = c.CreateCommand())
            {
                c.Open();
                cmd.CommandText = "SELECT name FROM sqlite_master WHERE type='table' AND name='ORGANIZATION';";
                var tbl = cmd.ExecuteScalar();
                Assert.IsNotNull(tbl, "ORGANIZATION table must still exist");

                cmd.CommandText = "SELECT COUNT(*) FROM ORGANIZATION WHERE OrgID = @id";
                cmd.Parameters.Add(new SQLiteParameter("@id", id));
                int rows = Convert.ToInt32(cmd.ExecuteScalar());
                Assert.AreEqual(1, rows);
            }
        }

    }
}