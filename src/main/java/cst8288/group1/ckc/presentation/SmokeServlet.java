package cst8288.group1.ckc.presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TEMPORARY smoke check: proves that the WAR deploys on Payara and that a servlet can forward to a JSP.
 * Delete this class, its mapping in web.xml, WEB-INF/views/smoke.jsp, index.jsp and SmokeServletTest
 * once the first real feature is in place.
 *
 * @author Dmytro Dehtiarov
 */
public class SmokeServlet extends HttpServlet {

    /**
     * Builds the status line shown on the smoke page.
     *
     * @param serverInfo the server name and version reported by the servlet container
     * @return a one-line status message
     */
    static String statusMessage(String serverInfo) {
        return "CKC is running on " + serverInfo;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("status", statusMessage(getServletContext().getServerInfo()));
        req.getRequestDispatcher("/WEB-INF/views/smoke.jsp").forward(req, resp);
    }
}
