package com.arianit;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class AppointmentController implements Serializable {

    private LocalDate date;
    private List<Appointment> appointments = new ArrayList<>();
    private Appointment selectedAppointment;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    Long id;



    @PostConstruct
    public void init() {
        id = 0L;
        date = LocalDate.now();
        LocalTime now = LocalTime.parse(LocalTime.now().format(dtf));
        LocalTime after1Hour = now.plusHours(1);
        Appointment appointment = new Appointment(id++,"Arzt","Arzt Termin", "Remscheid", date, now, after1Hour);
        Appointment appointment1 = new Appointment(id++,"Service","Service Termin", "Remscheid", date, now, after1Hour);
        Appointment appointment2 = new Appointment(id++,"Barber","Barber Termin", "Wuppertal", date.plusDays(1), now, after1Hour);
        Appointment appointment3 = new Appointment(id++,"Car Service","Car Service Termin", "Wuppertal", date.plusDays(1), now, after1Hour);

        appointments.add(appointment);
        appointments.add(appointment1);
        appointments.add(appointment2);
        appointments.add(appointment3);
    }

    public void onDateSelect(SelectEvent<LocalDate> event) {
        LocalDate selectedDate = event.getObject();
        setDate(selectedDate);

    }

    public List<Appointment> getAppointmentsForDate() {
        return appointments.stream().filter(appointment -> appointment.date.equals(date)).toList();
    }

    public void openNew() {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        selectedAppointment = appointment;
    }

    public void saveAppointment() {
        if (selectedAppointment.id == null) {
            selectedAppointment.setId(id++);
            appointments.add(selectedAppointment);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Appointment Added"));
        }
        else
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Appointment Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageAppointmentDialog').hide()");
        PrimeFaces.current().ajax().update("form:msgs", "form:appointmentsTable");
    }

    public void deleteAppointment() {
        appointments.remove(selectedAppointment);
        selectedAppointment = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Appointment Removed"));
        PrimeFaces.current().ajax().update("form:msgs", "form:appointmentsTable");
    }

}
