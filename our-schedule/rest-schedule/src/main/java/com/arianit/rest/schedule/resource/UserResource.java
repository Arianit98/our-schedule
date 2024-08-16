package com.arianit.rest.schedule.resource;

import com.arianit.rest.schedule.model.User;
import com.arianit.rest.schedule.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@Path("api/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    Logger logger;

    @Inject
    UserService service;

    @GET
    public Response getAllUsers() {
        List<User> users = service.findAllUsers();
        logger.debug("Total number of Users " + users);
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        User user = service.findUserById(id);
        if (user != null) {
            logger.debug("Found user " + user);
            return Response.ok(user).build();
        } else {
            logger.debug("No user found with id " + id);
            return Response.noContent().build();
        }
    }

    @POST
    public Response createUser(@Valid User user, @Context UriInfo uriInfo) {
        user = service.persistUser(user);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(user.id));
        logger.debug("New user created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @PUT
    public Response updateUser(@Valid User user) {
        user = service.updateUser(user);
        logger.debug("user updated with new value " + user);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        service.deleteUser(id);
        logger.debug("User deleted with " + id);
        return Response.noContent().build();
    }
}

