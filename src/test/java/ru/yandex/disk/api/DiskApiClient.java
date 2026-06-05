package ru.yandex.disk.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.yandex.disk.config.TestConfig;

import static io.restassured.RestAssured.given;

public class DiskApiClient {

    private RequestSpecification baseRequest() {
        return given()
                .baseUri(TestConfig.BASE_URL)
                .header("Authorization", "OAuth " + TestConfig.TOKEN)
                .header("Content-Type", "application/json");
    }

    // GET

    public Response getDiskInfo() {
        return baseRequest()
                .when()
                .get("/v1/disk");
    }

    public Response getResourcesInfo(String path) {
        return RestAssured
                .given()
                    .baseUri("https://cloud-api.yandex.net")
                    .header("Content-Type", "application/json")
                    .queryParam("path", path)
                .when()
                    .get("/v1/disk/resources");
    }

    public Response getTrashInfo(String path){
        return baseRequest()
                .queryParam("path", path)
                .when()
                .get("/v1/disk/trash/resources");
    }

    // PUT

    public Response createFolder(String path) {
        return baseRequest()
                .queryParam("path", path)
                .when()
                .put("/v1/disk/resources");
    }

    // POST

    public Response copyResource(String from, String to) {
        return baseRequest()
                .queryParam("from", from)
                .queryParam("path", to)
                .when()
                .post("/v1/disk/resources/copy");
    }

    // DELETE

    public Response deleteFolder(String path) {
        return baseRequest()
                .queryParam("path", path)
                .queryParam("permanently", true)
                .when()
                .delete("/v1/disk/resources");
    }

    public Response clearTrash() {
        return baseRequest()
                .when()
                .delete("/v1/disk/trash/resources");
    }
}