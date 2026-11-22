package com.rest;

import io.restassured.config.LogConfig;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomateGetRequestUsingHamcrest {

    @Test
    public void extract_response_using_hamcrest(){
        String name = given().
                baseUri("https://api.postman.com").
                header("x-api-key", ConfigLoader.getApiKey()).
                when().
                get("/workspaces").
                then().
                assertThat().
                statusCode(200).
                extract().
                response().path("workspaces[0].name");

        System.out.println("workspace name is : " + name);

        assertThat(name, equalTo("MyWorkspace"));
        Assert.assertEquals(name, "MyWorkspace");


    }

    @Test
    public void validate_response_body_hamcrest_learning(){

        given().
                baseUri("https://api.postman.com").
                header("x-api-key", ConfigLoader.getApiKey()).
                when().
                get("/workspaces").
                then().
                assertThat().
                statusCode(200).
                body("workspaces.name", contains("MyWorkspace", "MyChangedName"),
                    "workspaces.name", is(not(empty())),
                        "workspaces.name", hasSize(2),
                "workspaces.name", everyItem(startsWith("My")),
                        "workspaces[0]", hasKey("id"),
                        "workspaces[0]", hasValue("MyWorkspace"),

                // find and validate key value pair
                        "workspaces[0]", hasEntry("id", "fd5a7d8c-85cf-4b71-89de-edad1814388b"),
                        "workspaces[0]", not(equalTo(Collections.EMPTY_MAP)),
                        "workspaces[0].name", allOf(startsWith("My"), containsString("Workspace"))

                //this fails
               //body("workspaces.name", contains("MyChangedName", "MyWorkspace")

                //This works
               //body("workspaces.name", containsInAnyOrder("MyChangedName", "MyWorkspace")
                );
    }

    @Test
    public void request_response_logging(){
        given().
                baseUri(ConfigLoader.getBaseUri()).
                header("x-api-key", ConfigLoader.getApiKey()).
                log().all().
                //log().headers().
                //log().cookies().
                //log().parameters().
                //logs only if the validations fails
                //log().ifValidationFails().
                //a config to avoid adding both validation fail function for request and response
                //config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
        when().
                get("/workspaces").
        then().
                //log().all().
                //logs only if the validations fails
                log().ifValidationFails().
                //Log only if error
                //log().ifError().
                assertThat().
                statusCode(200);
    }

    @Test
    public void logs_blacklist_header(){

        Set<String> headers =  new HashSet<String>();
        headers.add("x-api-key");
        headers.add("Accept");
        given().
                baseUri(ConfigLoader.getBaseUri()).
                header("x-api-key", ConfigLoader.getApiKey()).
                //config(config.logConfig(LogConfig.logConfig().blacklistHeader("x-api-key"))).
                  config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))).
                log().all().
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

}
