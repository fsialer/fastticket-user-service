package com.fernando.fastticket_user_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.fastticket_user_service.domain.enums.ErrorType;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.AuthRequest;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.UserRequest;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.PersonEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories.RolRepository;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories.UserRepository;
import com.fernando.fastticket_user_service.infrastructure.config.TestMailConfig;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Testcontainers
@Import(TestMailConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserIntegrationTest(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder ){
        this.userRepository=userRepository;
        this.rolRepository=rolRepository;
        this.passwordEncoder=passwordEncoder;
   }

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto",()->"create");
    }

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI="http://localhost:"+port;
        rolRepository.save(RolEntity.builder().code("USER").description("User").build());
        rolRepository.save(RolEntity.builder().code("ADMIN").description("Administrador").build());
        rolRepository.save(RolEntity.builder().code("HELPDESK").description("Helpdesk").build());
    }

    @Test
    @DisplayName("When Send User Register Is Correct Expect Return201")
    void When_SendUserRegisterIsCorrect_Expect_Return201() throws Exception {
        UserRequest request = TestUtilUser.mockUserRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        given()
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post("/v1/users/register")
                .then()
                .statusCode(201)
                .header("Location", containsString("/v1/users/register/"))
                .body("id", notNullValue())
                .body("email", equalTo(request.getEmail()));
    }

    @Test
    @DisplayName("When Send User Register With An Email Exists Expect Return400")
    void When_SendUserRegisterWithAnEmailExists_Expect_Return400() throws Exception {
        userRepository.save(UserEntity.builder()
                .email("john@example.com")
                .password("test")
                .person(PersonEntity.builder().name("test")
                        .lastName("test").sex("M").build())
                .roles(Set.of(RolEntity.builder().id(1).code("USER").description("User").build()))
                .build());
        String requestJson = objectMapper.writeValueAsString(TestUtilUser.mockUserRequest());
        given()
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post("/v1/users/register")
                .then()
                .statusCode(400)
                .body("code", equalTo("USER_002"))
                .body("type", equalTo(ErrorType.FUNCTIONAL.name()));
    }

    @Test
    @DisplayName("When Email And Password Are Checking Expect Return 200")
    void When_EmailAndPasswordAreChecking_Expect_Return200() throws Exception{
        userRepository.save(UserEntity.builder()
                .email("example2@hotmail.com")
                .password(passwordEncoder.encode("ollssds"))
                .person(PersonEntity.builder().name("test")
                        .lastName("test").sex("M").build())
                .roles(Set.of(RolEntity.builder().id(1).code("USER").description("User").build()))
                .confirmEmail(true)
                .build());
        AuthRequest rq=TestUtilUser.mockAuthRequest();
        rq.setEmail("example2@hotmail.com");
        String requestJson = objectMapper.writeValueAsString(rq);
        given()
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post("/v1/users/check")
                .then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("email", equalTo("example2@hotmail.com"))
                .body("fullName", equalTo("test test"))
                .body("roles",hasItem("USER"));

    }
}
