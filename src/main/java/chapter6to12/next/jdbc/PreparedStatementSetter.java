package chapter6to12.next.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter {
    void setValues(PreparedStatement pstmt) throws SQLException;
}
