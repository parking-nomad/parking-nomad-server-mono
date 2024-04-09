package parkingnomad.parkingnomadservermono.acceptance.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.acceptance.CucumberClient;
import parkingnomad.parkingnomadservermono.common.dto.ErrorResponse;
import parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.latestparking.RedisLatestParkingEntity;
import parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.latestparking.RedisLatestParkingRepositoryAdaptor;
import parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking.JpaParkingEntity;
import parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking.JpaParkingRepositoryAdaptor;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class MemberSteps {

    @Autowired
    CucumberClient client;

    @Autowired
    JpaParkingRepositoryAdaptor parkings;

    @Autowired
    RedisLatestParkingRepositoryAdaptor latestParkings;

    @When("회원탈퇴를 한다.")
    public void resign() {
        final String accessToken = client.getAccessToken();

        final ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("api/members")
                .then().log().all()
                .extract();

        client.setResponse(response);
    }

    @Then("탈퇴된 회원의 accessToken은 사용할 수 없다.")
    public void asdf() {
        final String accessToken = client.getAccessToken();

        final ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .when().get("api/parkings/latest")
                .then().log().all()
                .extract();

        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(errorResponse.code()).isEqualTo("INTERNAL_SERVER_ERROR");
            softAssertions.assertThat(errorResponse.message()).isEqualTo("유효하지 않은 access_token 입니다.");
        });
    }

    @Then("탈퇴된 회원의 주차기록은 삭제된다.")
    public void checkDeleteParking() {
        final Long parking1 = client.getData("parking1");
        final Long parking2 = client.getData("parking2");

        final Optional<JpaParkingEntity> foundParking1 = parkings.findById(parking1);
        final Optional<JpaParkingEntity> foundParking2 = parkings.findById(parking2);

        assertSoftly(softAssertions -> {
            assertThat(foundParking1).isEmpty();
            assertThat(foundParking2).isEmpty();
        });
    }

    @Then("탈퇴된 회원의 최근 주차기록은 삭제된다.")
    public void checkDeleteLatestParking() {
        final Long member = client.getData("member");
        final Optional<RedisLatestParkingEntity> foundLatestParking = latestParkings.findById(member);

        assertThat(foundLatestParking).isEmpty();
    }
}
