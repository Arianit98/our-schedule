package com.arianit.rest.schedule.service;

import com.arianit.rest.schedule.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class UserService {

    @Inject
    Keycloak keycloak;

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> findAllUsers() {
        return User.listAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUserById(Long id) {
        return User.findById(id);
    }

    public User persistUser(@Valid User user) {
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(user.getUsername());
        keycloakUser.setEmail(user.getEmail());
        keycloakUser.setFirstName(user.getName());
        keycloakUser.setLastName(user.getSurname());
        keycloakUser.setRealmRoles(List.of("user"));
        keycloakUser.setRequiredActions(List.of("UPDATE_PASSWORD"));
        Response response;
        try {
            response = keycloak.realm("schedule").users().create(keycloakUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user in Keycloak: " + e.getMessage(), e);
        }
        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }
        keycloakUser = (UserRepresentation) response.getEntity();
        user.setSubjectId(keycloakUser.getId());
        user.persist();
        return user;
    }

    public User updateUser(@Valid User user) {
        User entity = User.findById(user.id);
        if (entity == null) {
            throw new RuntimeException("Failed to find user: " + user.id);
        }
        UserResource keycloakUser = keycloak.realm("schedule").users().get(entity.getSubjectId());
        UserRepresentation userRepresentation = keycloakUser.toRepresentation();
        if (userRepresentation == null) {
            throw new RuntimeException("Failed to find user in Keycloak: " + entity.getSubjectId());
        }

        keycloakUser.update(new UserRepresentation() {{
            if (!Objects.equals(user.getEmail(), userRepresentation.getEmail())) {
                setRequiredActions(List.of("CONFIRM_EMAIL"));
            }
            setUsername(user.getUsername());
            setEmail(user.getEmail());
            setFirstName(user.getName());
            setLastName(user.getSurname());
        }});
        entity.name = user.name;
        entity.surname = user.surname;
        entity.username = user.username;
        entity.email = user.email;
        entity.birthday = user.birthday;
        //TODO: check if user is updated
        return entity;
    }

    public void deleteUser(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }
        Response deleted;
        try {
            deleted = keycloak.realm("schedule").users().delete(user.getSubjectId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user in Keycloak: " + e.getMessage(), e);
        }
        if (deleted.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to delete user in Keycloak: " + deleted.getStatus());
        }
        user.delete();
    }
}
