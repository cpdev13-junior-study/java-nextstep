package chapter6to12.next.service;


import chapter6to12.next.dao.JdbcAnswerDao;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.model.Question;
import chapter6to12.next.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private JdbcQuestionDao questionDao;
    @Mock
    private JdbcAnswerDao answerDao;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void deleteQuestion_없는_질문() throws Exception {
        Mockito.when(questionDao.findById(1L)).thenReturn(null);
        Assertions.assertFalse(questionService.deleteQuestion(1L, new User("2", "pw", "name", "email")));
    }

    @Test
    void deleteQuestion_삭제할수_있음() throws Exception {
        User user = newUser("userId");
        Question question = new Question(1L, user.getUserId(), "title", "contents", new Date(), 0) {
            @Override
            public boolean canDelete(User user, List<Answer> answerList) {
                return true;
            }
        };
        Mockito.when(questionDao.findById(1L)).thenReturn(question);
        questionService.deleteQuestion(1L, user);
        Mockito.verify(questionDao).delete(question.getQuestionId());
    }

    public static User newUser(String userId) {
        return new User(userId, "pw", "name", "email");
    }
}