package chapter6to12.next.dao;

import chapter6to12.core.annotation.Repository;
import chapter6to12.next.jdbc.JdbcTemplate;
import chapter6to12.next.jdbc.KeyHolder;
import chapter6to12.next.jdbc.PreparedStatementCreator;
import chapter6to12.next.jdbc.RowMapper;
import chapter6to12.next.model.Question;

import java.sql.*;
import java.util.List;

@Repository
public class JdbcQuestionDao implements QuestionDao{

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    @Override
    public List<Question> findAll() {
        String sql = "SELECT questionId, writer, title, createdDate, countOfAnswer FROM QUESTIONS "
                + "order by questionId desc";

        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"), null,
                        rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));
            }

        };

        return jdbcTemplate.query(sql, rm);
    }

    @Override
    public Question insert(Question question){
        String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfAnswer) VALUES (?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, question.getWriter());
                pstmt.setString(2, question.getTitle());
                pstmt.setString(3, question.getContents());
                pstmt.setTimestamp(4, new Timestamp(question.getTimeFromCreateDate()));
                pstmt.setInt(5, question.getCountOfComment());
                return pstmt;
            }
        };

        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return findById(keyHolder.getId());
    }

    @Override
    public Question findById(long questionId) {
        JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
        String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS "
                + "WHERE questionId = ?";

        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"),
                        rs.getString("contents"), rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));
            }
        };

        return jdbcTemplate.queryForObject(sql, rm, questionId);
    }

    @Override
    public void increaseCount(long questionId) {
        String sql = "UPDATE QUESTIONS SET countOfAnswer = countOfAnswer+1 WHERE questionId=?";
        jdbcTemplate.update(
                sql,
                questionId
        );
    }

    @Override
    public void decreaseCount(long questionId) {
        String sql = "UPDATE QUESTIONS SET countOfAnswer = countOfAnswer-1 WHERE questionId=?";
        jdbcTemplate.update(
                sql,
                questionId
        );
    }

    @Override
    public void update(Question question) {
        String sql = "UPDATE QUESTIONS SET title=?,contents=? WHERE questionId=?";
        jdbcTemplate.update(
                sql,
                question.getTitle(),
                question.getContents(),
                question.getQuestionId()
        );
    }

    @Override
    public void delete(long questionId){
        String sql = "DELETE FROM QUESTIONS WHERE questionId=?";
        jdbcTemplate.update(
                sql,
                questionId
        );
    }
}
