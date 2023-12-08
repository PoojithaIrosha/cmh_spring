package lk.cmh.web.cmh.user;

import lombok.Builder;

@Builder
public record UserDto(long id, String firstName, String lastName, String email, UserRole role, String sub,
                      String picture, UserAddress address) {
}
