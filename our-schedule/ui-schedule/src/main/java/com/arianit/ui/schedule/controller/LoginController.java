package com.arianit.ui.schedule.controller;

import com.arianit.ui.schedule.api.ApiException;
import com.arianit.ui.schedule.api.UsersApi;
import com.arianit.ui.schedule.model.User;
import com.arianit.ui.schedule.util.Util;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Named
@SessionScoped
@Getter
@Setter
public class LoginController implements Serializable {

    @RestClient
    UsersApi api;
    @Inject
    Logger logger;
    List<User> users;
    private String username;
    private String password;

    @PostConstruct
    public void init() {
        try {
            users = api.apiUsersGet();
        } catch (ApiException e) {
            logger.error("init() =>" + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not get appointments!"));
        }
    }

    public String login() {
        requireNonNull(username);
        requireNonNull(password);
        requireNonNull(users);

        if (!users.isEmpty()) {
            Optional<User> currentUserOpt = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
            if (currentUserOpt.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Login failed!",
                        "Username is incorrect!"));
                return "login";
            }
            User currentUser = currentUserOpt.get();
            if (currentUser.getPassword().equals(password)) {
                HttpSession session = Util.getSession();
                session.setAttribute("username", username);
                session.setAttribute("user", currentUser);
                session.setAttribute("userId", currentUser.getId());
                return "appointment";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Login failed!",
                        "Wrong password!"));
                return "login";
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Login failed!",
                "The username or password is incorrect!"));
        return "login";
    }

    public String logout() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "login";
    }
}
