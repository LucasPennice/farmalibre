<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<nav class="auth-container">
  <h1 class="brand">Farma<span>Libre</span></h1>

  <div class="categories">
    <h3>Categorías</h3>

    <ul>
      <c:forEach items="${categorias}" var="c">
        <li>${c.nombre}</li>
      </c:forEach>
    </ul>
  </div>

  <div class="auth-actions">
    <button class="login-btn">Iniciar Sesión</button>
    <a class="register-link" href="#">Crear Cuenta</a>
  </div>
</nav>
