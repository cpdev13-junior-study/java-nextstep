package chapter6to12.next.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class QuestionTest {
    public static Question newQuestion(String writer) {
        return new Question(1L, writer, "title", "contents", new Date(), 0);
    }

    public static Question newQuestion(long questionId, String writer) {
        return new Question(questionId, writer, "title", "contents", new Date(), 0);
    }

    public static User newUser(String userId) {
        return new User(userId, "pw", "name", "email");
    }

    public static Answer newAnswer(String writer) {
        return new Answer(writer, "contents", 1L);
    }

    @Test
    void canDelete_글쓴이_다르다() throws Exception {
        User user = newUser("hcsung");
        Question question = newQuestion("writer");
        Assertions.assertFalse(question.canDelete(user, new ArrayList<Answer>()));
    }

    @Test
    void canDelete_글쓴이_같음_답변_없음() throws Exception {
        User user = newUser("hcsung");
        Question question = newQuestion("hcsung");
        Assertions.assertTrue(question.canDelete(user, new ArrayList<Answer>()));
    }

    @Test
    void canDelete_같은_사용자_답변() throws Exception {
        String userId = "hcsung";
        User user = newUser(userId);
        Question question = newQuestion(userId);
        List<Answer> answers = Arrays.asList(newAnswer(userId));
        Assertions.assertTrue(question.canDelete(user, answers));
    }

    @Test
    void canDelete_다른_사용자_답변() throws Exception {
        String userId = "hcsung";
        User user = newUser(userId);
        Question question = newQuestion(userId);
        List<Answer> answers = Arrays.asList(newAnswer("javajigi"));
        Assertions.assertFalse(question.canDelete(user, answers));
    }

}