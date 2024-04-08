package parkingnomad.parkingnomadservermono.acceptance.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.acceptance.CucumberClient;
import parkingnomad.parkingnomadservermono.parking.MockParkingImageFile;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.PageResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.SaveParkingRequest;
import parkingnomad.parkingnomadservermono.parking.application.port.out.ImageUploader;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ParkingSteps {

    @Autowired
    CucumberClient client;

    @Autowired
    ImageUploader imageUploader;

    @When("위도 {double} 경도 {double}의 주차정보를 저장한다.")
    public void saveParking(double latitude, double longitude) throws InterruptedException {
        when(imageUploader.upload(any())).thenReturn("saved image name");
        final String accessToken = client.getAccessToken();

        final SaveParkingRequest request = new SaveParkingRequest(latitude, longitude);

        final ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .multiPart("parkingImage", MockParkingImageFile.FILE, "image/webp")
                .multiPart("saveParkingRequest", request, "application/json")
                .when().post("api/parkings")
                .then().log().all()
                .extract();

        client.setResponse(response);
        final String location = response.header("Location");
        if (!Strings.isBlank(location)) {
            final String[] locations = location.split("/");
            final long productId = Long.parseLong(locations[locations.length - 1]);
            client.addData("parking" + productId, productId);
        }
        Thread.sleep(500);
    }

    @When("{string}을 조회한다.")
    public void findById(String parking) {
        final Long parkingId = client.getData(parking);
        final String accessToken = client.getAccessToken();

        final ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .pathParam("parkingId", parkingId)
                .when().get("api/parkings/{parkingId}")
                .then().log().all()
                .extract();

        client.setResponse(response);
    }

    @When("가장 최근에 저장된 주차정보를 조회한다.")
    public void findLatestParking() {
        final String accessToken = client.getAccessToken();

        final ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .when().get("api/parkings/latest")
                .then().log().all()
                .extract();

        client.setResponse(response);
    }

    @Then("조회 된 parking의 id는 {string}의 id와 동일하고, 주소는 {string}이다.")
    public void checkFindParkingResult(String parking, String address) {
        final Long expectedId = client.getData(parking);

        final ExtractableResponse<Response> response = client.getResponse();
        final ParkingResponse parkingResponse = response.as(ParkingResponse.class);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(parkingResponse.id()).isEqualTo(expectedId);
            softAssertions.assertThat(parkingResponse.address()).isEqualTo(address);
        });
    }

    @When("{string}을 삭제한다.")
    public void deleteParking(String parking) {
        final Long parkingId = client.getData(parking);
        final String accessToken = client.getAccessToken();

        final ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .pathParam("parkingId", parkingId)
                .when().delete("api/parkings/{parkingId}")
                .then().log().all()
                .extract();

        client.setResponse(response);
    }

    @When("주차정보를 반환한다. 페이지 사이즈 = {int}, 페이지 넘버 = {int}")
    public void findParkingsByMemberIdAndPage(int size, int page) {
        final String accessToken = client.getAccessToken();

        final ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .queryParam("page", page)
                .queryParam("size", size)
                .when().get("api/parkings")
                .then().log().all()
                .extract();

        client.setResponse(response);
    }

    @Then("{int}을 응답한다.")
    public void checkStatusCode(int statusCode) {
        final ExtractableResponse<Response> response = client.getResponse();

        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @Then("응답은 아래의 주차정보를 포함한다.")
    public void checkFindParkingPageableResponse(DataTable dataTable) {
        final List<String> keys = dataTable.asList(String.class);
        final List<Long> parkingIds = keys.stream()
                .map(client::getData)
                .toList();

        final ExtractableResponse<Response> response = client.getResponse();
        final JsonPath jsonPath = response.jsonPath();
        final List<Long> resultIds = jsonPath.getList("contents.id", Long.class);

        assertThat(resultIds).containsExactlyElementsOf(parkingIds);
    }

    @ParameterType(value = "true|True|TRUE|false|False|FALSE")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

    @Then("hasNext는 {booleanValue} 이다.")
    public void checkHasNext(final boolean expectedHasNext) {
        final PageResponse pageResponse = client.getResponse().as(PageResponse.class);

        assertThat(pageResponse.getHasNext()).isEqualTo(expectedHasNext);
    }
}
