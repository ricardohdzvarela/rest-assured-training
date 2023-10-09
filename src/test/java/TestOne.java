import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestOne {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    public void testLoginSuccess(){

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body("{\n"
                + "    \"email\": \"eve.holt@reqres.in\",\n"
                + "    \"password\": \"cityslicka\"\n"
                + "}")
            .post("/login")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .log().all().extract().asString();

    }

    @Test
    public void testGetUsers() {
        RestAssured.given().when().get("/users/2")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("data.id", Matchers.equalTo(2));
    }
}
