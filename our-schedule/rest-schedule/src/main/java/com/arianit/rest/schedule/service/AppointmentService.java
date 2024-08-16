package com.arianit.rest.schedule.service;

import com.arianit.rest.schedule.model.Appointment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class AppointmentService {

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Appointment> findAllAppointments() {
        return Appointment.listAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Appointment findAppointmentById(Long id) {
        return Appointment.findById(id);
    }

    public Appointment persistAppointment(@Valid Appointment appointment) {
        appointment.persist();
        return appointment;
    }

    public Appointment updateAppointment(@Valid Appointment appointment) {
        Appointment entity = Appointment.findById(appointment.id);
        entity.title = appointment.title;
        entity.description = appointment.description;
        entity.date = appointment.date;
        entity.startTime = appointment.startTime;
        entity.endTime = appointment.endTime;
        return entity;
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = Appointment.findById(id);
        appointment.delete();
    }
}
