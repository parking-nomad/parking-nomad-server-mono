Feature: Member 인수테스트
  Background:
    When "kakao"를 통해 소셜 로그인을 한다.

  Scenario: 회원탈퇴를 진행한다.
    Given 위도 37.0789561558879 경도 127.423084873712의 주차정보를 저장한다.
    Given 위도 35.17977 경도 129.07504의 주차정보를 저장한다.
    When 회원탈퇴를 한다.
    Then 204을 응답한다.
    Then 탈퇴된 회원의 accessToken은 사용할 수 없다.
    Then 탈퇴된 회원의 주차기록은 삭제된다.
    Then 탈퇴된 회원의 최근 주차기록은 삭제된다.
