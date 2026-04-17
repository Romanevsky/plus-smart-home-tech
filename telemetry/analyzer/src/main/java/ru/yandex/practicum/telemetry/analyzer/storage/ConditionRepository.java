package ru.yandex.practicum.telemetry.analyzer.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
