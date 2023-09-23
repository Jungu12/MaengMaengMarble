package maengmaeng.gamelogicservice.config;

import maengmaeng.gamelogicservice.util.RedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration

public class RedisConfig {

	@Bean
	public ChannelTopic lobbyTopic() {
		return new ChannelTopic("LOBBY");
	}

	@Bean
	public ChannelTopic waitingRoomTopic() {
		return new ChannelTopic("WAITING_ROOM");
	}

	@Bean
	public ChannelTopic gameRoomTopic() {
		return new ChannelTopic("GAME_ROOM");
	}

	@Bean
	public ChannelTopic chatTopic() {
		return new ChannelTopic("CHAT");
	}

	// redis pub/sub 메시지를 처리하는 listener 설정
	@Bean
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory, RedisSubscriber subscriber, ChannelTopic lobbyTopic, ChannelTopic waitingRoomTopic, ChannelTopic gameRoomTopic, ChannelTopic chatTopic) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		//Topic Listener에 설정
		container.addMessageListener(subscriber, lobbyTopic);
		container.addMessageListener(subscriber, waitingRoomTopic);
		container.addMessageListener(subscriber, gameRoomTopic);
		container.addMessageListener(subscriber, chatTopic);
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
}

