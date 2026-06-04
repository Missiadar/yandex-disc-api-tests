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
    void setUp() {
        client.createFolder(TEST_FOLDER);
    }

    @AfterEach
    void tearDown() {
        client.deleteFolder(TEST_FOLDER);
    }

    @Test
    @DisplayName("GET /v1/disk - информация о диске возвращает 200")
    void testGetDiskInfo() {
        Response response = client.getDiskInfo();

        assertThat("Статус должен быть 200",
                response.statusCode(), equalTo(200));
        assertThat("Поле facebook должно быть строкой",
                response.jsonPath().get("system_folders.vkontakte"), instanceOf(String.class));
    }

    @Test
    @DisplayName("GET /v1/disk/resources - существующая папка возвращает 200 и тип dir")
    void testGetExistingFolder() {
        Response response = client.getResource(TEST_FOLDER);

        assertThat("Статус должен быть 200",
                response.statusCode(), equalTo(200));
        assertThat("Тип ресурса должен быть dir",
                response.jsonPath().getString("type"), equalTo("dir"));
    }

    @Test
    @DisplayName("GET /v1/disk/resources - несуществующий путь возвращает 404")
    void testGetNonExistentResource() {
        Response response = client.getResource("disk:/123");

        assertThat("Статус должен быть 404",
                response.statusCode(), equalTo(404));
    }
}