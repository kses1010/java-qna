$(document).on('click', ".answer-write input[type='submit']", addAnswer);

function addAnswer(e) {
    e.preventDefault();
    console.log("click me");

    let queryString = $(".answer-write").serialize();
    console.log("query : " + queryString);

    let url = $(".answer-write").attr("action");
    console.log("url : " + url);

    $.ajax({
        type: 'post',
        url: url,
        data: queryString,
        dataType: 'json',
        error: onError,
        success: onSuccess
    })
}

function onError(data, status) {
    console.error(data);
    console.error("failure");
}

function onSuccess(data, status) {
    console.log(data);
    let answerTemplate = $("#answerTemplate").html();
    let template = answerTemplate.format(data.writer.userId, data.createdWrittenTime, data.contents, data.question.id, data.id);
    $(".qna-comment-slipp-articles").prepend(template);
    $(".qna-comment-count strong").text(data.question.countOfAnswer);
    $(".answer-write textarea").val("");
}

$(document).on('click', '.link-delete-article', deleteAnswer);

function deleteAnswer(e) {
    e.preventDefault();

    let deleteBtn = $(this);
    let url = deleteBtn.attr("href");
    console.log("url : " + url);

    $.ajax({
        type: 'delete',
        url: url,
        dataType: 'json',
        error: function (xhr, status) {
            console.error("error");
        },
        success: function (data, status) {
            console.log(data);
            if (data.valid) {
                deleteBtn.closest("article").remove();
                $(".qna-comment-count strong").text(data.question.countOfAnswer);
            } else {
                alert(data.errorMessage);
            }
        }
    })
}

String.prototype.format = function () {
    let args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] !== 'undefined'
            ? args[number]
            : match;
    });
};
