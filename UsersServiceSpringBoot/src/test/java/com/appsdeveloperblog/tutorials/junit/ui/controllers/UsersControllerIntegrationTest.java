package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.security.SecurityConstants;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
                properties = "server.port=8081")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersControllerIntegrationTest {

    private TestRestTemplate testRestTemplate;

    private String authToken;

    @BeforeEach
    void setUp() {
        testRestTemplate = new TestRestTemplate();
    }

    @Test
    @Order(1)
    @DisplayName("User can be created")
    void testCreateUser_whenValidDetailsProvided_returnsUserDetails() throws JSONException {

        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Katy");
        userDetailsRequestJson.put("lastName", "Maro");
        userDetailsRequestJson.put("email", "test@test.com");
        userDetailsRequestJson.put("password", "12345678");
        userDetailsRequestJson.put("repeatPassword", "12345678");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(userDetailsRequestJson.toString(), httpHeaders);

        ResponseEntity<UserRest> createdUserDetails = testRestTemplate.postForEntity(
                "http://localhost:8081/users",
                requestEntity,
                UserRest.class
        );

        Assertions.assertEquals(HttpStatus.OK, createdUserDetails.getStatusCode());
        Assertions.assertEquals(userDetailsRequestJson.get("firstName"), createdUserDetails.getBody().getFirstName());

    }

    @Test
    @Order(3)
    @DisplayName("GET /users requires JWT")
    public void testGetUsers_whenMissingJWT_returns403 () {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", "application/json");

        HttpEntity requestEntity = new HttpEntity(null, httpHeaders);

        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange(
                "http://localhost:8081/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<UserRest>>() {}
        );

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(2)
    @DisplayName("/login works")
    public void testUserLogin_whenValidCredentialsProvided_returnsJWTinAuthorizationHeader() throws JSONException {

        JSONObject credentialsRequestJson = new JSONObject();
        credentialsRequestJson.put("email", "test@test.com");
        credentialsRequestJson.put("password", "12345678");

        HttpEntity<String> requestEntity = new HttpEntity<>(credentialsRequestJson.toString());

        ResponseEntity response = testRestTemplate.postForEntity(
                "http://localhost:8081/users/login",
                requestEntity,
                null
        );
        authToken = response.getHeaders()
                            .getValuesAsList(SecurityConstants.HEADER_STRING)
                            .get(0);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getHeaders().getValuesAsList(SecurityConstants.HEADER_STRING).get(0));
        Assertions.assertNotNull(response.getHeaders().getValuesAsList("UserID").get(0));
    }

    @Test
    @Order(4)
    @DisplayName("GET /users works")
    public void testGetUsers_whenValidJWTProvided_returnsUser() throws JSONException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(authToken);

        HttpEntity requestEntity = new HttpEntity(httpHeaders);

        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange(
                "http://localhost:8081/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<UserRest>>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().size() == 1);

    }

}