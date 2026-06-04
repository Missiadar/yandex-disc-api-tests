package ru.yandex.disk.config;

import io.github.cdimascio.dotenv.Dotenv;

public class TestConfig {

    private static final Dotenv dotenv = Dotenv.configure()
            .load();

    public static final String TOKEN = dotenv.get("OAuth_TOKEN");
    public static final String BASE_URL = "https://cloud-api.yandex.net";
}