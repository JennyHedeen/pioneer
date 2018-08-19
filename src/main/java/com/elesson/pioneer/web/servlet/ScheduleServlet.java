package com.elesson.pioneer.web.servlet;


import com.elesson.pioneer.model.User;
import com.elesson.pioneer.service.EventService;
import com.elesson.pioneer.service.EventServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * The {@code ScheduleServlet} class purpose is to form a list of {@code Appointment} class objects.
 * Resulting collection depends on attributes provided in URL.
 * Possible URL attributes:
 *      action=my - records for authorized user will be returned
 *      action=date&date=somedate - somedate will be parsed to LocalDate and the records for specified
 *          data will be returned. It is also required that masterId to be stored in session.
 *          If masterId is null you will be redirected to the page with list of masters.
 * Redirects to error page if some exception comes from DAO layer.
 */
public class ScheduleServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ScheduleServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        EventService service = EventServiceImpl.getEventService();

        String date = req.getParameter("date");
        LocalDate localDate = date==null ? LocalDate.now() : LocalDate.parse(date);
        localDate = localDate.isBefore(LocalDate.now()) ? LocalDate.now() : localDate;

        HttpSession session = req.getSession();
        User authUser = (User)session.getAttribute("authUser");

        req.setAttribute("events", service.getEvents(localDate));
        req.setAttribute("nextWeek", getNextWeek());
        req.setAttribute("date", localDate);

        req.getRequestDispatcher("jsp/schedule.jsp").forward(req, resp);
    }

    private List<LocalDate> getNextWeek() {
        List<LocalDate> nextWeek = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            nextWeek.add(LocalDate.now().plusDays(i));
        }
        return nextWeek;
    }

}