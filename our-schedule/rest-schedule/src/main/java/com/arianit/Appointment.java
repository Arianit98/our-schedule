package com.arianit;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
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
}
