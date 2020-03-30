package com.codessquad.qna.answer;

import com.codessquad.qna.question.Question;
import com.codessquad.qna.question.QuestionRepository;
import com.codessquad.qna.user.User;
import com.codessquad.qna.utils.HttpSessionUtils;
import com.codessquad.qna.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
    private Logger log = LoggerFactory.getLogger(ApiAnswerController.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("")
    public Answer createAnswer(@PathVariable Long questionId, String contents, HttpSession session) {
        if (HttpSessionUtils.isNoneExistentUser(session)) {
            return null;
        }
        log.info("세션확인완료");
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).orElseThrow(IllegalStateException::new);
        Answer answer = new Answer(loginUser, question, contents);
        question.addAnswer();
        return answerRepository.save(answer);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
        if (HttpSessionUtils.isNoneExistentUser(session)) {
            return Result.fail("로그인해야 합니다.");
        }
        Answer answer = answerRepository.findById(id).orElseThrow(IllegalStateException::new);
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        if (answer.isNotSameWriter(loginUser)) {
            log.info("delete fail");
            return Result.fail("자신의 글만 삭제할 수 있습니다.");
        }
        answerRepository.delete(answer);
        Question question = questionRepository.findById(questionId).orElseThrow(IllegalStateException::new);
        question.deleteAnswer();
        questionRepository.save(question);
        return Result.ok(question);
    }
}
