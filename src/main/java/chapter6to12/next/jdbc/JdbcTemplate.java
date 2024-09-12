package chapter6to12.next.jdbc;

import chapter6to12.core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static JdbcTemplate jdbcTemplate;

    public JdbcTemplate() {
    }

    public static JdbcTemplate getInstance(){
        if(jdbcTemplate==null){
            jdbcTemplate = new JdbcTemplate();
        }
        return jdbcTemplate;
    }
    public void insert(String sql, PreparedStatementSetter preparedStatementSetter) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(String sql, Object... params) {
        update(sql, createPreparedStatementSetter(params));
    }

    public void update(PreparedStatementCreator psc, KeyHolder holder) {
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = psc.createPreparedStatement(conn);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                holder.setId(rs.getLong(1));
            }
            rs.close();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> query(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setValues(pstmt);
            rs = pstmt.executeQuery();

            List<T> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(rowMapper.mapRow(rs));
            }
            rs.close();
            return resultList;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... params) {
        return query(sql, createPreparedStatementSetter(params), rowMapper);
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setValues(pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
            rs.close();
            return null;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parms) {
        return queryForObject(sql, createPreparedStatementSetter(parms), rowMapper);
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... params) {
        return pstmt -> {
            for (int i = 1; i <= params.length; i++) {
                pstmt.setObject(i, params[i - 1]);
            }
        };
    }
}
