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

    URL privateKeyUrl = getClass().getResource("/keys/private.pem");
    if (privateKeyUrl == null) {
      throw new IllegalStateException("Private key file missing in resources/keys/");
    }

    String privateKeyContent =
        Files.readString(Path.of(privateKeyUrl.toURI()), StandardCharsets.UTF_8)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

    PKCS8EncodedKeySpec privateSpec =
        new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
    this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(privateSpec);

    URL publicKeyUrl = getClass().getResource("/keys/public.pem");
    if (publicKeyUrl == null) {
      throw new IllegalStateException("Public key file missing in resources/keys/");
    }

    String publicKeyContent =
        Files.readString(Path.of(publicKeyUrl.toURI()), StandardCharsets.UTF_8)
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");

    X509EncodedKeySpec publicSpec =
        new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
    this.publicKey = KeyFactory.getInstance("RSA").generatePublic(publicSpec);
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
