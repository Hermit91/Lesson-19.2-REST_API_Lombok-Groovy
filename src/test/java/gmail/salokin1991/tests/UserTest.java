package gmail.salokin1991.tests;

import gmail.salokin1991.lombok.LombokUserData;
import gmail.salokin1991.models.UserData;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest extends Specs {
    @Test
    void singleUser() {
        // @formatter:off
        given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .log().body();
        // @formatter:on
    }

    @Test
    void listOfUsers() {
        // @formatter:off
        given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .log().body();
        // @formatter:on
    }

    @Test
    void singleUserWithModel() {
        // @formatter:off
        UserData data = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(UserData.class);
        // @formatter:on
        assertEquals(2, data.getData().getId());
    }

    @Test
    void singleUserWithLombokModel() {
        // @formatter:off
        LombokUserData data = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(LombokUserData.class);
        // @formatter:on
        assertEquals(2, data.getUser().getId());
    }

    @Test
    public void checkEmailUsingGroovy() {
        // @formatter:off
        given()
                .spec(request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
        // @formatter:on
    }
}
