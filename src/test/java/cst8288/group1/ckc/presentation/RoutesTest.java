package cst8288.group1.ckc.presentation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Routes}.
 *
 * @author Dmytro Dehtiarov
 */
class RoutesTest {

    @Test
    void rootRouteExists() {
        assertNotNull(Routes.find("GET", "/"));
    }

    @Test
    void missingPathInfoMeansRoot() {
        assertNotNull(Routes.find("GET", null));
    }

    @Test
    void unknownRouteIsNotFound() {
        assertNull(Routes.find("GET", "/something"));
    }

    @Test
    void methodIsPartOfTheRoute() {
        assertNull(Routes.find("POST", "/"));
    }
}