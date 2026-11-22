package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class AutomateDeleteTest {

    String workspaceId = "6711db85-c2d0-49d2-aa27-a36940ba51d4";
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
    public void validate_delete_request_bdd_style(){

        given().
                when().
                delete("/workspaces/"+ workspaceId).
                then().
                assertThat().
                body("workspace.id", equalTo(workspaceId),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));

    }


    @org.testng.annotations.Test
    public void validate_delete_request_non_bdd_style(){


        Response response = with().
                delete("/workspaces/" + workspaceId);

        assertThat(response.path( "workspace.id").toString(), matchesPattern("^[a-z0-9-]{36}$"));
        assertThat(response.path( "workspace.id").toString(), equalTo(workspaceId));

    }
}
