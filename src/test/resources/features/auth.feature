Feature: Auth 인수테스트

  Scenario: kakao 소셜로그인을 한다.
    When "kakao"를 통해 소셜 로그인을 한다.
    Then refreshToken은 쿠키에 저장하고, access token을 포함하여 200을 응답한다.

  Scenario: refreshToken을 통해 새로운 accessToken과 refreshToken을 발급한다.
    Given "kakao"를 통해 소셜 로그인을 한다.
    When 새로운 토큰을 발급한다.
    Then refreshToken은 쿠키에 저장하고, access token을 포함하여 200을 응답한다.

  Scenario: 로그아웃을 한다.
    Given "kakao"를 통해 소셜 로그인을 한다.
    When 로그아웃을 한다.
    Then cookie에 저장된 refresh token을 공백문자로 변경한다.
