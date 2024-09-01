package chapter6to12.next.dao;

import chapter6to12.next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate();
        insertJdbcTemplate.insert(
                sql,
                pstmt -> {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                }
        );
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return (User) jdbcTemplate.queryForObject(
                sql,
                pstmt -> pstmt.setString(1, userId),
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"))
        );
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query(
                sql,
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"))
        );
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?,email=?, name=? WHERE userId=?";
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate();
        updateJdbcTemplate.update(
                sql,
                pstmt -> {
                    pstmt.setString(1, user.getPassword());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getUserId());
                }
        );
    }
}
