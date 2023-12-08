package lk.cmh.web.cmh.auth;


import lombok.Builder;

@Builder
public record AuthReqDto(String email, String password) {
}
