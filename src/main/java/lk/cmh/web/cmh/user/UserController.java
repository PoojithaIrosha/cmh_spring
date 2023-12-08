package lk.cmh.web.cmh.user;

import lk.cmh.web.cmh.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("{email}")
    public User getUser(@PathVariable("email") String email) {
        User user = userService.getUser(email);
        return user;
    }

    @GetMapping("/seller/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    public UserDto getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(email);
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).role(user.getRole()).sub(user.getSub()).picture(user.getPicture()).address(user.getAddress()).build();
    }

    @GetMapping("/me/address")
    public UserAddress getAddress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(email);
        return user.getAddress() != null ? user.getAddress() : new UserAddress();
    }

    @PostMapping("/me/address")
    public UserAddress updateAddress(@RequestBody UserAddress address, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateAddress(userDetails, address);
    }

    @PutMapping("/me/become-seller")
    public Map<String, String> makeASeller(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.makeASeller(userDetails);
        return Map.of("message", "success");
    }

    @PutMapping("/me/update-profile")
    public UserDto updateProfile(@RequestBody UserProfileDto userProfileDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateProfile(userDetails, userProfileDto);
    }

    @PutMapping("/me/update-user")
    public UserDto updateUser(@RequestBody UpdateUserDto updateUserDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUser(userDetails, updateUserDto);
    }
}
