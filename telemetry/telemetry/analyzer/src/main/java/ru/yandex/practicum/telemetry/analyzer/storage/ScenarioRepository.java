package ru.yandex.practicum.telemetry.analyzer.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findByHubId(String hubId);

    void deleteByHubIdAndName(String hubId, String name);
}
