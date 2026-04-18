package com.arianit.rest.schedule.resource;

import com.arianit.rest.schedule.entity.User;
import com.arianit.rest.schedule.service.UserService;
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
@Path("api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class UserResource {

    @Inject
    Logger logger;

    @Inject
    UserService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("admin")
    public Response getAllUsers() {
        List<User> users = service.findAllUsers();
        logger.info("Total number of Users " + users);
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getUser(@PathParam("id") Long id) {
        User user = service.findUserById(id);
        if (user != null) {
            logger.info("Found user " + user);
            return Response.ok(user).build();
        } else {
            logger.info("No user found with id " + id);
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/me")
    @RolesAllowed({"user", "admin"})
    public Response getCurrentUser() {
        String subjectId = jwt.getSubject();
        User user = User.find("subjectId", subjectId).firstResult();
        if (user == null) {
            logger.info("No user found with subjectId " + subjectId);
            throw new NotFoundException("User not found");
        }
        logger.info("Found current user " + user);
        return Response.ok(user).build();
    }

    @POST
    @RolesAllowed({"admin"})
    public Response createUser(@Valid User user, @Context UriInfo uriInfo) {
        user = service.persistUser(user);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(user.id));
        logger.info("New user created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @PUT
    @RolesAllowed({"user", "admin"})
    public Response updateUser(@Valid User user) {
        user = service.updateUser(user);
        logger.debug("user updated for id: " + user.id);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin"})
    public Response deleteUser(@PathParam("id") Long id) {
        service.deleteUser(id);
        logger.info("User deleted with " + id);
        return Response.noContent().build();
    }
}

