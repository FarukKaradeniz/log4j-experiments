package com.farukkaradeniz.log4jexperiments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "LOG_EVENT")
@Getter
@Setter
public class MyLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "event_time")
    private OffsetDateTime eventTime;

    @Column(name = "level")
    private String level;

    @Column(name = "logger_name")
    private String loggerName;

    @Column(name = "message")
    private String message;
}
