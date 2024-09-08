package com.arianit.ui.schedule.controller;

import com.arianit.ui.schedule.api.ApiException;
import com.arianit.ui.schedule.api.AppointmentsApi;
import com.arianit.ui.schedule.model.Appointment;
import com.arianit.ui.schedule.model.User;
import com.arianit.ui.schedule.util.Util;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
@Getter
@Setter
public class AppointmentController implements Serializable {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    @RestClient
    AppointmentsApi api;
    @Inject
    Logger logger;
    private LocalDate selectedDate;
    private List<Appointment> appointments = new ArrayList<>();
    private Appointment selectedAppointment;
    private User currentUser;


    @PostConstruct
    public void init() {
        if (Util.getUser() != null) {
            currentUser = Util.getUser();
        }
        selectedDate = LocalDate.now();
        try {
            appointments = api.apiAppointmentsGet().stream().filter(appointment -> appointment.getUser().getId().equals(currentUser.getId())).collect(Collectors.toList());
        } catch (ApiException e) {
            logger.error("init() =>" + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not get appointments!"));
        }
    }

    public void onDateSelect(SelectEvent<LocalDate> event) {
        LocalDate selectedDate = event.getObject();
        setSelectedDate(selectedDate);
    }

    public List<Appointment> getAppointmentsForDate() {
        return appointments.stream().filter(appointment -> appointment.getDate().equals(selectedDate)).toList();
    }

    public void openNew() {
        Appointment appointment = new Appointment();
        appointment.setDate(selectedDate);
        appointment.setUser(currentUser);
        selectedAppointment = appointment;
    }

    public void saveAppointment() {
        if (selectedAppointment.getId() == null) {
            try {
                api.apiAppointmentsPost(selectedAppointment);
                appointments = api.apiAppointmentsGet().stream().filter(appointment -> appointment.getUser().getId().equals(currentUser.getId())).collect(Collectors.toList());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Appointment Added"));
            } catch (ApiException e) {
                logger.error("saveAppointment() =>" + e);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not add appointment!"));
            }
        } else {
            try {
                api.apiAppointmentsPut(selectedAppointment);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Appointment Updated"));
            } catch (ApiException e) {
                logger.error("saveAppointment() =>" + e);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not update appointment!"));
            }
        }
        PrimeFaces.current().executeScript("PF('manageAppointmentDialog').hide()");
        PrimeFaces.current().ajax().update("form:msgs", "form:appointmentsTable");
    }

    public void deleteAppointment() {
        try {
            api.apiAppointmentsIdDelete(selectedAppointment.getId());
            appointments.remove(selectedAppointment);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Appointment Removed"));
        } catch (ApiException e) {
            logger.error("deleteAppointment() =>" + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not delete appointment!"));
        }
        selectedAppointment = null;
        PrimeFaces.current().ajax().update("form:msgs", "form:appointmentsTable");
    }

    public String getCurrentDateAsLabel() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd MMMM yyyy");
        return dtf.format(selectedDate);
    }
}
