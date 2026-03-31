package com.vannchhai.smart_salary_api.security;

import com.vannchhai.smart_salary_api.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtProperties jwtProperties;
  private PrivateKey privateKey;
  private PublicKey publicKey;

  @PostConstruct
  public void initKeys() throws Exception {
    String privateKeyStr = System.getenv("JWT_PRIVATE_KEY");
    String publicKeyStr = System.getenv("JWT_PUBLIC_KEY");

    if (privateKeyStr != null
        && publicKeyStr != null
        && !privateKeyStr.isBlank()
        && !publicKeyStr.isBlank()) {
      this.privateKey = loadPrivateKeyFromString(privateKeyStr);
      this.publicKey = loadPublicKeyFromString(publicKeyStr);
    } else {
      this.privateKey = loadPrivateKeyFromResource("/keys/private.pem");
      this.publicKey = loadPublicKeyFromResource("/keys/public.pem");
    }
  }

  private PrivateKey loadPrivateKeyFromString(String key) throws Exception {
    String privateKeyPEM =
        key.replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
    byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
    return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
  }

  private PublicKey loadPublicKeyFromString(String key) throws Exception {
    String publicKeyPEM =
        key.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
    byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
    return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
  }

  private PrivateKey loadPrivateKeyFromResource(String path) throws Exception {
    URL privateKeyUrl = getClass().getResource(path);
    if (privateKeyUrl == null) {
      throw new IllegalStateException("Private key file missing in resources/keys/");
    }
    String key = Files.readString(Path.of(privateKeyUrl.toURI()), StandardCharsets.UTF_8);
    return loadPrivateKeyFromString(key);
  }

  private PublicKey loadPublicKeyFromResource(String path) throws Exception {
    URL publicKeyUrl = getClass().getResource(path);
    if (publicKeyUrl == null) {
      throw new IllegalStateException("Public key file missing in resources/keys/");
    }
    String key = Files.readString(Path.of(publicKeyUrl.toURI()), StandardCharsets.UTF_8);
    return loadPublicKeyFromString(key);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
        .signWith(privateKey, Jwts.SIG.RS256)
        .compact();
  }

  public String generateRefreshToken(UserDetails user) {
    return Jwts.builder()
        .subject(user.getUsername())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
        .signWith(privateKey, Jwts.SIG.RS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> resolver) {
    final Claims claims = extractAllClaims(token);
    return resolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  public boolean isRefreshTokenValid(String token) {
    try {
      Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
      return !isTokenExpired(token);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
