package ru.yandex.disk.tests;

import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Feature("POST запросы")
public class PostTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/PostTestFolder";
    private static final String COPY_FOLDER   = "disk:/PostTestCopy";
    private static final String MOVE_FOLDER   = "disk:/PostTestMove";

    @BeforeEach
    void createTestFolder() {
        client.createFolder(TEST_FOLDER);
    }

    @AfterEach
    void deleteTestFolder() {
        client.deleteFolder(TEST_FOLDER);
        client.deleteFolder(COPY_FOLDER);
        client.deleteFolder(MOVE_FOLDER);
    }

    @Test
    @DisplayName("POST /v1/disk/resources/copy - копирование папки возвращает 201")
    void testCopyFolder() {
        client.copyResource(TEST_FOLDER, COPY_FOLDER)
                .then().statusCode(SC_CREATED);
        client.getResource(COPY_FOLDER)
                .then().statusCode(SC_OK)
                .body("type", equalTo("dir"))
                .body("path", equalTo(COPY_FOLDER));
    }

    @Test
    @DisplayName("POST /v1/disk/resources/copy - копирование в уже занятый путь возвращает 409")
    void testCopyToExistingPath() {
        client.copyResource(TEST_FOLDER, COPY_FOLDER)
                .then().statusCode(SC_CREATED);
        client.copyResource(TEST_FOLDER, COPY_FOLDER)
                .then().statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("POST /v1/disk/resources/move - перемещение папки возвращает 201")
    void testMoveFolder() {
        client.moveResource(TEST_FOLDER, MOVE_FOLDER)
                .then().statusCode(SC_CREATED);

        client.getResource(TEST_FOLDER)
                .then().statusCode(SC_NOT_FOUND);

        client.getResource(MOVE_FOLDER)
                .then().statusCode(SC_OK)
                .body("type", equalTo("dir"))
                .body("path", equalTo(MOVE_FOLDER));
    }
}