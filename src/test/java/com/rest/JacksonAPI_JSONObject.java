package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import utils.ConfigLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class JacksonAPI_JSONObject {

    @BeforeTest
    public void beforeClass(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri(ConfigLoader.getBaseUri()).
                //setBaseUri(ConfigLoader.getBaseUri2()).
                addHeader("x-api-key", ConfigLoader.getApiKey()).

                //setConfig(config.encoderConfig(EncoderConfig.encoderConfig().
                //appendDefaultContentCharsetToContentTypeIfUndefined(false))).
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
    public void validate_post_request_payload_as_map(){

        HashMap<String, Object> mainObject = new HashMap<String, Object>();

        HashMap<String, String> nestedObject = new HashMap<String, String>();
        nestedObject.put("name", "MySecondWorkspace");
        nestedObject.put("type", "team");
        nestedObject.put("description", "Created as Part of RestAsured Automation");

        mainObject.put("workspace", nestedObject);
        ObjectMapper objectMapper= new ObjectMapper();
        String mainObjectStr = objectMapper.writeValueAsString(mainObject);

        given().
                body(mainObjectStr).
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

        List<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        jsonList.add(obj5001);
        jsonList.add(obj5002);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonListStr = objectMapper.writeValueAsString(jsonList);

        given().
                body(jsonListStr).
        when().
                post("/post").
        then().
                assertThat().
                body("msg",equalTo("successful"));

    }

    @org.testng.annotations.Test
    public void validate_Serialize_arraylist_using_jackson(){
        //to run this test make sure in the before class the BaseUri2 is being called to access this mock URI

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode nestedObjNode1 = objectMapper.createObjectNode();
        nestedObjNode1.put("id", "5001");
        nestedObjNode1.put("type", "None");

        ObjectNode nestedNode2 = objectMapper.createObjectNode();
        nestedNode2.put("id", "5002");
        nestedNode2.put("type", "Glazed");


       ArrayNode mainNode = objectMapper.createArrayNode();
       mainNode.add(nestedObjNode1);
       mainNode.add(nestedNode2);

       String mainJsonObjStr = objectMapper.writeValueAsString(mainNode);

        given().
                body(mainJsonObjStr).
        when().
                post("/post").
        then().
                assertThat().
                body("msg",equalTo("successful"));

    }

    @org.testng.annotations.Test
    public void validate_json_payload_using_jackson(){

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode nestedNode = objectMapper.createObjectNode();
        nestedNode.put("name", "MySecondWorkspace");
        nestedNode.put("type", "team");
        nestedNode.put("description", "Created as Part of RestAsured Automation");

        ObjectNode mainNode = objectMapper.createObjectNode();
        mainNode.set("workspace", nestedNode);

        String mainObjStr = objectMapper.writeValueAsString(mainNode);

        given().
                body(mainObjStr).
                when().
                post("/workspaces").
                then().
                assertThat().
                body("workspace.name",equalTo("MySecondWorkspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));

    }



}
