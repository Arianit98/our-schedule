package com.arianit.rest.schedule.service;

import com.arianit.rest.schedule.entity.Appointment;
import com.arianit.rest.schedule.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class AppointmentService {

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Appointment> findAllAppointments(Long userId) {
        return Appointment.findAppointments(userId);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Appointment findAppointmentById(Long appointmentId, Long userId) {
        return Appointment.findAppointment(appointmentId, userId);
    }

    public Appointment persistAppointment(User user, @Valid Appointment appointment) {
        if (!user.equals(appointment.getUser())) {
            throw new BadRequestException("User ID in the appointment does not match the authenticated user.");
        }
        appointment.persist();
        return appointment;
    }

    public Appointment updateAppointment(User user, @Valid Appointment appointment) {
        Appointment entity = Appointment.findById(appointment.id);
        if (!user.equals(entity.getUser())) {
            throw new BadRequestException("User ID in the appointment does not match the authenticated user.");
        }
        entity.title = appointment.title;
        entity.description = appointment.description;
        entity.date = appointment.date;
        entity.startTime = appointment.startTime;
        entity.endTime = appointment.endTime;
        return entity;
    }

    public void deleteAppointment(User user, Long id) {
        Appointment appointment = Appointment.findById(id);
        if (!user.equals(appointment.getUser())) {
            throw new BadRequestException("User ID in the appointment does not match the authenticated user.");
        }
        appointment.delete();
    }
}
