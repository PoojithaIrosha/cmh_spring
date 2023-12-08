package lk.cmh.web.cmh.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthRespDto> login(@RequestBody AuthReqDto data) {
        return ResponseEntity.ok(authService.login(data));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> mRegister(@RequestBody RegisterReqDto data) {
        return ResponseEntity.ok(authService.register(data));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthRespDto> refreshToken(@RequestBody Map<String, String> data) {
        return ResponseEntity.ok(authService.refreshToken(data.get("refreshToken")));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthRespDto> googleLogin(@RequestBody Map<String, String> data) {
        return ResponseEntity.ok(authService.googleLogin(data.get("idToken")));
    }
}
