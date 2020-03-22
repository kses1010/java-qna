# step5. XHR과 AJAX기반 개발

## comment 사항

- [ ] 중복 코드에 대한 리팩토링 하기
- [x] formattedWrittenTime에서 메서드 용도에 따른 이름 변경하기
- [ ] answers list 활용하기
- [ ] OrderBy 어노테이션보다는 repository의 쿼리메서드를 생성하여 호출하기
- [ ] questionRepository.findById(id).orElseThrow(IllegalStateException::new)) 해당 중복되는 코드 리팩토링하기
- [ ] LocalDateTime 컨버터 삭제.

---

## 5.1 JSON API, AJAX를 활용한 답변 추가 

### StackOverflow방지하는 방법

동영상 그대로 진행했을 경우 StackOverFlow로 인해 작동이 제대로 나지 않는다.

그 원인은 **ORM**을 통한 Question과 Answer간의 매핑은 객체 간 연관관계가 양방향이다.

양방향일 경우 toString사용 시 StackOverFlow가 발생한다. A->B B->A 로 서로 간의 계속되는 참조로 오버스택에러가 발생한다.

String은 항상 새로운 인스턴스를 생성하기 때문에 계속하여 String을 사용할 경우 메모리 낭비가 심하다.

StringBuilder, StringBuffer를 사용하자.

#### <해결방안>

1. Question에서 매핑하는 Answer에 대해서만 @JsonIgnore 어노테이션 적용
2. Answer에서 toString을 삭제. Question에서 Answer매핑하여 Answer출력 하기 때문.

## 5.2 AJAX를 활용해 답변 삭제 기능 구현

삭제할 시 valid가 어떤역할을 하는지 정확하게 알 것. 

`Result.java`의 boolean 타입의 valid를 역으로 설정하는 바람에 적용이 안되어 고생을 했다.

```javascript
$(document).on('click', '.link-delete-article', deleteAnswer);

function deleteAnswer(e) {
    e.preventDefault();
 
    let deleteBtn = $(this);			// 여기서 this는 event e를 지칭한다.
    let url = deleteBtn.attr("href");
    console.log("url : " + url);

    $.ajax({
        type : 'delete',
        url : url,
        dataType : 'json',
        error : function (xhr, status) {
            console.log("error");
        },								// 에러메시지 이벤트작동
        success : function (data, status) {
            console.log("data");
            if (data.valid) {
                deleteBtn.closest("article").remove();
            } else {
                alert(data.errorMessage);
            }
        }
    })
```

삭제구현시 해당 클래스 이름이 답변 삭제기능과 같아서 버그 발생. 클래스 이름을 구분지을 수 있도록 변경할 것.

## 5.3 질문 목록에 답변 수 보여주기 기능 추가

```sql
SELECT COUNT(*) FROM ANSWER WHERE QUESTION_ID = 1;	//count = 2
```

해당 기능을 실행하고 questioRepo에 다시 저장해야 적용이 된다.

Answer기능에만 Ajax를 활용한 XHR방식의 요청이라 새로고침을 진행해야 답변수 카운트가 측정된다.

삭제에 대한 카운트를 실시간으로 적용하려면 `AnswerController`에서 return data가 무엇인지 알아야한다.

data가 뭘 의미하는지 몰라서 고생했다.

```javascript
$.ajax({
        type: 'delete',
        url: url,
        dataType: 'json',
        error: function (xhr, status) {
            console.error("error");
        },
        success: function (data, status) {  //여기서 data는 question data를 의미한다.
            console.log(data);
            if (data.valid) {
                deleteBtn.closest("article").remove();
                $(".qna-comment-count strong").text(data.question.countOfAnswer);
            } else {
                alert(data.errorMessage);
            }
        }
    })

AnswerController.java
@DeleteMapping
~~~
return Result.ok(question);	// 중요!!
```



## 5.4 도메인 클래스에 대한 중복 제거 및 리팩토링

중복제거하면서 깨달은건 패키지를 각 역할(User, Question, Answer)로 하기 보단 도메인으로 나누어서
도메인(Controller, Domain(User, Answer, Question), Repository)로 나누는 것이 좋다. 
그 이유는 객체지향의 진실과 오해에서 읽었는데 기억이안남...

진행중 Question 만들때마다 CountOfAnswer을 지정해주지 않아 NPE(nullpoint에러)가 발생하는걸 깨달았다. 
NPE를 조심하자. 

## 5.5 Swagger 라이브러리를 통한  API 문서화 및 테스트

Swagger 빌드할 때 버전 꼭 확인하자! 버전이 통일되어야 빌드 에러가 나지 않는다.

```groovy
compile group: 'io.springfox', name: 'springfox-swagger2', version: '2.5.0'
compile group: 'io.springfox', name: 'springfox-swagger-ui', version: '2.5.0'
```

