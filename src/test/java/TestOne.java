import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.example.helpers.JsonDataProviderReader;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestOne {

    JSONObject loginData;

    @BeforeClass(groups = {"login"})
    public void setup() {
        JsonDataProviderReader jsonDataProviderReader = new JsonDataProviderReader();
        loginData = jsonDataProviderReader.readJsonData("src/main/resources/user/userLoginData.json");
    }

    @Test(groups = {"login"})
    public void testLoginSuccessful(){
        JSONObject successLoginData = (JSONObject) loginData.get("success_login_data");
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(successLoginData.toJSONString())
            .post("/login")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().asString();
    }

    @Test(groups = {"login"})
    public void testLoginBadRequest() {
        JSONObject badRequestLoginData = (JSONObject) loginData.get("bad_request_login_data");
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(badRequestLoginData.toJSONString())
            .post("/login")
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .extract().asString();
    }

}
