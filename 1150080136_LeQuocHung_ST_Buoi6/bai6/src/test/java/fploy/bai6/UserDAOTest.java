package fploy.bai6;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserDAOTest {
    private UserDAO dao;

    @Before
    public void setUp() throws SQLException {
        dao = new UserDAO();
        // Clean table before each test
        dao.delete("testuser");
        dao.delete("testuser2");
    }

    @After
    public void tearDown() throws SQLException {
        if (dao != null) dao.close();
    }

    @Test
    public void testInsertAndFind() throws SQLException {
        User u = new User("testuser", "pass", "Test User", "test@example.com");
        assertTrue(dao.insert(u));

        User found = dao.findByUsername("testuser");
        assertNotNull(found);
        assertEquals("Test User", found.getFullname());
    }

    @Test
    public void testUpdate() throws SQLException {
        User u = new User("testuser2", "pass", "User2", "u2@example.com");
        assertTrue(dao.insert(u));

        u.setFullname("User2 Updated");
        u.setEmail("u2new@example.com");
        assertTrue(dao.update(u));

        User found = dao.findByUsername("testuser2");
        assertNotNull(found);
        assertEquals("User2 Updated", found.getFullname());
        assertEquals("u2new@example.com", found.getEmail());
    }

    @Test
    public void testDelete() throws SQLException {
        User u = new User("testuser", "pass", "Test User", "test@example.com");
        assertTrue(dao.insert(u));
        assertTrue(dao.delete("testuser"));
        assertNull(dao.findByUsername("testuser"));
    }

    @Test(expected = SQLException.class)
    public void testDuplicateInsert() throws SQLException {
        User u = new User("testuser", "pass", "Test User", "test@example.com");
        assertTrue(dao.insert(u));
        // inserting same username should throw SQLException due to PRIMARY KEY
        dao.insert(u);
    }
}