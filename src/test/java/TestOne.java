import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class TestOne {

    @Test
    public void testLoginSuccess(){

        String response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body("{\n"
                + "    \"email\": \"eve.holt@reqres.in\",\n"
                + "    \"password\": \"cityslicka\"\n"
                + "}")
            .post("https://reqres.in/api/login")
            .then().log().all().extract().asString();

    }
}
