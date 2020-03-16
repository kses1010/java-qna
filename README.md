# step5. XHR과 AJAX기반 개발

## comment 사항

- [ ] 

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



## 5.3 질문 목록에 답변 수 보여주기 기능 추가



## 5.4 도메인 클래스에 대한 중복 제거 및 리팩토링



## 5.5 Swagger 라이브러리를 통한  API 문서화 및 테스트