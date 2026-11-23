package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import utils.ConfigLoader;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomatePostTest {

    @BeforeTest
    public void beforeClass(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
        //setBaseUri(ConfigLoader.getBaseUri()).
        //addHeader("x-api-key", ConfigLoader.getApiKey()).

        //Enable the below 2-line for last array test case
        setBaseUri(ConfigLoader.getBaseUri2()).
        addHeader("x-mock-match-request-body", "true").
        //setConfig(config.encoderConfig(EncoderConfig.encoderConfig().
        //appendDefaultContentCharsetToContentTypeIfUndefined(false))).
         setContentType(ContentType.JSON)
        //setContentType("application/json; charset=utf-8")
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

    @org.testng.annotations.Test
    public void validate_post_request_payload_from_file(){

        File file = new File("src/main/resources/CreateWorkspacePayload.json");

        given().
                body(file).
                when().
                post("/workspaces").
                then().
                assertThat().
                body("workspace.name",equalTo("MyFirstWorkspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));

    }

    @org.testng.annotations.Test
    public void validate_post_request_payload_as_map(){

        HashMap<String, Object> mainObject = new HashMap<String, Object>();

        HashMap<String, String> nestedObject = new HashMap<String, String>();
        nestedObject.put("name", "MySecondWorkspace");
        nestedObject.put("type", "team");
        nestedObject.put("description", "Created as Part of RestAsured Automation");

        mainObject.put("workspace", nestedObject);

        given().
                body(mainObject).
                when().
                post("/workspaces").
                then().
                assertThat().
                body("workspace.name",equalTo("MySecondWorkspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));

    }

    @org.testng.annotations.Test
    public void validate_post_request_payload_json_as_arraylist(){
        //to run this test make sure in the before class the BaseUri2 is being called to access this mock URI

        HashMap<String, String> obj5001 = new HashMap<String, String>();
        obj5001.put("id", "5001");
        obj5001.put("type", "None");

        HashMap<String, String> obj5002 = new HashMap<String, String>();
        obj5002.put("id", "5002");
        obj5002.put("type", "Glazed");

        List<HashMap<String, String>>  jsonList = new ArrayList<HashMap<String, String>>();
        jsonList.add(obj5001);
        jsonList.add(obj5002);

        System.out.println(jsonList);
        given().
                body(jsonList).
                when().
                post("/post").
                then().
                assertThat().
                body("msg",equalTo("successful"));

    }
}
