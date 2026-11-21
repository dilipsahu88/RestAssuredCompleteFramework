package com.rest;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
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
}
