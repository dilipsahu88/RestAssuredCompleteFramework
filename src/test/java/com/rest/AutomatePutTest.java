package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.thread.IWorker;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class AutomatePutTest {
    String workspaceId = "73cfc671-e37d-4d3c-a67e-c80f2ee89327";
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
                "        \"name\": \"MyFirstWorkspaceUpdated1\",\n" +
                "        \"type\": \"team\",\n" +
                "        \"description\": \"\"\n" +
                "    }\n" +
                "}";

        given().
                body(payload).
        when().
                put("/workspaces/"+ workspaceId).
        then().
                assertThat().
                body("workspace.name",equalTo("MyFirstWorkspaceUpdated1"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", equalTo(workspaceId));

    }


    @org.testng.annotations.Test
    public void validate_post_request_non_bdd_style(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"MyFirstWorkspaceUpdated\",\n" +
                "        \"type\": \"team\",\n" +
                "        \"description\": \"\"\n" +
                "    }\n" +
                "}";


        Response response = with().
                body(payload).
                put("/workspaces/" + workspaceId);

        assertThat(response.path("workspace.name").toString(), equalTo("MyFirstWorkspaceUpdated"));
        assertThat(response.path( "workspace.id").toString(), matchesPattern("^[a-z0-9-]{36}$"));
        assertThat(response.path( "workspace.id").toString(), equalTo(workspaceId));

    }
}
