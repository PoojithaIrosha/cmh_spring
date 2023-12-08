package lk.cmh.web.cmh.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserAddress updateAddress(UserDetailsImpl userDetails, UserAddress address) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAddress(address);
        userRepository.save(user);
        return user.getAddress();
    }

    public void makeASeller(UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(UserRole.ROLE_SELLER);
        userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDto updateProfile(UserDetailsImpl userDetails, UserProfileDto url) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPicture(url.picture());
        userRepository.save(user);
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).role(user.getRole()).sub(user.getSub()).picture(user.getPicture()).address(user.getAddress()).build();
    }

    public UserDto updateUser(UserDetailsImpl userDetails, UpdateUserDto updateUserDto) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(updateUserDto.firstName());
        user.setLastName(updateUserDto.lastName());
        userRepository.save(user);
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).role(user.getRole()).sub(user.getSub()).picture(user.getPicture()).address(user.getAddress()).build();
    }
}
