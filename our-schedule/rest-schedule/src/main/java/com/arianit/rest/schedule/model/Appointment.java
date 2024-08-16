package com.arianit.rest.schedule.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Appointment extends PanacheEntity {
    public String title;
    public String description;
    public String location;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    public User user;
}
