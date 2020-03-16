$(".answer-write input[type='submit']").on("click", addAnswer);

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
    console.log(data);
    console.log("failure");
}

function onSuccess(data, status) {
    console.log(data);
    let answerTemplate = $("#answerTemplate").html();
    let template = answerTemplate.format(data.writer.userId, data.formattedWrittenTime, data.contents, data.id, data.id);
    $(".qna-comment-slipp-articles").prepend(template);

    $(".answer-write textarea").val("");
}

String.prototype.format = function () {
    let args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};
