package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import utils.ConfigLoader;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomatePostTest {

    @BeforeTest
    public void beforeClass(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
        setBaseUri(ConfigLoader.getBaseUri()).
        addHeader("x-api-key", ConfigLoader.getApiKey()).
        setContentType(ContentType.JSON)
        .log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.responseSpecification = responseSpecBuilder.build();

    }


    @org.testng.annotations.Test
    public void validate_post_request_bdd_style(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"id\": \"fd5a7d8c-85cf-4b71-89de-edad1814388b\",\n" +
                "        \"name\": \"MyFirstWorkspace\",\n" +
                "        \"type\": \"team\",\n" +
                "        \"description\": null\n" +
                "            }\n" +
                "}";

        given().
                body(payload).
        when().
                post("/workspaces").
        then().
        assertThat().
        body("workspace.name",equalTo("MyFirstWorkspace"),
                "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));

    }


    @org.testng.annotations.Test
    public void validate_post_request_non_bdd_style(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"id\": \"fd5a7d8c-85cf-4b71-89de-edad1814388b\",\n" +
                "        \"name\": \"MyFirstWorkspace\",\n" +
                "        \"type\": \"team\",\n" +
                "        \"description\": null\n" +
                "            }\n" +
                "}";

        Response response = with().
                body(payload).
                post("/workspaces");

                assertThat(response.path("workspace.name").toString(), equalTo("MyFirstWorkspace"));
                assertThat(response.path( "workspace.id").toString(), matchesPattern("^[a-z0-9-]{36}$"));

    }
}
