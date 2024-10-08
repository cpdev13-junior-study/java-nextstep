package chapter6to12.next.service;

import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.Service;
import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.model.Question;
import chapter6to12.next.model.User;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    @Inject
    public QuestionService(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    public boolean deleteQuestion(Long questionId, User user) {
        Question findQuestion = questionDao.findById(questionId);
        if (findQuestion == null) return false;
        List<Answer> findAnswerList = answerDao.findAllByQuestionId(questionId);

        if (findQuestion.canDelete(user, findAnswerList)) {
            questionDao.delete(questionId);
            return true;
        }
        return false;
    }
}
