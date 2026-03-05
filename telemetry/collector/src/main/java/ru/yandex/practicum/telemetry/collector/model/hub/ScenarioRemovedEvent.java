package ru.yandex.practicum.telemetry.collector.model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {

    @NotNull
    @Length(min = 3)
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
