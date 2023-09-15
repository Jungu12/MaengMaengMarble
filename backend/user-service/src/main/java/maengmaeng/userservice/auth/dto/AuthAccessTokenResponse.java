package maengmaeng.userservice.auth.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthAccessTokenResponse {
	@NotBlank
	private String accessToken;
}
