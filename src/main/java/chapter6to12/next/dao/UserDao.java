package chapter6to12.next.dao;

import chapter6to12.core.annotation.Repository;
import chapter6to12.next.jdbc.JdbcTemplate;
import chapter6to12.next.model.User;

import java.util.List;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.insert(
                sql,
                pstmt -> {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                }
        );
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
        return jdbcTemplate.queryForObject(
                sql,
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")),
                userId
        );
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.query(
                sql,
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"))
        );
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?,email=?, name=? WHERE userId=?";
        jdbcTemplate.update(
                sql,
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                user.getUserId()
        );
    }
}
