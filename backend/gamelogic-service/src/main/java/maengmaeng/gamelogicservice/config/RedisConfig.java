package maengmaeng.gamelogicservice.config;

import maengmaeng.gamelogicservice.util.RedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	// redis pub/sub 메시지를 처리하는 listener 설정
	@Bean
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory, RedisSubscriber subscriber, ChannelTopic waitingroomTopic) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// 대기방 토픽을 RedisSubscriber에 등록
		container.addMessageListener(subscriber,waitingroomTopic);
		return container;
	}

	// 어플리케이션에서 사용할 redisTemplate 설정
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		//Redis 서버에는 bytes 코드만이 저장되므로 key와 value에 Serializer를 설정해야한다.
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		//Json 포맷 형식으로 메세지를 교환하기 위해 인자로 JackSon2JsonRedisSerializer 설정
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
		return redisTemplate;
	}

	/*
	공통으로 쓸 수 있는 대기방 토픽 생성
	 */
	@Bean
	public ChannelTopic waitingroomTopic(){
		return new ChannelTopic("WAITINGROOM");
	}
}

