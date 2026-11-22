package com.rest;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class ResponseSpecificationTest {


    @BeforeTest
    public void beforeClass(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(ConfigLoader.getBaseUri());
        requestSpecBuilder.addHeader("x-api-key", ConfigLoader.getApiKey());
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();
        RestAssured.requestSpecification = requestSpecBuilder.build();

        //ResponseSpecification
//        responseSpecification = RestAssured.expect().
//                statusCode(200).
//                contentType(ContentType.JSON).
//                log().all();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.responseSpecification = responseSpecBuilder.build();

    }


    @org.testng.annotations.Test
    public void validate_status_response_code(){
       get("/workspaces").
        then().spec(responseSpecification);

    }

    @org.testng.annotations.Test
    public void validate_response_body_responseSpecification(){
    Response response = get("/workspaces").
    then().spec(responseSpecification).
            extract().response();
        assertThat(response.path("workspaces[0].name").toString(), equalTo("MyWorkspace"));

    }


}
