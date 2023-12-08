package lk.cmh.web.cmh.auth;

import lk.cmh.web.cmh.user.UserRole;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record AuthRespDto(String email, String accessToken, String refreshToken, String firebaseToken, ZonedDateTime expiresIn, String role) {
}
