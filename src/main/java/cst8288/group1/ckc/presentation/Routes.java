package cst8288.group1.ckc.presentation;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps an HTTP method and a path after {@code /app} to the action that handles it.
 * Each module adds its routes in its own block below.
 *
 * @author Dmytro Dehtiarov
 */
public final class Routes {

    private static final Map<String, Action> ROUTES = new HashMap<>();

    static {
        // ---- shared
        add("GET", "/", (req, resp) -> "home");

        // ---- user
        // ---- inventory
        // ---- ledger
        // ---- training
        // ---- equipment
        // ---- maintenance
    }

    private Routes() { }

    private static void add(String method, String path, Action action) {
        if (ROUTES.put(method + " " + path, action) != null) {
            // Fails at startup if there is duplicate route
            throw new IllegalStateException("Duplicate route: " + method + " " + path);
        }
    }

    /**
     * Finds the action for a request.
     *
     * @param method the HTTP method, for example {@code "GET"}
     * @param pathInfo the part of the URL after {@code /app}; {@code null} means the root
     * @return the action, or {@code null} when no route matches
     */
    public static Action find(String method, String pathInfo) {
        String path = (pathInfo == null || pathInfo.isEmpty()) ? "/" : pathInfo;
        return ROUTES.get(method + " " + path);
    }
}