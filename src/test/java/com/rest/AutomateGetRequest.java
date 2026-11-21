package com.rest;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AutomateGetRequest {

    @Test
    public void validate_get_status_code(){
        given().
                baseUri("https://api.postman.com").
                header("x-api-key","XXX").
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("workspaces.name", hasItems("MyWorkspace", "ChangedName"),
                        "workspaces.type", hasItems("team", "team"),
                        "workspaces[0].name", is(equalTo("MyWorkspace")),
                        "workspaces.size()", equalTo(2),
                        "workspaces.name", hasItem("MyWorkspace")

                );

    }


}
