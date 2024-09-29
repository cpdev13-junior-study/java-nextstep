package chapter6to12.next.service;

import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.model.Question;

public class QuestionService {

    private final JdbcQuestionDao questionDao;

    public QuestionService(JdbcQuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public boolean deleteQuestion(Long questionId) {
        Question findQuestion = questionDao.findById(questionId);
        if (findQuestion.getCountOfComment() != 0) {
            return false;
        }
        questionDao.delete(questionId);
        return true;
    }
}
