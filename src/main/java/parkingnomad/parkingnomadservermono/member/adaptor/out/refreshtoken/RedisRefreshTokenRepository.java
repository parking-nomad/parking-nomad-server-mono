package parkingnomad.parkingnomadservermono.member.adaptor.out.refreshtoken;

import org.springframework.data.repository.CrudRepository;

public interface RedisRefreshTokenRepository extends CrudRepository<RedisRefreshTokenEntity, String> {
}
