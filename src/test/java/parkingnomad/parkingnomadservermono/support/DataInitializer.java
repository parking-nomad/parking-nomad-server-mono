package parkingnomad.parkingnomadservermono.support;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@Component
@Profile("test")
public class DataInitializer {
    private static final int OFF = 0;
    private static final int ON = 1;
    private static final String TRUNCATE = "TRUNCATE ";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final List<String> tableNames = new ArrayList<>();

    @BeforeEach
    @Transactional(REQUIRES_NEW)
    public void deleteAll() {
        if (tableNames.isEmpty()) {
            init();
        }

        setForeignKeyEnabled(OFF);
        truncateAllTables();
        setForeignKeyEnabled(ON);
        redisDeleteAll();
    }

    private void redisDeleteAll() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    private void setForeignKeyEnabled(final int enabled) {
        em.createNativeQuery("SET foreign_key_checks = " + enabled).executeUpdate();
    }

    private void truncateAllTables() {
        tableNames.stream()
                .map(tableName -> em.createNativeQuery(TRUNCATE + tableName))
                .forEach(Query::executeUpdate);
    }

    private void init() {
        final List<String> tableNames = em.createNativeQuery("SHOW TABLES ").getResultList();
        this.tableNames.addAll(tableNames);
    }
}
