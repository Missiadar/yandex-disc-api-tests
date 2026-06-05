package ru.yandex.disk.tests;

import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("POST запросы")
public class PostTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/PostTestFolder";
    private static final String COPY_FOLDER   = "disk:/PostTestCopy";

    @BeforeEach
    void creating() {
        client.createFolder(TEST_FOLDER);
    }

    @AfterEach
    void deleting() {
        client.deleteFolder(TEST_FOLDER);
        client.deleteFolder(COPY_FOLDER);
    }

    @Test
    @DisplayName("POST /v1/disk/resources/copy - копирование папки возвращает 201")
    void testCopyFolder() {
        Response response = client.copyResource(TEST_FOLDER, COPY_FOLDER);
        assertThat("Статус должен быть 201",
                response.statusCode(), equalTo(201));
    }

    @Test
    @DisplayName("POST /v1/disk/resources/copy - копирование в уже занятый путь возвращает 409")
    void testCopyToExistingPath() {
        client.copyResource(TEST_FOLDER, COPY_FOLDER);
        Response response = client.copyResource(TEST_FOLDER, COPY_FOLDER);
        assertThat("Статус должен быть 409",
                response.statusCode(), equalTo(409));
    }
}