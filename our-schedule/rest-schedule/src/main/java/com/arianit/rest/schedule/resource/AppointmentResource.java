package com.arianit.rest.schedule.resource;

import com.arianit.rest.schedule.model.Appointment;
import com.arianit.rest.schedule.service.AppointmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@Path("api/appointments")
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentResource {

    @Inject
    Logger logger;

    @Inject
    AppointmentService service;

    @GET
    public Response getAllAppointments() {
        List<Appointment> appointments = service.findAllAppointments();
        logger.debug("Total number of Appointments " + appointments);
        return Response.ok(appointments).build();
    }

    @GET
    @Path("/{id}")
    public Response getAppointment(@PathParam("id") Long id) {
        Appointment appointment = service.findAppointmentById(id);
        if (appointment != null) {
            logger.debug("Found appointment " + appointment);
            return Response.ok(appointment).build();
        } else {
            logger.debug("No appointment found with id " + id);
            return Response.noContent().build();
        }
    }

    @POST
    public Response createAppointment(@Valid Appointment appointment, @Context UriInfo uriInfo) {
        appointment = service.persistAppointment(appointment);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(appointment.id));
        logger.debug("New appointment created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @PUT
    public Response updateAppointment(@Valid Appointment appointment) {
        appointment = service.updateAppointment(appointment);
        logger.debug("appointment updated with new value " + appointment);
        return Response.ok(appointment).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAppointment(@PathParam("id") Long id) {
        service.deleteAppointment(id);
        logger.debug("Appointment deleted with " + id);
        return Response.noContent().build();
    }
}
