package ru.yandex.disk.tests;

import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("PUT запросы")
public class PutTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/PutTestFolder";

    @AfterEach
    void deleting() {
        client.deleteFolder(TEST_FOLDER);
    }

    @Test
    @DisplayName("PUT /v1/disk/resources - создание новой папки возвращает 201")
    void testCreateFolder() {
        Response response = client.createFolder(TEST_FOLDER);
        assertThat("Статус должен быть 201",
                response.statusCode(), equalTo(201));
    }

    @Test
    @DisplayName("PUT /v1/disk/resources - повторное создание той же папки возвращает 409")
    void testCreateFolderAlreadyExists() {
        client.createFolder(TEST_FOLDER);
        Response response = client.createFolder(TEST_FOLDER);
        assertThat("Статус должен быть 409",
                response.statusCode(), equalTo(409));
    }
}