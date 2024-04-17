package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private RestTemplate restTemplate;

  private SessionRepository<? extends Session> sessionRepository;

  public ResponseEntity<?> fetchAuthorizationEndpoint() {
    String url = "https://auth.current.bankid.no/auth/realms/current/.well-known/openid-configuration";
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(response.getBody());
      JsonNode authorizationRequestNode = rootNode.path("authorization_endpoint");

      if (authorizationRequestNode.isMissingNode()) {
        return ResponseEntity.badRequest().body("Authorization endpoint not found in the response");
      } else {
        return ResponseEntity.ok(authorizationRequestNode.asText());
      }
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error processing JSON response: " + e.getMessage());
    }
  }

  @GetMapping("/authorization-request")
  public ResponseEntity<?> generateAuthDetails() {

    // Authorization Endpoint
    ResponseEntity<?> fetchedResponse = fetchAuthorizationEndpoint();
    String authorizationEndpoint = (String) fetchedResponse.getBody();
    System.out.println("Authorization endpoint: " + authorizationEndpoint);

    //Session session = sessionRepository.createSession();

    String state = UUID.randomUUID().toString();
    System.out.println("State: " + state);

    String nonce = UUID.randomUUID().toString();
    System.out.println("Nonce: " + nonce);

    String codeVerifier = KeyGenerators.string().generateKey();
    System.out.println("codeVerifier: " + codeVerifier);

    String codeChallenge;

    /*

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
      codeChallenge = Base64.getUrlEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      return ResponseEntity.internalServerError().body("Error generating code challenge");
    }

    session.setAttribute("state", state);
    session.setAttribute("nonce", nonce);
    session.setAttribute("codeVerifier", codeVerifier);
    sessionRepository.save(session);

    return ResponseEntity.ok(new AuthDetails(state, nonce, codeChallenge, "S256"));

     */

    return ResponseEntity.ok("OK");
  }
}

