package ru.yandex.disk.tests;

import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("DELETE запросы")
public class DeleteTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/DeleteTestFolder";

    @BeforeEach
    void setUp() {
        client.createFolder(TEST_FOLDER);
    }

    @AfterEach
    void tearDown() {
        client.deleteFolder(TEST_FOLDER);
    }

    @Test
    @DisplayName("DELETE /v1/disk/resources — удаление существующей папки возвращает 204")
    void testDeleteExistingFolder() {
        Response response = client.deleteFolder(TEST_FOLDER);

        assertThat("Статус должен быть 204",
                response.statusCode(), equalTo(204));
    }

    @Test
    @DisplayName("DELETE /v1/disk/resources — удаление несуществующего ресурса возвращает 404")
    void testDeleteNonExistingFolder() {
        Response response = client.deleteFolder("disk:/123");

        assertThat("Статус должен быть 404",
                response.statusCode(), equalTo(404));
    }
}