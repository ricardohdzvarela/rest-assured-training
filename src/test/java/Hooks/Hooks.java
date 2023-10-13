package Hooks;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;

public class Hooks {

    @BeforeSuite
    public void beforeSuiteSetup() {
        System.out.println("Initial configuration...");
        System.out.println("Running tests on: " + System.getProperty("env", "qa"));

        if(System.getProperty("env", "qa").equals("qa")) {
            RestAssured.baseURI = "https://reqres.in";
        } else if(System.getProperty("env", "qa").equals("dev")) {
            RestAssured.baseURI = "https://reqres.dev.in";
        }

        RestAssured.basePath = "/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

}
