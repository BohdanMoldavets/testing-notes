package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8081")
class UsersControllerIntegrationTest {



}