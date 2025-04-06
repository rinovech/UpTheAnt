package com.uptheant.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
@Tag(name = "System API", description = "Операции для работы с системой")
public class SystemController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version:1.0.0}")
    private String appVersion;

    @Operation(summary = "Получить имя приложения", description = "Возвращает название сервиса")
    @GetMapping("/app-name")
    public String getAppName() {
        return appName;
    }

    @Operation(summary = "Получить версию приложения")
    @GetMapping("/app-version")
    public String getAppVersion() {
        return appVersion;
    }

    @Operation(summary = "Статус работы сервиса")
    @GetMapping("/status")
    public String getStatus() {
        return "Service is up and running";
    }
}