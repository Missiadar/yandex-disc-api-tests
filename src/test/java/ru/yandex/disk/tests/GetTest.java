package ru.yandex.disk.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ru.yandex.disk.api.DiskApiClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("GET запросы")
public class GetTest {

    private static final DiskApiClient client = new DiskApiClient();
    private static final String TEST_FOLDER = "disk:/GetTestFolder";

    @BeforeEach
    void creating() {
        client.createFolder(TEST_FOLDER);
    }

    @AfterEach
    void deleting() {
        client.deleteFolder(TEST_FOLDER);
    }

    @Test
    @DisplayName("GET /v1/disk - информация о диске возвращает 200, а объект возвращает строку")
    void testGetDiskInfo() {
        Response response = client.getDiskInfo();
        assertThat("Статус должен быть 200",
                response.statusCode(), equalTo(200));
        assertThat("Поле facebook должно быть строкой",
                response.jsonPath().get("system_folders.facebook"), instanceOf(String.class));
    }

    @Test
    @DisplayName("GET /v1/disk/resources - без токена возвращает 401")
    void testGetFolderWithoutToken() {
        Response response = client.getResourcesInfo(TEST_FOLDER);
        assertThat("Статус должен быть 401",
                response.statusCode(), equalTo(401));
    }

    @Test
    @DisplayName("GET /v1/disk/trash/resources - некорректный запрос возвращает 400")
    void testGetBadRequest() {
        Response response = client.getTrashInfo("disk:/123");
        assertThat("Статус должен быть 400",
                response.statusCode(), equalTo(400));
    }
}