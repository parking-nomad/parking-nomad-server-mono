package parkingnomad.parkingnomadservermono.acceptance;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import parkingnomad.parkingnomadservermono.member.domain.oauth.clients.OAuthClients;
import parkingnomad.parkingnomadservermono.parking.application.port.out.ImageUploader;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;
import parkingnomad.parkingnomadservermono.support.DataInitializer;

@ActiveProfiles("test")
@Import({CucumberClient.class})
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration extends BaseTestWithContainers {

    @LocalServerPort
    private int port;

    @Autowired
    DataInitializer dataInitializer;

    @MockBean
    OAuthClients oAuthClients;

    @MockBean
    ImageUploader imageUploader;

    @Before
    public void before() {
        RestAssured.port = port;
        dataInitializer.deleteAll();
    }
}
