import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.io.InputStream;
import org.apache.http.HttpStatus;
import org.example.helpers.JsonDataProviderReader;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestTwo {

    JSONObject userData;
    JSONObject resourceData;

    @BeforeClass(groups = {"manageUsers"})
    public void setup() {
        JsonDataProviderReader jsonDataProviderReader = new JsonDataProviderReader();

        userData = jsonDataProviderReader.readJsonData("src/main/resources/user/userData.json");
        resourceData = jsonDataProviderReader.readJsonData("src/main/resources/resource/resourceData.json");
    }

    @Test(groups = {"manageUsers"})
    public void testGetListUsers() {
        InputStream getUserListSchemaResponse = getClass().getClassLoader()
            .getResourceAsStream("responseSchemas/getUserListSchemaResponse.json");

        RestAssured.given()
            .when()
            .get("/users")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(getUserListSchemaResponse));
    }

    @Test(groups = {"manageUsers"})
    public void testGetSingleUser() {
        InputStream getUserSchemaResponse = getClass().getClassLoader()
            .getResourceAsStream("responseSchemas/getUserSchemaResponse.json");

        RestAssured.given()
            .when()
            .get("/users/2")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(getUserSchemaResponse));
    }

    @Test(groups = {"manageUsers"})
    public void testGetSingleUserNotFound() {
        RestAssured.given()
            .when()
            .get("/users/23")
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testGetListResource() {
        InputStream getResourceListSchema = getClass().getClassLoader()
            .getResourceAsStream("responseSchemas/getResourceListSchemaResponse.json");

        RestAssured.given()
            .when()
            .get("/unknown")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(getResourceListSchema));
    }

    @Test
    public void testGetSingleResource() {
        InputStream getResourceSchema = getClass().getClassLoader()
            .getResourceAsStream("responseSchemas/getResourceSchemaResponse.json");

        RestAssured.given()
            .when()
            .get("/unknown/2")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(getResourceSchema));
    }

    @Test
    public void testGetSingleResourceNotFound() {
        RestAssured.given()
            .when()
            .get("/unknown/23")
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"manageUsers"})
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

    @Test(groups = {"manageUsers"})
    public void testPatchUser() {
        InputStream updateUserSchema = getClass().getClassLoader()
            .getResourceAsStream("responseSchemas/updateUserSchemaResponse.json");

        JSONObject updateUserData = (JSONObject) userData.get("update_user");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(updateUserData.toJSONString())
            .patch("/users/2")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(updateUserSchema));
    }

    @Test(groups = {"manageUsers"})
    public void testUpdateUser() {
        InputStream updateUserSchema = getClass().getClassLoader()
            .getResourceAsStream("responseSchemas/updateUserSchemaResponse.json");

        JSONObject updateUserData = (JSONObject) userData.get("update_user");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(updateUserData.toJSONString())
            .put("/users/2")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(updateUserSchema));
    }

    @Test(groups = {"manageUsers"})
    public void testDeleteUser() {
        RestAssured.given()
            .when()
            .delete("/users/2")
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testPostRegisterSuccessful() {
        InputStream postRegisterSchema = getClass().getClassLoader()
            .getResourceAsStream("responseSchemas/postRegisterSchemaResponse.json");

        JSONObject createResource = (JSONObject) resourceData.get("create_resource");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(createResource.toJSONString())
            .post("/register")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(postRegisterSchema));
    }

    @Test
    public void testPostRegisterUnsuccessful() {
        JSONObject resourceBadRequest = (JSONObject) resourceData.get("resource_bad_request");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(resourceBadRequest.toJSONString())
            .post("/register")
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}
