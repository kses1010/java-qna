package com.codessquad.qna.utils;

import com.codessquad.qna.question.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
    private boolean valid;
    private String errorMessage;

    @JsonProperty
    private Question question;

    private Result(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    private Result(boolean valid, Question question) {
        this.valid = valid;
        this.question = question;
    }

    public static Result ok() {
        return new Result(true, "null");
    }

    public static Result ok(Question question) {
        return new Result(true, question);
    }

    public static Result fail(String errorMessage) {
        return new Result(false, errorMessage);
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
