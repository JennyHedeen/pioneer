package com.elesson.pioneer.dao;

import com.elesson.pioneer.dao.exception.DBException;
import com.elesson.pioneer.dao.exception.DuplicateEntityException;
import com.elesson.pioneer.dao.util.ConnectionPool;
import com.elesson.pioneer.dao.util.DBConnection;
import com.elesson.pioneer.model.Entity;
import com.elesson.pioneer.model.EntityFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides implementation of all {@code JDBCDao} interface methods.
 */
public class JDBCDao implements Dao {

    private static final Logger logger = LogManager.getLogger(JDBCDao.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(String query, int id) {
        int resultRows = 0;
        try(DBConnection con = ConnectionPool.getPool().getConnection()) {
            PreparedStatement pst = con.prepareStatement(query, id);
            resultRows = pst.executeUpdate();
            logger.info("Deleted entity id={}", id);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DBException("Unable to delete record");
        }
        return resultRows == 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> T get(Class cl, String query, Object... values) {
        T entity = null;
        try(DBConnection con = ConnectionPool.getPool().getConnection()) {
            PreparedStatement pst = con.prepareStatement(query, values);
            ResultSet rs = pst.executeQuery();

            if(rs.next()) {
                entity = (T)cl.getConstructor(ResultSet.class).newInstance(rs);
                logger.info("Entity obtained: " + entity.toString());
            }
            rs.close();
        } catch (SQLException e) {
            logger.error(e);
            throw new DBException(e.getMessage());
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e);
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> List<T> getAll(Class cl, String query, Object... values) {
        List<T> list = new ArrayList<>();
        T entity = null;
        try(DBConnection con = ConnectionPool.getPool().getConnection()) {
            PreparedStatement pst = con.prepareStatement(query, values);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                entity = (T)cl.getConstructor(ResultSet.class).newInstance(rs);
                list.add(entity);
                logger.info("Entity obtained: " + entity.toString());
            }
            rs.close();
        } catch (SQLException e) {
            logger.error(e);
            throw new DBException("Unable to obtain record");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> T save(T entity, String query, Object... values) {
        try(DBConnection con = ConnectionPool.getPool().getConnection()) {
            PreparedStatement pst = con.prepareInsertStatement(query, values);
            if(pst.executeUpdate()==1) {
                ResultSet rs = pst.getGeneratedKeys();
                rs.next();
                entity.setId(rs.getInt(1));
                logger.info("New entity created with id={}", entity.getId());
                rs.close();
            }
        } catch (SQLException e) {
            if(e.getMessage().contains("Duplicate")) {
                logger.error(e);
                throw new DuplicateEntityException();
            }
            logger.error(e);
            throw new DBException("Unable to saveAll new record");
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> int save(String query, Object[][] values) {
        int count = 0;
        DBConnection con = ConnectionPool.getPool().getConnection();
        try {
            PreparedStatement pst;
            con.setAutoCommit(false);
            for(Object[] ob : values) {
                pst = con.prepareInsertStatement(query, ob);
                pst.executeUpdate();
                count++;
            }
            con.commit();
        } catch (SQLException e) {
            if(e.getMessage().contains("Duplicate")) {
                logger.error(e);
                throw new DuplicateEntityException();
            }
            logger.error(e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                logger.error(e1);
            }
            throw new DBException("Unable to save new records");
        }
        finally {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> T update(T entity, String query, Object... values) {
        try(DBConnection con = ConnectionPool.getPool().getConnection()) {
            PreparedStatement pst = con.prepareStatement(query, values);
            if(pst.executeUpdate()!=1) {
                logger.error("Entity was not not updated");
                throw new DBException("Unable to update record");
            }
            logger.info("User data successfully updated for id=" + entity.getId());
        } catch (SQLException e) {
            if(e.getMessage().contains("Duplicate")) {
                logger.error(e);
                throw new DuplicateEntityException();
            }
            logger.error(e);
            throw new DBException("Unable to saveAll new record");
        }
        return entity;
    }
}
