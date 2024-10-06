package com.arianit.ui.schedule.util;

import com.arianit.ui.schedule.api.ApiException;
import com.arianit.ui.schedule.api.UsersApi;
import com.arianit.ui.schedule.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;

@FacesValidator("usernameValidator")
public class UsernameValidator implements Validator {

    @RestClient
    UsersApi api;

    @Inject
    Logger logger;
    List<User> users;

    @PostConstruct
    public void init() {
        try {
            users = api.apiUsersGet();
        } catch (ApiException e) {
            logger.error("init() =>" + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not get users!"));
        }
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String username = (String) o;
        if (username != null && usernameExists(username)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already exists!", ""));
        }
    }

    private boolean usernameExists(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }
}
