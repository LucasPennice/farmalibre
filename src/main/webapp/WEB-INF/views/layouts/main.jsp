<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html lang="en">
  <!DOCTYPE html>
  <html>
    <head>
      <meta charset="UTF-8" />
      <title>${pageTitle}</title>
      <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/styles/global.css"
      />
    </head>
    <body style="display: flex; margin: 0">
      <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

      <c:if test="${not empty errores}">
        <div id="toast-container">
          <c:forEach items="${errores}" var="e">
            <div class="toast-error">${e}</div>
          </c:forEach>
        </div>
      </c:if>

      <script>
        setTimeout(() => {
          const t = document.getElementById("toast-container");
          if (t) t.remove();
        }, 4000);
      </script>

      <div class="content">
        <jsp:include page="${content}" />
      </div>
    </body>
  </html>
</html>
