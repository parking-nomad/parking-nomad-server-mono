package parkingnomad.parkingnomadservermono.acceptance.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import parkingnomad.parkingnomadservermono.acceptance.CucumberClient;
import parkingnomad.parkingnomadservermono.member.adaptor.in.web.dto.AccessTokenResponse;
import parkingnomad.parkingnomadservermono.member.domain.UserInfo;
import parkingnomad.parkingnomadservermono.member.domain.oauth.clients.OAuthClients;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthStep {

    public static final String REFRESH_TOKEN = "refresh_token";


    @Autowired
    CucumberClient client;

    @Autowired
    OAuthClients oAuthClients;

    @When("{string}를 통해 소셜 로그인을 한다.")
    public void socialLogin(final String provider) {
        final UserInfo userInfo = UserInfo.of("sub", provider, "dh");
        when(oAuthClients.getUserInfo(anyString(), anyString())).thenReturn(userInfo);

        final ExtractableResponse<Response> response = given().log().all()
                .queryParam("code", "code")
                .pathParam("provider", "kakao")
                .when()
                .get("/api/auth/{provider}")
                .then().log().all()
                .extract();

        final AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);
        client.setResponse(response);
        client.setRefreshToken(response.cookie(REFRESH_TOKEN));
        client.setAccessToken(accessTokenResponse.accessToken());
    }

    @When("새로운 토큰을 발급한다.")
    public void refresh() {
        final String refreshToken = client.getRefreshToken();
        final ExtractableResponse<Response> response = given().log().all()
                .cookie(REFRESH_TOKEN, refreshToken)
                .when()
                .get("api/auth/refresh")
                .then().log().all()
                .extract();
        client.setResponse(response);
    }

    @When("로그아웃을 한다.")
    public void logout() {
        final String refreshToken = client.getRefreshToken();
        final ExtractableResponse<Response> response = given().log().all()
                .cookie(REFRESH_TOKEN, refreshToken)
                .when()
                .get("api/auth/logout")
                .then().log().all()
                .extract();
        client.setResponse(response);
    }

    @Then("refreshToken은 쿠키에 저장하고, access token을 포함하여 200을 응답한다.")
    public void checkSocialLoginResult() {
        final ExtractableResponse<Response> response = client.getResponse();
        final AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softAssertions.assertThat(response.cookie(REFRESH_TOKEN)).isNotNull();
            softAssertions.assertThat(accessTokenResponse.accessToken()).isNotNull();
        });
    }

    @Then("cookie에 저장된 refresh token을 공백문자로 변경한다.")
    public void checkLogoutResult() {
        final ExtractableResponse<Response> response = client.getResponse();

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softAssertions.assertThat(response.cookie(REFRESH_TOKEN)).isEqualTo("");
        });
    }
}
