package chapter6to12.next.dao;

import chapter6to12.core.jdbc.ConnectionManager;
import chapter6to12.next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        InsertJdbcTemplate insertJdbcTemplate = new InsertJdbcTemplate() {
            @Override
            String createQueryForInsert() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }

            @Override
            void setValuesForInsert(User user, PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };
        insertJdbcTemplate.insert(user);
    }

    public User findByUserId(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public List<User> findAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                userList.add(user);
            }
            return userList;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public void update(User user) throws SQLException {
        UpdateJdbcTemplate updateJdbcTemplate = new UpdateJdbcTemplate() {
            @Override
            String createQueryForUpdate() {
                return "UPDATE USERS SET password=?,email=?, name=? WHERE userId=?";
            }

            @Override
            void setValuesForUpdate(User user, PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getUserId());
            }
        };
        updateJdbcTemplate.update(user);
    }
}
