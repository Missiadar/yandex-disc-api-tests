package ru.yandex.disk.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.yandex.disk.config.TestConfig;

import static io.restassured.RestAssured.given;

public class DiskApiClient {

    private RequestSpecification unauthorizedRequest() {
        return given()
                .baseUri(TestConfig.BASE_URL)
                .header("Content-Type", "application/json");
    }

    private RequestSpecification authorizedRequest() {
        return unauthorizedRequest()
                .header("Authorization", "OAuth " + TestConfig.TOKEN);
    }

    public Response getResource(String path) {
        return authorizedRequest()
                .queryParam("path", path)
                .when()
                .get("/v1/disk/resources");
    }

    public Response getResourceWithoutToken(String path) {
        return unauthorizedRequest()
                .queryParam("path", path)
                .when()
                .get("/v1/disk/resources");
    }

    public Response getTrashInfo(String path){
        return authorizedRequest()
                .queryParam("path", path)
                .when()
                .get("/v1/disk/trash/resources");
    }

    public Response createFolder(String path) {
        return authorizedRequest()
                .queryParam("path", path)
                .when()
                .put("/v1/disk/resources");
    }

    public Response restoreFromTrash(String trashPath) {
        return authorizedRequest()
                .queryParam("path", trashPath)
                .when()
                .put("/v1/disk/trash/resources/restore");
    }

    public Response copyResource(String from, String to) {
        return authorizedRequest()
                .queryParam("from", from)
                .queryParam("path", to)
                .when()
                .post("/v1/disk/resources/copy");
    }

    public Response moveResource(String from, String to) {
        return authorizedRequest()
                .queryParam("from", from)
                .queryParam("path", to)
                .when()
                .post("/v1/disk/resources/move");
    }

    public Response deleteFolder(String path) {
        return authorizedRequest()
                .queryParam("path", path)
                .queryParam("permanently", true)
                .when()
                .delete("/v1/disk/resources");
    }

    public Response moveFolderToTrash(String path) {
        return authorizedRequest()
                .queryParam("path", path)
                .queryParam("permanently", false)
                .when()
                .delete("/v1/disk/resources");
    }

    public Response clearTrash() {
        return authorizedRequest()
                .when()
                .delete("/v1/disk/trash/resources");
    }
}