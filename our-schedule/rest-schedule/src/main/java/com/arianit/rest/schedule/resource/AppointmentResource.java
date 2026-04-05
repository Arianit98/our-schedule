package com.arianit.rest.schedule.resource;

import com.arianit.rest.schedule.entity.Appointment;
import com.arianit.rest.schedule.entity.User;
import com.arianit.rest.schedule.service.AppointmentService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@Path("api/v1/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class AppointmentResource {

    @Inject
    Logger logger;

    @Inject
    AppointmentService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("user")
    public Response getAllAppointments() {
        List<Appointment> appointments = service.findAllAppointments(getCurrentUser().id);
        logger.debug("Total number of Appointments " + appointments);
        return Response.ok(appointments).build();
    }

    @GET
    @Path("{appointment_id}")
    @RolesAllowed("user")
    public Response getAppointment(@PathParam("appointment_id") Long appointmentId) {
        Appointment appointment = service.findAppointmentById(getCurrentUser().id, appointmentId);
        if (appointment != null) {
            logger.debug("Found appointment " + appointment);
            return Response.ok(appointment).build();
        } else {
            logger.debug("No appointment found with id " + appointmentId);
            return Response.noContent().build();
        }
    }

    @POST
    @RolesAllowed("user")
    public Response createAppointment(@Valid Appointment appointment, @Context UriInfo uriInfo) {
        appointment = service.persistAppointment(getCurrentUser(), appointment);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(appointment.id));
        logger.debug("New appointment created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @PUT
    @RolesAllowed("user")
    public Response updateAppointment(@Valid Appointment appointment) {
        appointment = service.updateAppointment(getCurrentUser(), appointment);
        logger.debug("appointment updated with new value " + appointment);
        return Response.ok(appointment).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    public Response deleteAppointment(@PathParam("id") Long id) {
        service.deleteAppointment(getCurrentUser(), id);
        logger.debug("Appointment deleted with " + id);
        return Response.noContent().build();
    }

    private User getCurrentUser() {
        String subjectId = jwt.getSubject();
        User user = User.find("subjectId", subjectId).firstResult();
        if (user == null)
            throw new NotFoundException("User not found");
        return user;
    }
}
