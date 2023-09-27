package maengmaeng.gamelogicservice.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import redis.embedded.RedisServer;

@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {
	@Value("localhost")
	private String redisHost;

	@Value("6379")
	private int redisPort;

	// @Value("${spring.redis.password}")
	// private String password;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
		redisConfiguration.setHostName(redisHost);
		redisConfiguration.setPort(redisPort);
		// redisConfiguration.setPassword(password);
		return new LettuceConnectionFactory(redisConfiguration);
	}

	// private RedisServer redisServer;
	//
	// @PostConstruct
	// public void redisServer() throws IOException {
	// 	redisServer = new RedisServer(6379);
	// 	redisServer.start();
	// }
	//
	// @PreDestroy
	// public void stopRedis() {
	// 	if (redisServer != null) {
	// 		redisServer.stop();
	// 	}
	// }

}
