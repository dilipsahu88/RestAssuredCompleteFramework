package com.rest;

import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static java.util.Locale.filter;

public class JsonSchemaValidationTest {

    @Test
    public void validate_json_Schema(){
        given().
                baseUri("https://postman-echo.com").
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("EchoGet.json"));

    }

    @Test
    public void validate_json_Schema_using_logging_filter(){
        given().
                baseUri("https://postman-echo.com").
               // filter(new RequestLoggingFilter()).
                filter(new ResponseLoggingFilter(LogDetail.BODY)).
                //filter(new ResponseLoggingFilter()).
                filter(new ResponseLoggingFilter(LogDetail.STATUS)).
        when().
                get("/get").
        then().
                assertThat().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("EchoGet.json"));

    }

    @Test
    public void validate_json_Schema_using_logging_to_file() throws FileNotFoundException {

        PrintStream fileOutputStream = new PrintStream(new File("restAssured.log"));
        given().
                baseUri("https://postman-echo.com").
                // filter(new RequestLoggingFilter()).
                        filter(new ResponseLoggingFilter(fileOutputStream)).
                //filter(new ResponseLoggingFilter()).
                        filter(new ResponseLoggingFilter(fileOutputStream)).
                when().
                get("/get").
                then().
                assertThat().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("EchoGet.json"));

    }

    @Test
    public void validate_json_Schema_using_logging_filter_to_file() throws FileNotFoundException {

        PrintStream fileOutputStream = new PrintStream(new File("restAssured.log"));
        given().
                baseUri("https://postman-echo.com").
                filter(new ResponseLoggingFilter(LogDetail.BODY, fileOutputStream)).
                filter(new ResponseLoggingFilter(LogDetail.STATUS, fileOutputStream)).
                when().
                get("/get").
                then().
                assertThat().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("EchoGet.json"));

    }
}
