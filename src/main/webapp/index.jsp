<%-- TEMPORARY: sends the root URL to the smoke page. Replace with the real home or login page. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% response.sendRedirect(request.getContextPath() + "/smoke"); %>
