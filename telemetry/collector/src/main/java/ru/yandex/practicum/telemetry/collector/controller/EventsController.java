package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventsController {

    private Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private Map<HubEventType, HubEventHandler> hubEventHandlers;

    @Autowired
    public EventsController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getEventType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getEventType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void postSensorEvent(@RequestBody @Valid SensorEvent event) {
        SensorEventHandler handler = sensorEventHandlers.get(event.getType());
        if (handler == null) {
            throw new IllegalArgumentException("No handler for " + event.getType());
        }
        handler.handle(event);
    }

    @PostMapping("/hubs")
    public void postHubEvent(@RequestBody @Valid HubEvent event) {
        HubEventHandler handler = hubEventHandlers.get(event.getType());
        if (handler == null) {
            throw new IllegalArgumentException("No handler for " + event.getType());
        }
        handler.handle(event);
    }
}
