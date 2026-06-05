package ru.yandex.disk.tests;

import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.apache.http.HttpStatus.*;

@Feature("PUT запросы")
public class PutTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/PutTestFolder";

    @AfterEach
    void deleteTestFolder() {
        client.deleteFolder(TEST_FOLDER);
    }

    @Test
    @DisplayName("PUT /v1/disk/resources - создание новой папки возвращает 201")
    void testCreateNewFolder() {
        client.createFolder(TEST_FOLDER)
                .then().statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("PUT /v1/disk/resources - повторное создание той же папки возвращает 409")
    void testRejectsDuplicateFolder() {
        client.createFolder(TEST_FOLDER)
                .then().statusCode(SC_CREATED);
        client.createFolder(TEST_FOLDER)
                .then().statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("PUT /v1/disk/resources - пустой параметр path возвращает 400")
    void testCreateFolderWithEmptyPath() {
        client.createFolder("")
                .then().statusCode(SC_BAD_REQUEST);
    }
}