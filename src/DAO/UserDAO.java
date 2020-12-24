package DAO;

import model.User;
import utils.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends DataAccessObject<User> {

    private static final String GET_USER = "SELECT userId, userName, password, " +
            "active, createDate, createdBy, lastUpdate, lastUpdateBy " +
            "FROM user WHERE userName=? AND password=?";

    public UserDAO(Connection connection) {
        super(connection);
    }

    public Long findUserId(String username, String password) {
        User user = new User();

        try (PreparedStatement statement = this.connection.prepareStatement(GET_USER);) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                user.setId(rs.getLong("userId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return user.getId();
    }

    @Override
    public User findById(Long id) {
        User user = new User();
        try (PreparedStatement ps = this.connection.prepareStatement(GET_USER);) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getLong("userId"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return user;
    }

    public ResultSet findAll() {
        return null;
    }

    @Override
    public User update(User dto) {
        return null;
    }

    @Override
    public User create(User dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
