package parkingnomad.parkingnomadservermono.acceptance;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.context.TestComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@TestComponent
@ScenarioScope
public class CucumberClient {
    private String accessToken;
    private String refreshToken;
    private Map<String, Long> dataStorage = new HashMap<>();
    private ExtractableResponse<Response> response;

    public CucumberClient() {
    }

    public void addData(final String key, final Long value) {
        dataStorage.put(key, value);
    }

    public Long getData(final String key) {
        final Long id = dataStorage.get(key);
        if (Objects.isNull(id)) {
            return 0L;
        }
        return id;
    }

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public void setResponse(final ExtractableResponse<Response> response) {
        this.response = response;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
