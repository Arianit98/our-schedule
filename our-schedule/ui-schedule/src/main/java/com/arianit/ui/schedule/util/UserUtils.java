package com.arianit.ui.schedule.util;

import com.arianit.ui.schedule.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;

@ApplicationScoped
public class UserUtils implements Serializable {

    public void handleFileUpload(FileUploadEvent event, User currentUser) {
        UploadedFile image = event.getFile();
        if (image != null) {
            currentUser.setImage(image.getContent());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Image Uploaded!"));
        }
    }
}
