package com.rest;

import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class RequestParametersTest {

    @Test
    public void single_query_parameter(){
        given().
                baseUri("https://postman-echo.com").
                param("foo1", "bar1").
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void single_query_parameter_using_query_param(){
        given().
                baseUri("https://postman-echo.com").
                queryParam("foo1", "bar1").
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void multiple_query_parameter_using_param(){
        given().
                baseUri("https://postman-echo.com").
                param("foo1", "bar1").
                param("foo2", "bar2").
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void multiple_query_parameter_using_hasMap(){
        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("foo1", "bar1");
        queryParam.put("foo2", "bar2");
        given().
                baseUri("https://postman-echo.com").
                queryParams(queryParam).
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void multiple_value_query_parameter(){

        given().
                baseUri("https://postman-echo.com").
                //queryParam("foo1", "bar1, bar2, bar3").
                queryParam("foo1", "bar1;bar2;bar3").
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void validate_path_param(){

        given().
                baseUri("https://reqres.in").
                pathParam("userId", "2").
                log().all().

        when().
                get("/api/users/{userId}").
        then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void validate_multi_part_form_data(){

        given().
                baseUri("https://postman-echo.com").
                multiPart("foo1", "bar1").
                log().all().

                when().
                post("/post").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void validate_upload_file_multipart_form_data(){
        String attribute = "{\"name\":\"temp.txt\", \"parent\":{\"id\":\"123456\"}}";
        given().
                baseUri("https://postman-echo.com/").
                multiPart("file", new File("temp.txt") ).
                multiPart("attribute", attribute, "application/json").
                log().all().

                when().
                post("/post").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void validate_download_file_using_rest_Assured(){
        String attribute = "{\"name\":\"temp.txt\", \"parent\":{\"id\":\"123456\"}}";
        given().
                baseUri("https://postman-echo.com/").
                multiPart("file", new File("temp.txt") ).
                multiPart("attribute", attribute, "application/json").
                log().all().

                when().
                post("/post").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }

    @Test
    public void validate_form_url_encoded(){

        given().
                baseUri("https://postman-echo.com/").
                config(config().encoderConfig(EncoderConfig.encoderConfig()
                                .appendDefaultContentCharsetToContentTypeIfUndefined(false))).


                formParam("key1", "value1").
                formParam("Key 2", "value 2").
                log().all().

                when().
                post("/post").
                then().
                log().all().
                assertThat().
                statusCode(200);

    }


}
