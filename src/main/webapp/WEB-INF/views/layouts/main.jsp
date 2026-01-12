<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
  <head>
    <title>${pageTitle}</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/styles/global.css"
    />
  </head>
  <body style="display: flex; margin: 0">
    <!-- Navbar (componente) -->
    <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

    <div class="content">
      <jsp:include page="${content}" />
    </div>
  </body>
</html>
