package com.rest;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AutomateGetRequest {

    @Test
    public void validate_get_status_code(){
        given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-6aff5cc3c2f4bd0001904062-35eb7106419290daea450c7c3d1e1e0944").
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
