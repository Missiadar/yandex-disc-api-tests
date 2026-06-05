package ru.yandex.disk.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Feature("DELETE запросы")
public class DeleteTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/DeleteTestFolder";
    private static final String TRASH_FOLDER  = "trash:/";

    @BeforeEach
    void createTestFolder() {
        client.createFolder(TEST_FOLDER);
    }

    @AfterEach
    void deleteTestFolder() {
        client.deleteFolder(TEST_FOLDER);
        client.clearTrash();
    }

    @Test
    @DisplayName("DELETE /v1/disk/resources - удаление существующей папки возвращает 204")
    void testDeleteExistingFolder() {
        client.deleteFolder(TEST_FOLDER)
                .then().statusCode(SC_NO_CONTENT);
    }

    @Test
    @DisplayName("DELETE /v1/disk/resources - удаление несуществующего ресурса возвращает 404")
    void testDeleteNonExistingFolder() {
        client.deleteFolder("disk:/NonExistingFolder")
                .then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /v1/disk/resources - удаление в корзину и последующее восстановление возвращает ресурс на диск")
    void testDeleteToTrashAndRestore() {
        client.moveFolderToTrash(TEST_FOLDER)
                .then().statusCode(SC_NO_CONTENT);

        Response response = client.getTrashInfo(TRASH_FOLDER);
        String path = response.jsonPath()
                .getString("_embedded.items.find { it.origin_path == '" + TEST_FOLDER + "' }.path");

        if (path == null) {
            throw new RuntimeException("Ресурс с именем " + TEST_FOLDER + " не найден в корзине.");
        }

        client.restoreFromTrash(path)
                .then().statusCode(anyOf(equalTo(SC_CREATED), equalTo(SC_ACCEPTED)));

        client.getResource(TEST_FOLDER)
                .then().statusCode(SC_OK)
                .body("type", equalTo("dir"));
    }

    @Test
    @DisplayName("DELETE /v1/disk/trash/resources - очистка корзины возвращает 202 или 204")
    void testClearTrash() {
        client.clearTrash()
                .then().statusCode(anyOf(equalTo(SC_ACCEPTED), equalTo(SC_NO_CONTENT)));
    }
}