package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "scenarios")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String hubId;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(table = "scenario_conditions",
            name = "sensor_id")
    @JoinTable(name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id"))
    @Builder.Default
    private Map<String, Condition> conditions = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(table = "scenario_actions",
            name = "sensor_id")
    @JoinTable(name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    @Builder.Default
    private Map<String, Action> actions = new HashMap<>();
}
