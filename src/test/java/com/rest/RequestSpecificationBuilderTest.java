package com.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class RequestSpecificationBuilderTest {

    RequestSpecification requestSpecification;

    @BeforeTest
    public void beforeClass(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(ConfigLoader.getBaseUri());
        requestSpecBuilder.addHeader("x-api-key", ConfigLoader.getApiKey());
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();
    }


    @org.testng.annotations.Test
    public void validate_get_status_code_request_specification(){
        given().
                spec(requestSpecification).
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                body("workspaces.name", hasItems("MyWorkspace", "MyChangedName"),
                        "workspaces.type", hasItems("team", "team"),
                        "workspaces[0].name", is(equalTo("MyWorkspace")),
                        "workspaces.size()", equalTo(2),
                        "workspaces.name", hasItem("MyWorkspace")

                );

    }

    @Test
    public void validate_get_status_code_request_response_specification(){

        Response response = given().spec(requestSpecification).get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path( "workspaces[0].name").toString(), equalTo("MyWorkspace"));
//                body("workspaces.name", hasItems("MyWorkspace", "MyChangedName"),
//                        "workspaces.type", hasItems("team", "team"),
//                        "workspaces[0].name", is(equalTo("MyWorkspace")),
//                        "workspaces.size()", equalTo(2),
//                        "workspaces.name", hasItem("MyWorkspace")
//
//                );

    }
    @Test
    public void validate_get_status_code_request_response_specification_alternateway(){

        Response response = given(requestSpecification).
                header("dummyheader", "dummyValue1").
                get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path( "workspaces[0].name").toString(), equalTo("MyWorkspace"));
//
//                );

    }
}
