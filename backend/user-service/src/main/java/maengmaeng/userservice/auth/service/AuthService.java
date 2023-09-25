package maengmaeng.userservice.auth.service;

import javax.servlet.http.HttpServletRequest;

import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.UserAvatar;
import maengmaeng.userservice.user.repository.AvatarRepository;
import maengmaeng.userservice.user.repository.UserRepository;
import maengmaeng.userservice.user.repository.UserAvatarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import maengmaeng.userservice.auth.dto.CodeDto;
import maengmaeng.userservice.auth.dto.OAuthToken;
import maengmaeng.userservice.auth.dto.TokenInfoResponse;
import maengmaeng.userservice.auth.util.JwtProvider;
import maengmaeng.userservice.auth.util.JwtRedisManager;
import maengmaeng.userservice.exception.AuthException;
import maengmaeng.userservice.exception.ExceptionCode;

@Service
public class AuthService {
	private final RestTemplate restTemplate;

	private final String clientId;

	private final String clientSecret;

	private final String accessTokenUrl;

	private final String userInfoUrl;

	private final UserRepository userRepository;

	private final UserAvatarRepository userAvatarRepository;

	private final AvatarRepository avatarRepository;

	private final JwtRedisManager jwtRedisManager;

	private final JwtProvider jwtProvider;

	public AuthService(RestTemplate restTemplate,
		@Value("${spring.security.oauth2.client.registration.naver.client-id}") String clientId,
		@Value("${spring.security.oauth2.client.registration.naver.client-secret}") String clientSecret,
		@Value("${spring.security.oauth2.client.provider.naver.token-uri}") String accessTokenUrl,
		@Value("${spring.security.oauth2.client.provider.naver.user-info-uri}") String userInfoUrl, UserRepository userRepository,
					   UserAvatarRepository userAvatarRepository, AvatarRepository avatarRepository, JwtProvider jwtProvider,
		JwtRedisManager jwtRedisManager) {
		this.restTemplate = restTemplate;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.accessTokenUrl = accessTokenUrl;
		this.userInfoUrl = userInfoUrl;
		this.userRepository = userRepository;
		this.userAvatarRepository = userAvatarRepository;
		this.avatarRepository = avatarRepository;
		this.jwtProvider = jwtProvider;
		this.jwtRedisManager = jwtRedisManager;
	}

	/**
	 * 인가 코드를 받아 액세스 토큰을 발급 받고, 이를 통해 사용자의 정보를 조회해 JWT 생성 및 반환
	 * @param codeDto 인가 코드
	 * @return Map Access Token, Refresh Token
	 * */
	public OAuthToken createTokens(CodeDto codeDto) {
		String accessToken = getAccessToken(codeDto);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		headers.set("Accept", "application/json");

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
			userInfoUrl,
			HttpMethod.GET,
			requestEntity,
			String.class
		);

		User user = extractUser(response.getBody());

		String userId = user.getUserId();
		// 사용자가 처음 서비스를 이용하는 경우
		if (userRepository.findUserByUserId(userId).isEmpty()) {
			userRepository.save(user);
		}

		addUserAvatar(userId);

		//JWT 생성 하고, Redis 저장소에 Token 저장하는 로직 추가 필요 (확인 필요!)
		OAuthToken oAuthToken = new OAuthToken(jwtProvider.generateAccessToken(userId),
			jwtProvider.generateRefreshToken(userId));
		jwtRedisManager.storeJwt(userId, oAuthToken.getRefreshToken());

		return oAuthToken;
	}

	/**
	 * 토큰으로부터 사용자 ID 추출 후 반환
	 * */
	public TokenInfoResponse getUserId(String token) {
		return new TokenInfoResponse(jwtProvider.getUserId(token));
	}

	/**
	 * Refresh Token을 받아 유효성을 검사하고, 유효한 경우는 Access Token과 Refresh Token을 재발급
	 */
	public OAuthToken reGenerateAuthToken(HttpServletRequest request) {
		String refreshToken = jwtProvider.resolveToken(request);

		if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
			return null;
		}

		String id = jwtProvider.getUserId(refreshToken);

		return new OAuthToken(jwtProvider.generateAccessToken(id), jwtProvider.generateRefreshToken(id));
	}

	/**
	 * 인가 코드를 통해 Naver로부터 액세스 토큰을 발급받아 반환
	 * @param codeDto 인가 코드, state
	 * @return String 액세스 토큰
	 * */
	private String getAccessToken(CodeDto codeDto) {
		return extractAccessToken(requestAccessToken(codeDto).getBody());
	}

	/**
	 * 주어진 정보로부터 User 객체를 생성해 반환
	 * @param data
	 * @return User
	 * */
	private User extractUser(String data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(data);

			String username = jsonNode.get("response").get("email").asText().split("@")[0];

			User user = new User(username, username);

			return user;
		} catch (Exception e) {
			throw new AuthException(ExceptionCode.USER_CREATED_FAILED);
		}
	}

	private void addUserAvatar(String userId) {
		try {
			User user = userRepository.findById(userId).orElse(null); // 기존 코드 수정
			Avatar avatar = avatarRepository.findById(1).orElse(null); // 기존 코드 수정

			if (user != null && avatar != null) {
				// 이미 존재하는 사용자와 아바타의 조합인지 확인
				UserAvatar existingUserAvatar = userAvatarRepository.findByUserAndAvatar(user, avatar);

				if (existingUserAvatar == null) {
					// 조합이 존재하지 않으면 새로운 UserAvatar 생성
					UserAvatar userAvatar = new UserAvatar(user, avatar, true);
					userAvatarRepository.save(userAvatar);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 주어진 응답으로부터 Access Token을 추출해 반환
	 * @param response 데이터
	 * @return String Access Token 또는 null
	 * */
	private String extractAccessToken(String response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response);
			return jsonNode.get("access_token").asText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthException(ExceptionCode.NAVER_TOKEN_RESPONSE_FAILED);
		}
	}

	private ResponseEntity<String> requestAccessToken(CodeDto codeDto) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/json");

			HttpEntity<String> requestEntity = new HttpEntity<>(headers);
			return restTemplate.exchange(
				accessTokenUrl + "?grant_type=authorization_code" +"&client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + codeDto.getCode() + "&state=" + codeDto.getState(),
				HttpMethod.POST,
				requestEntity,
				String.class
			);
		} catch (Exception e) {
			throw new AuthException(ExceptionCode.NAVER_ACCESS_TOKEN_FETCH_FAILED);
		}
	}
}