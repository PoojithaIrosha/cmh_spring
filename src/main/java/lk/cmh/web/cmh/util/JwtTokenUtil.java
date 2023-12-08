package lk.cmh.web.cmh.util;

import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSASigner;
import io.fusionauth.jwt.rsa.RSAVerifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    private static final String ISSUER = "www.cmh.lk";
    public static final long ACCESS_TOKEN_EXPIRATION_IN_DAYS = 1;
    public static final long REFRESH_TOKEN_EXPIRATION_IN_DAYS = 30;
    private static final String FIREBASE_SERVICE_ACCOUNT = "firebase-adminsdk-g0qx3@ceylonmarkethub.iam.gserviceaccount.com";
    private static final String FIREBASE_TOKEN_AUDIENCE = "https://identitytoolkit.googleapis.com/google.identity.identitytoolkit.v1.IdentityToolkit";

    // Firebase Token Signer
    private Signer getFirebaseTokenSigner() {
        try {
            return RSASigner.newSHA256Signer(new String(Files.readAllBytes(Paths.get("firebase_private_key.pem"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Access Token Signer
    private Signer getTokenSigner() {
        try {
            return RSASigner.newSHA256Signer(new String(Files.readAllBytes(Paths.get("private.key"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Access Token Verifier
    private Verifier getTokenVerifier() {
        return RSAVerifier.newVerifier(Paths.get("public.key"));
    }

    // Retrieve subject from the token
    public String getSubject(String token) throws JWTExpiredException {
        return JWT.getDecoder().decode(token, getTokenVerifier()).subject;
    }

    // Generate Access Token
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims, Long expirationInDays) throws IOException {
        JWT jwt = new JWT()
                .setIssuer(ISSUER)
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject(userDetails.getUsername())
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusDays(expirationInDays))
                .addClaim("role", userDetails.getAuthorities().stream().findFirst().get().getAuthority());

        extraClaims.forEach(jwt::addClaim);
        return JWT.getEncoder().encode(jwt, getTokenSigner());
    }

    // Generate Firebase Custom Token
    public String generateFirebaseToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        JWT jwt = new JWT()
                .setIssuer(FIREBASE_SERVICE_ACCOUNT)
                .setAudience(FIREBASE_TOKEN_AUDIENCE)
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject(FIREBASE_SERVICE_ACCOUNT)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusDays(ACCESS_TOKEN_EXPIRATION_IN_DAYS))
                .addClaim("role", userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .addClaim("uid", UUID.randomUUID().toString());

        extraClaims.forEach(jwt::addClaim);
        return JWT.getEncoder().encode(jwt, getFirebaseTokenSigner());
    }

    public String generateToken(UserDetails userDetails, Long expirationInDays) throws IOException {
        return generateToken(userDetails, Map.of(), expirationInDays);
    }

    public String generateFirebaseToken(UserDetails userDetails) {
        return generateFirebaseToken(userDetails, Map.of());
    }

    // Get Token Expiration
    public ZonedDateTime getExpiration(String token) {
        return JWT.getDecoder().decode(token, getTokenVerifier()).expiration;
    }

    // Check for token validity
    public boolean isTokenValid(String token, UserDetails userDetails) {
        JWT decode = JWT.getDecoder().decode(token, getTokenVerifier());
        return userDetails.getUsername().equals(getSubject(token)) && !decode.isExpired();
    }
}
