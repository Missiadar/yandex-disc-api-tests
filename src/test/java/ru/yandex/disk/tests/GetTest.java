package ru.yandex.disk.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Feature("GET запросы")
public class GetTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/GetTestFolder";

    @BeforeEach
    void createTestFolder() {
        client.createFolder(TEST_FOLDER);
    }

    @AfterEach
    void deleteTestFolder() {
        client.deleteFolder(TEST_FOLDER);
    }

    @Test
    @DisplayName("GET /v1/disk/resources - чтение созданной папки возвращает 200 и корректные метаданные")
    void testGetCreatedFolder() {
        client.getResource(TEST_FOLDER)
                .then().statusCode(SC_OK)
                .body("type", equalTo("dir"))
                .body("name", equalTo("GetTestFolder"))
                .body("path", equalTo(TEST_FOLDER));
    }

    @Test
    @DisplayName("GET /v1/disk/resources - без токена возвращает 401")
    void testGetFolderWithoutToken() {
        client.getResourceWithoutToken(TEST_FOLDER)
                .then().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("GET /v1/disk/trash/resources - некорректный запрос disk:/ вместо trash:/ возвращает 400")
    void testGetBadRequest() {
        client.getTrashInfo("disk:/someFolder132")
                .then().statusCode(SC_BAD_REQUEST);
    }
}