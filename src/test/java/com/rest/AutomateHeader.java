package com.rest;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AutomateHeader {

    @Test
    public void multiple_headers(){

        Header header = new Header("header", "value1");
        Header matchHeader = new Header("x-mock-match-request-headers", "header");

        given().
                baseUri(ConfigLoader.getBaseUri2()).
                header(header).
                header(matchHeader).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200)
        ;
    }

    @Test
    public void multiple_headers_using_Headers(){

        Header header = new Header("header", "value1");
        Header matchHeader = new Header("x-mock-match-request-headers", "header");

        Headers headers = new Headers(header, matchHeader);

        given().
                baseUri(ConfigLoader.getBaseUri2()).
                headers(headers).
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200)
        ;
    }

    @Test
    public void multiple_headers_using_map(){

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("header", "value1");
        headers.put("x-mock-match-request-headers", "header");

        given().
                baseUri(ConfigLoader.getBaseUri2()).
                headers(headers).
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200)
        ;
    }

    @Test
    public void multi_value_header_in_request(){

//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("header", "value1");
//        headers.put("x-mock-match-request-headers", "header");


        given().
                baseUri(ConfigLoader.getBaseUri2()).
                header("multiValHeader", "value1", "value2").
                log().headers().
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200)
        ;
    }

    @Test
    public void assert_response_header(){

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("header", "value2");
        headers.put("x-mock-match-request-headers", "header");


        given().
                baseUri(ConfigLoader.getBaseUri2()).
                headers(headers).
                log().headers().
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200).
                header("responseHeader","resValue2")
        ;
    }

    @Test
    public void assert_multiple_response_header(){

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("header", "value2");
        headers.put("x-mock-match-request-headers", "header");


        given().
                baseUri(ConfigLoader.getBaseUri2()).
                headers(headers).
                log().headers().
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200).
                headers("responseHeader","resValue2",
                        "x-ratelimit-limit", "120")
        ;
    }

    @Test
    public void extract_response_headers(){

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("header", "value2");
        headers.put("x-mock-match-request-headers", "header");


        Headers extractedHeaders = given().
                baseUri(ConfigLoader.getBaseUri2()).
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().
                statusCode(200).
                extract().
                headers()
        ;
        System.out.println("header name :" + extractedHeaders.get("responseHeader").getName());
        System.out.println("header value :" + extractedHeaders.get("responseHeader").getValue());
        System.out.println("header value :" + extractedHeaders.getValue("responseHeader"));
    }

    @Test
    public void extract_all_header_value(){

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("header", "value2");
        headers.put("x-mock-match-request-headers", "header");


        Headers extractedHeaders = given().
                baseUri(ConfigLoader.getBaseUri2()).
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().
                statusCode(200).
                extract().
                headers();

        for(Header header : extractedHeaders){
            System.out.print("header Name : " + header.getName() + ",");
            System.out.println("Header Value :" + header.getValue());

        }
    }

    @Test
    public void extract_multi_value_from_response_header(){

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("header", "value1");
        headers.put("x-mock-match-request-headers", "header");


        Headers extractedHeaders = given().
                baseUri(ConfigLoader.getBaseUri2()).
                headers(headers).
                when().
                get("/get").
        then().
                assertThat().
                statusCode(200).
                extract().
                headers();

        List<String> values = extractedHeaders.getValues("multiValueHeader");
        for(String value: values){
            System.out.println(value);

        }
    }



}
