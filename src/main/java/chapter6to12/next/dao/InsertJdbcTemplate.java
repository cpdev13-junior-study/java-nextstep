package chapter6to12.next.dao;

import chapter6to12.core.jdbc.ConnectionManager;
import chapter6to12.next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJdbcTemplate {

    public void insert(User user, UserDao userDao) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = userDao.createQueryForInsert();
            pstmt = con.prepareStatement(sql);
            userDao.setValuesForInsert(user, pstmt);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }
}
