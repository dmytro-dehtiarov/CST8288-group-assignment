package cst8288.group1.ckc.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * TEMPORARY: proves that CI compiles the project and runs JUnit. Delete together with SmokeServlet.
 *
 * @author Dmytro Dehtiarov
 */
class SmokeServletTest {

    @Test
    void statusMessageNamesTheServer() {
        assertEquals("CKC is running on Payara Server", SmokeServlet.statusMessage("Payara Server"));
    }
}
