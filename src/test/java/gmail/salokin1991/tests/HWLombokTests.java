package gmail.salokin1991.tests;

import gmail.salokin1991.lombok.HWCreateUserRequest;
import gmail.salokin1991.lombok.HWCreateUserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

public class HWLombokTests extends HWSpecs {

    @Test
    public void checkAvatarPathUsingGroovy() {
        given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .log().body()
                .body("data.findAll{it.avatar =~/.*?image.jpg/}.avatar.flatten()",
                        hasItems("https://reqres.in/img/faces/12-image.jpg",
                                "https://reqres.in/img/faces/8-image.jpg"));
    }

    @Test
    public void checkIdUsingGroovy() {
        given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .log().body()
                .body("data.flatten().findAll{it.id}.id",
                        hasItems(7, 11));

    }

    @Test
    void createUserWithLombok() {
        HWCreateUserRequest createUser = new HWCreateUserRequest("ace", "detective");

        HWCreateUserResponse response = given()
                .spec(request)
                .body(createUser)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().as(HWCreateUserResponse.class);

        assertThat(createUser.getName().equals(response.getName()));
        assertThat(createUser.getJob().equals(response.getJob()));
        assertThat(response.getId()).isNotEmpty();
        assertThat(response.getCreatedAt()).isNotEmpty();
    }

    @Test
    void updateUserWithLombok() {
        HWCreateUserRequest createUser = new HWCreateUserRequest("ace", "detective");

        HWCreateUserResponse response = given()
                .spec(request)
                .body(createUser)
                .when()
                .put("/users/2")
                .then()
                .log().body()
                .spec(responseSpec)
                .extract().as(HWCreateUserResponse.class);

        assertThat(createUser.getName().equals(response.getName()));
        assertThat(createUser.getJob().equals(response.getJob()));
        assertThat(response.getUpdatedAt()).isNotEmpty();
    }

    @Test
    void listUsersTest() {
        Response response =
                given()
                        .spec(request)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .extract().response();

        assertThat(response.path("total").toString()).isEqualTo("12");
        assertThat((Integer) response.path("page")).isEqualTo(2);
        assertThat((Integer) response.path("per_page")).isEqualTo(6);
        assertThat(response.path("data.first_name").toString()).contains("George");
        assertThat(response.path("data.email").toString()).contains("tobias.funke@reqres.in");
        assertThat((List<?>) response.path("data.email")).hasSize(6);
    }

    @Test
    void listResourceTest() {
        Response response =
                given()
                        .spec(request)
                        .when()
                        .get("/unknown")
                        .then()
                        .extract().response();

        assertThat(response.path("support.text").toString()).startsWith("To").endsWith("ed!");
        assertThat(response.path("data.name").toString()).contains("aqua sky");
        assertThat((List<?>) response.path("data.id")).hasSize(6);
    }

    @Test
    void listSingleResourceNotFoundTest() {
        given()
                .spec(request)
                .when()
                .get("/unknown/23")
                .then()
                .statusCode(404);
    }

    @Test
    void deleteTest() {
        given()
                .spec(request)
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }
}
