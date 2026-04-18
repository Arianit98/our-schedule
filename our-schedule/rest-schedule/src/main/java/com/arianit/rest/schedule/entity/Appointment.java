package com.arianit.rest.schedule.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Appointment.findAppointment", query = "SELECT a FROM Appointment a WHERE a.id in :appointmentId and (a.user.id in :userId or a.user.id in (SELECT c.id FROM User u JOIN u.connections c WHERE u.id = :userID))"),
        @NamedQuery(name = "Appointment.findAppointments", query = "SELECT a FROM Appointment a WHERE a.user.id in :userID or a.user.id in (SELECT c.id FROM User u JOIN u.connections c WHERE u.id = :userID)")})
public class Appointment extends PanacheEntity {
    public String title;
    public String description;
    public String location;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public static Appointment findAppointment(Long appointmentId, Long userId) {
        return find("#Appointment.findAppointment", Map.of("appointmentId", appointmentId, "userId", userId)).firstResult();
    }

    public static List<Appointment> findAppointments(Long userId) {
        return find("#Appointment.findAppointments", Map.of("userId", userId)).list();
    }
}
