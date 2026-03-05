package ru.yandex.practicum.telemetry.collector.service.hub;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;

@RequiredArgsConstructor
public abstract class HubEventHandler {
    protected final String topic;
    protected final CollectorClient client;

    public abstract void handle(HubEvent event);

    public abstract HubEventType getEventType();
}
