package com.rest;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AutomateGetRequest {

    @Test
    public void validate_get_status_code(){
        given().
                baseUri(ConfigLoader.getBaseUri()).
                header("x-api-key", ConfigLoader.getApiKey()).
        when().
                get("/workspaces").
        then().
                log().all().
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
    public void extract_response(){
        String res = given().
                baseUri(ConfigLoader.getBaseUri()).
                header("x-api-key", ConfigLoader.getApiKey()).
        when().
                get("/workspaces").
        then().
        assertThat().
        statusCode(200).
        extract().
        response().asString();

        System.out.println("Response : " + res);
//        System.out.println("workspace name : " + res.path("workspaces[0].name"));
//
//        JsonPath jsonPath = new JsonPath(res.asString());
//        System.out.println("workspace name : " + jsonPath.getString("workspaces[0].name"));

            System.out.println("workspace name : " + JsonPath.from(res).getString("workspaces[0].name"));

    }


}
