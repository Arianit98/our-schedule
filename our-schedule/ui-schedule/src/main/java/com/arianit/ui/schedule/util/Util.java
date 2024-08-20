package com.arianit.ui.schedule.util;

import com.arianit.ui.schedule.model.User;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Util {

    public static HttpSession getSession() {
        return (HttpSession)
                FacesContext.
                        getCurrentInstance().
                        getExternalContext().
                        getSession(false);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.
                getCurrentInstance().
                getExternalContext().getRequest();
    }

    public static Long getUserId() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (Long) session.getAttribute("userId");
    }

    public static User getUser() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (User) session.getAttribute("user");
    }
}
