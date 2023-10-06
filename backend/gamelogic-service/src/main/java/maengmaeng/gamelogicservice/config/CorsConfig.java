package maengmaeng.gamelogicservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowCredentials(true)
			.allowedOriginPatterns("*") // 모든 호스트 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드 지정
			.allowedHeaders("*"); // 허용할 요청 헤더 지정
	}
}