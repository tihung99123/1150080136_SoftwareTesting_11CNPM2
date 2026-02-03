package fploy.bai4;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OrganizationTest {

    private OrganizationDAO dao;

    @Before
    public void setUp() throws Exception {
        dao = new OrganizationDAO();
        dao.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        if (dao != null) dao.close();
    }

    @Test
    public void testSaveAndFind() throws Exception {
        OrganizationUnit unit = new OrganizationUnit("u1", "HR", "Human Resources");
        boolean saved = dao.save(unit);
        assertTrue(saved);

        OrganizationUnit found = dao.findById("u1");
        assertNotNull(found);
        assertEquals("HR", found.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation() throws Exception {
        // Name is required; saving should throw IllegalArgumentException
        OrganizationUnit invalid = new OrganizationUnit("u2", "", "No Name");
        dao.save(invalid);
    }

    @Test(expected = java.sql.SQLException.class)
    public void testDuplicateUnit() throws Exception {
        OrganizationUnit unit = new OrganizationUnit("u3", "IT", "Info Tech");
        assertTrue(dao.save(unit));
        // Saving the same unit again should raise a SQL exception (PRIMARY KEY violation)
        dao.save(unit);
    }
}