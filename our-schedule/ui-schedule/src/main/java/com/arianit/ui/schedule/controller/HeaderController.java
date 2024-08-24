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
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
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
    private User currentUser;

    @PostConstruct
    public void init() {
        if (Util.getUser() != null) {
            currentUser = Util.getUser();
        }
    }

    public String logout() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "/login.xhtml?faces-redirect=true";
    }

    public void saveUser() {
        try {
            api.apiUsersPut(currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Updated"));
        } catch (ApiException e) {
            logger.error("saveUser() =>" + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not update user!"));
        }

        PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
        PrimeFaces.current().ajax().update("form:msgs");
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile image = event.getFile();
        File file = new File("src/main/resources/META-INF/resources/images/" + image.getFileName());
        try {
            IOUtils.copy(image.getInputStream(), new FileOutputStream(file));
            currentUser.setImage(image.getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Image Uploaded!"));
        } catch (Exception e) {
            logger.error("handleFileUpload() =>" + e);
            throw new RuntimeException(e);
        }
    }
}
