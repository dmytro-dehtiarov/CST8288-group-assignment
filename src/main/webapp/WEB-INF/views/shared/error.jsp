<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h1>${requestScope['jakarta.servlet.error.status_code'] == 404 ? 'Page not found' : 'Something went wrong'}</h1>
<p>${requestScope['jakarta.servlet.error.message']}</p>
<p><a href="${pageContext.request.contextPath}/app/">Back to home</a></p>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>