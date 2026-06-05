package ru.yandex.disk.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.yandex.disk.config.TestConfig;

import static io.restassured.RestAssured.given;

public class DiskApiClient {

    // без авторизации
    private RequestSpecification requestWithoutToken() {
        return given()
                .baseUri(TestConfig.BASE_URL)
                .header("Content-Type", "application/json");
    }

    // с авторизацией
    private RequestSpecification baseRequest() {
        return requestWithoutToken()
                .header("Authorization", "OAuth " + TestConfig.TOKEN);
    }

    // GET

    public Response getDiskInfo() {
        return baseRequest()
                .when()
                .get("/v1/disk");
    }

    public Response getResourcesInfo(String path) {
        return requestWithoutToken()
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