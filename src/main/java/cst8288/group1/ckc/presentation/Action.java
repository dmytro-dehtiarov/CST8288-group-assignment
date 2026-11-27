package cst8288.group1.ckc.presentation;

import cst8288.group1.ckc.dataaccess.DaoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles one route. The controller finds the action in {@link Routes} and runs it.
 *
 * @author Dmytro Dehtiarov
 */
@FunctionalInterface
public interface Action {
    /**
     * Handles the request and says what the controller should do next.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return a view name to forward to, such as {@code "equipment/list"} for
     *         {@code /WEB-INF/views/equipment/list.jsp}; or {@code "redirect:/app/equipment"} to redirect;
     *         or {@code null} if the action already wrote the response
     * @throws DaoException if the database fails
     * @throws ServletException if the servlet container fails
     * @throws IOException if writing the response fails
     */
    String execute(HttpServletRequest request, HttpServletResponse response)
        throws DaoException, ServletException, IOException;
}
