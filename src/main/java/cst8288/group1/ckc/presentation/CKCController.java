package cst8288.group1.ckc.presentation;

import cst8288.group1.ckc.dataaccess.DaoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The application's controller: every request under {@code /app} comes here. It finds the
 * action for the request, runs it, then forwards to a JSP or redirects. It holds no business logic.
 *
 * @author Dmytro Dehtiarov
 */
public class CKCController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(CKCController.class.getName());
    private static final String REDIRECT = "redirect:";
    private static final String VIEWS = "/WEB-INF/views/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        dispatch(req, resp);
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Action action = Routes.find(req.getMethod(), req.getPathInfo());
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String result;
        try {
            result = action.execute(req, resp);
        } catch (DaoException e) {
            // Logs real cause
            LOG.log(Level.SEVERE, "Database error on " + req.getMethod() + " " + req.getPathInfo(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error, please try again");
            return;
        }

        if (result == null) {
            return;
        }
        if (result.startsWith(REDIRECT)) {
            resp.sendRedirect(req.getContextPath() + result.substring(REDIRECT.length()));
        } else {
            req.getRequestDispatcher(VIEWS + result + ".jsp").forward(req, resp);
        }
    }
}