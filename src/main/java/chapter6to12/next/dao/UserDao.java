package chapter6to12.next.dao;

import chapter6to12.next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }

            @Override
            Object mapRow(ResultSet resultSet) throws SQLException {
                return null;
            }
        };
        insertJdbcTemplate.insert(sql);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }

            @Override
            Object mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };
        return (User) jdbcTemplate.queryForObject(sql);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {}

            @Override
            Object mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };
        return jdbcTemplate.query(sql);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?,email=?, name=? WHERE userId=?";
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getUserId());
            }

            @Override
            Object mapRow(ResultSet resultSet) throws SQLException {
                return null;
            }
        };
        updateJdbcTemplate.update(sql);
    }
}
