package com.arianit;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Appointment {
    public Long id;
    public String title;
    public String description;
    public String location;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

}
