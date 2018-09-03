package com.elesson.pioneer.web.servlet;

import com.elesson.pioneer.dao.exception.DBException;
import com.elesson.pioneer.dao.exception.DuplicateEntityException;
import com.elesson.pioneer.model.User;
import com.elesson.pioneer.service.ServiceFactory;
import com.elesson.pioneer.service.UserService;
import com.elesson.pioneer.web.util.UserDataValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.elesson.pioneer.web.util.Constants.*;

/**
 * The {@code RegistrationServlet} class purpose is to allow user to register a new account.
 * This class perform validation of entered data before storing to database.
 * Invalidates the session and creates new one. After that stores user data into session.
 * Redirects to error page if some exception comes from DAO layer.
 */
public class RegistrationServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RegistrationServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(REGISTRATION_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String regName = req.getParameter(A_REG_NAME);
        String regEmail = req.getParameter(A_REG_EMAIL);
        String regPassword = req.getParameter(A_REG_PASS);
        String confPassword = req.getParameter(A_REG_PASS2);

        if(UserDataValidation.hasError(req, regName, regEmail, regPassword, confPassword)) {
            req.getRequestDispatcher(REGISTRATION_JSP).forward(req, resp);
        } else {
            try {
                UserService userService = ServiceFactory.getUserService();
                User user = new User(regName, regEmail, regPassword, User.Role.CLIENT);
                userService.create(user);
                HttpSession session = req.getSession();
                session.invalidate();
                session = req.getSession();
                session.setAttribute(A_AUTH_USER, user);
                logger.info("{} logged in", user.getName());
                resp.sendRedirect(SCHEDULE);
            } catch (DuplicateEntityException de) {
                logger.warn(de.getMessage());
                req.setAttribute(A_DUPLICATE, true);
                req.getRequestDispatcher(REGISTRATION_JSP).forward(req, resp);
            } catch (DBException e) {
                resp.setStatus(500);
            }
        }
    }
}
