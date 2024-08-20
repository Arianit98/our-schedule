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
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.ArrayList;
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
    List<User> users = new ArrayList<>();
    private String username;
    private String password;
    private User selectedUser;

    @PostConstruct
    public void init() {
        try {
            users = api.apiUsersGet();
        } catch (ApiException e) {
            logger.error("init() =>" + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not get users!"));
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
                        "There is no user with this username!"));
                PrimeFaces.current().ajax().update("form:msgs");
                return "/login.xhtml";
            }
            User currentUser = currentUserOpt.get();
            if (currentUser.getPassword().equals(password)) {
                HttpSession session = Util.getSession();
                session.setAttribute("user", currentUser);
                session.setAttribute("userId", currentUser.getId());
                return "/appointment.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Login failed!",
                        "Wrong password!"));
                PrimeFaces.current().ajax().update("form:msgs");
                return "/login.xhtml";
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Login failed!",
                "The username or password is incorrect!"));
        return "/login.xhtml";
    }

    public String logout() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "/login.xhtml?faces-redirect=true";
    }

    public void openNew() {
        User user = new User();
        user.setActive(true);
        selectedUser = user;
    }

    public void saveUser() {
        if (selectedUser.getId() == null) {
            try {
                api.apiUsersPost(selectedUser);
                users.add(selectedUser);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Added"));
            } catch (ApiException e) {
                logger.error("saveUser() =>" + e);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not add user!"));
            }
        } else {
            try {
                api.apiUsersPut(selectedUser);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Updated"));
            } catch (ApiException e) {
                logger.error("saveUser() =>" + e);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not update user!"));
            }
        }
        PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
        PrimeFaces.current().ajax().update("form:msgs");
    }
}
