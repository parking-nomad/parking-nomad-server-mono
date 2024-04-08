package parkingnomad.parkingnomadservermono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableAsync
public class ParkingNomadServerMonoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingNomadServerMonoApplication.class, args);
    }

}
