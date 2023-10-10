import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.example.helpers.JsonDataProviderReader;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestOne {

    JSONObject loginData;
    JSONObject userData;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        JsonDataProviderReader jsonDataProviderReader = new JsonDataProviderReader();
        loginData = jsonDataProviderReader.readJsonData("src/main/resources/userLoginData.json");
        userData = jsonDataProviderReader.readJsonData("src/main/resources/userData.json");
    }

    @Test
    public void testLoginSuccess(){
        JSONObject successLoginData = (JSONObject) loginData.get("success_login_data");
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(successLoginData.toJSONString())
            .post("/login")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().asString();

    }

    @Test
    public void testLoginBadRequest(){
        JSONObject badRequestLoginData = (JSONObject) loginData.get("bad_request_login_data");
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(badRequestLoginData.toJSONString())
            .post("/login")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().asString();

    }

    @Test
    public void testGetUser() {
        RestAssured.given()
            .when()
            .get("/users/2")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("data.id", Matchers.equalTo(2));
    }

    @Test
    public void testDeleteUser() {
        RestAssured.given()
            .when()
            .delete("/users/2")
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testPatchUser() {
        JSONObject updateUserData = (JSONObject) userData.get("update_user");
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(updateUserData.toJSONString())
            .patch("/users/2")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("name", Matchers.equalTo("morpheus"))
            .body("job", Matchers.equalTo("zion resident"));
    }

    @Test
    public void testCreateUser() {
        JSONObject createUserData = (JSONObject) userData.get("create_user");
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(createUserData.toJSONString())
            .post("/users")
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("name", Matchers.equalTo("morpheus"))
            .body("job", Matchers.equalTo("leader"));
    }
}
