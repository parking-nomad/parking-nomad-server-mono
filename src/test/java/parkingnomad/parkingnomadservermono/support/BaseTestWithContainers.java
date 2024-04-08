package parkingnomad.parkingnomadservermono.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

@SpringBootTest
@Testcontainers
public abstract class BaseTestWithContainers extends BaseTest {

    private static final String REDIS_IMAGE = "redis:6-alpine";
    private static final String MYSQL_IMAGE = "mysql:8.0";

    private static final GenericContainer<?> REDIS_CONTAINER;
    private static final MySQLContainer MYSQL_CONTAINER;


    @Autowired
    private DataInitializer dataInitializer;

    static {
        MYSQL_CONTAINER = (MySQLContainer) new MySQLContainer(MYSQL_IMAGE)
                .withDatabaseName("parking_nomad")
                .withEnv("MYSQL_ROOT_PASSWORD", "test");

        REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
                .withExposedPorts(6379)
                .withReuse(true);

        Startables.deepStart(REDIS_CONTAINER, MYSQL_CONTAINER).join();
    }

    @DynamicPropertySource
    static void overrideMySqlProps(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "root");
        registry.add("spring.datasource.password", () -> "test");
    }

    @DynamicPropertySource
    public static void overrideRedisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> String.valueOf(REDIS_CONTAINER.getMappedPort(6379)));
        registry.add("spring.data.redis.password", () -> "password");
    }

    @BeforeEach
    void setUp() {
        dataInitializer.deleteAll();
    }
}
