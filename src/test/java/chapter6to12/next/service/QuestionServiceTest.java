package chapter6to12.next.service;


import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.model.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private JdbcQuestionDao questionDao;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void deleteQuestion_없는_질문() throws Exception {
        Mockito.when(questionDao.findById(1L)).thenReturn(
                new Question(1L, "writer", "title", "contents", Date.from(Instant.now()), 2)
        );

        Assertions.assertFalse(questionService.deleteQuestion(1L));
    }
}