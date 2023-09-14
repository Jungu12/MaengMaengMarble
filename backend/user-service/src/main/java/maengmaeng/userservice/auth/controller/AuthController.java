package maengmaeng.userservice.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.auth.dto.AuthAccessTokenResponse;
import maengmaeng.userservice.auth.dto.CodeDto;
import maengmaeng.userservice.auth.dto.OAuthToken;
import maengmaeng.userservice.auth.service.AuthService;
import maengmaeng.userservice.auth.util.CookieManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service/auth")
public class AuthController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final AuthService authService;

	/**
	 * 인가 코드를 받아 사용자 여부를 확인
	 * 사용자가 아닌 경우, DB에 email을 id로 저장
	 * 이후 github_id를 가지고 JWT 생성 후 반환
	 * Access Token : AccessTokenResponse
	 * Refresh Token : httpOnly Cookie
	 * */
	@PostMapping("/naver")
	public ResponseEntity<?> getCode(@RequestBody CodeDto codeDto, HttpServletResponse response) {
		logger.debug("getCode(), code = {}", codeDto.getCode());

		OAuthToken oAuthToken = authService.createTokens(codeDto);

		response.addHeader("Set-Cookie", CookieManager.createCookie(oAuthToken.getRefreshToken()).toString());

		return ResponseEntity.ok().body(new AuthAccessTokenResponse(oAuthToken.getAccessToken()));
	}

	/**
	 * 사용자의 Refresh Token을 받아 OAuthToken 재발급
	 * 유효한 경우, 재발급
	 * Access Token : AccessTokenResponse
	 * Refresh Token : httpOnly Cookie
	 * */
	@GetMapping("/token")
	public ResponseEntity<?> getToken(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getToken(), Token 재발급");

		OAuthToken oAuthToken = authService.reGenerateAuthToken(request);

		if (oAuthToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		response.addHeader("Set-Cookie", CookieManager.createCookie(oAuthToken.getRefreshToken()).toString());

		return ResponseEntity.ok().body(new AuthAccessTokenResponse(oAuthToken.getAccessToken()));
	}

	@GetMapping("/me")
	public ResponseEntity<?> getUserId(@CookieValue(value = "refreshToken") String token) {
		logger.debug("getUserId()");
		return ResponseEntity.ok().body(authService.getUserId(token));
	}
}
