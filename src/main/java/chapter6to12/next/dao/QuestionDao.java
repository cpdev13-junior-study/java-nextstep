package chapter6to12.next.dao;

import chapter6to12.next.model.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> findAll();

    Question findById(long questionId);

    Question insert(Question question);

    void increaseCount(long questionId);

    void decreaseCount(long questionId);

    void update(Question question);

    void delete(long questionId);
}
