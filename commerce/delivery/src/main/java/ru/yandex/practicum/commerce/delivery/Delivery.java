package ru.yandex.practicum.commerce.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.contract")
@ComponentScan(basePackages = {"ru.yandex.practicum.commerce.aspect", "ru.yandex.practicum.commerce.delivery"})
public class Delivery {
    public static void main(String[] args) {
        SpringApplication.run(Delivery.class, args);
    }
}
