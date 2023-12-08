package lk.cmh.web.cmh.auth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lk.cmh.web.cmh.cart.Cart;
import lk.cmh.web.cmh.user.*;
import lk.cmh.web.cmh.util.JwtTokenUtil;
import lk.cmh.web.cmh.wishlist.Wishlist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final FirebaseApp firebaseApp;

    public AuthRespDto login(AuthReqDto data) {
        return getToken(authenticate(data));
    }

    private UserDetails authenticate(AuthReqDto data) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.email(), data.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userRepository.findByEmail(authentication.getName()).map(UserDetailsImpl::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private AuthRespDto getToken(UserDetails userDetails) {
        String accessToken = null;
        String refreshToken = null;
        String firebaseToken = null;
        try {
            accessToken = jwtUtil.generateToken(userDetails, JwtTokenUtil.ACCESS_TOKEN_EXPIRATION_IN_DAYS);
            refreshToken = jwtUtil.generateToken(userDetails, JwtTokenUtil.REFRESH_TOKEN_EXPIRATION_IN_DAYS);

            String customToken = FirebaseAuth.getInstance(firebaseApp).createCustomToken(userDetails.getUsername());
            firebaseToken = customToken;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }

        return AuthRespDto.builder()
                .email(userDetails.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getExpiration(accessToken))
                .firebaseToken(firebaseToken)
                .role(userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .build();
    }

    public Map<String, String> register(RegisterReqDto data) {
        if(userRepository.existsByEmail(data.email()))
            throw new RuntimeException("Email already exists");
        User user = User.builder()
                .firstName(data.firstName())
                .lastName(data.lastName())
                .email(data.email())
                .password(passwordEncoder.encode(data.password()))
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        user.setCart(Cart.builder().user(user).build());
        user.setWishlist(Wishlist.builder().user(user).build());
        userRepository.save(user);

        return Map.of("message", "User registered successfully");
    }

    public AuthRespDto refreshToken(String refreshToken) {
        String email = jwtUtil.getSubject(refreshToken);
        UserDetails userDetails = userRepository.findByEmail(email).map(UserDetailsImpl::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return getToken(userDetails);
    }

    public AuthRespDto googleLogin(String idToken) {
        String email = null;
        String name = null;
        String picture = null;
        try {
            FirebaseToken ftoken = FirebaseAuth.getInstance(firebaseApp).verifyIdToken(idToken);
            email = ftoken.getEmail();
            name = ftoken.getName();
            picture = ftoken.getPicture();

        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
        Optional<UserDetails> userDetails = userRepository.findByEmail(email).map(UserDetailsImpl::new);
        if(userDetails.isPresent()) {
            return getToken(userDetails.get());
        }else {
            User user = User.builder()
                    .email(email)
                    .firstName(name)
                    .picture(picture)
                    .sub(email)
                    .role(UserRole.ROLE_CUSTOMER)
                    .build();

            user.setCart(Cart.builder().user(user).build());
            user.setWishlist(Wishlist.builder().user(user).build());
            User newUser = userRepository.save(user);

            return getToken(new UserDetailsImpl(newUser));
        }
    }
}
