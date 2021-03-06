package com.elesson.pioneer.dao;

import com.elesson.pioneer.dao.exception.DuplicateEntityException;
import com.elesson.pioneer.dao.util.DBHelper;
import com.elesson.pioneer.model.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.elesson.pioneer.dao.TestData.*;
import static org.junit.Assert.*;

public class UserDaoImplTest {



    private UserDao userDao = DaoEntityFactory.getUserDao();

    @Before
    public void setUp() throws Exception {
        DBHelper.initDB();
    }


    @Test
    public void getAll() {
        assertEquals(userDao.getAll().size(), 5);
        assertArrayEquals(new User[]{USER_1, USER_2, USER_3, USER_4, USER_5}, userDao.getAll().toArray());
    }

    @Test
    public void save() {
        userDao.save(USER_6);
        assertArrayEquals(new User[]{USER_1, USER_2, USER_3, USER_4, USER_5, USER_6}, userDao.getAll().toArray());
    }

    //test pass for MySql, fails for H2. Reason: different exception wording for db constraints
    @Test(expected = DuplicateEntityException.class)
    @Ignore
    public void saveDuplicate() {
        userDao.save(USER_6);
        userDao.save(USER_6_2);
    }

    @Test
    public void update() {
        userDao.save(USER_7);
        assertArrayEquals(new User[]{USER_1, USER_7, USER_3, USER_4, USER_5}, userDao.getAll().toArray());
    }

    @Test
    public void delete() {
        assertNotNull(userDao.getById(3));
        userDao.delete(3);
        assertNull(userDao.getById(3));
        userDao.delete(30);
        assertArrayEquals(new User[]{USER_1, USER_2, USER_4, USER_5}, userDao.getAll().toArray());
    }

    @Test
    public void getByEmail() {
        assertEquals(USER_1, userDao.getByEmail("ro@gmail.com"));
    }

    @Test
    public void getById() {
        assertEquals(USER_1, userDao.getById(1));
        assertEquals(USER_3, userDao.getById(3));
    }

    @Test
    public void getNotFound() throws Exception {
        assertNull(userDao.getById(11));
        assertNull(userDao.getByEmail(""));
    }
}