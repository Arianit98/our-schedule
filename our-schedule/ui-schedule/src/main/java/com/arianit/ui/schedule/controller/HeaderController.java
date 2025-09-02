package com.arianit.ui.schedule.controller;

import com.arianit.ui.schedule.api.UsersApi;
import com.arianit.ui.schedule.model.User;
import com.arianit.ui.schedule.util.UserUtils;
import com.arianit.ui.schedule.util.Util;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;

import java.io.Serializable;

@Named
@SessionScoped
@Getter
@Setter
public class HeaderController implements Serializable {

    @RestClient
    UsersApi api;

    @Inject
    Logger logger;
    @Inject
    UserUtils userUtils;

    private User currentUser;

    @PostConstruct
    public void init() {
        if (Util.getUser() != null) {
            currentUser = Util.getUser();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        userUtils.handleFileUpload(event, currentUser);
    }

    public User getCurrentUser() {
        return Util.getUser();
    }
}
