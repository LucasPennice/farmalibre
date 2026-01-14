<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<nav class="navbar">
  <a href="/farmalibre">
    <img src="${pageContext.request.contextPath}/assets/images/Logo.png" class="logo" />
  </a>

  <div class="contenedor_categorias">
    <h1>Categorías</h1>

    <c:forEach items="${categorias}" var="c">
      <c:set var="activeId" value="${param.categoriaId}" />
      <a
        href="?categoriaId=${c.id}"
        class="categoria-link ${c.id == activeId ? 'categoria-link-activo' : ''}"
      >
        ${c.nombre}
      </a>
    </c:forEach>
  </div>

  <div style="flex: 1"></div>

  <div class="contenedor_categorias">
    <h1>Acciones Rápidas</h1>

    <a href="" class="contenedor_acciones_rapidas">
      <div class="contenedor_acciones_rapidas_icono">
        <img src="${pageContext.request.contextPath}/assets/images/acciones.png" class="accion_rapida_icono" />
      </div>

      <p>Hacerme Proveedor</p>

      <div class="aprobar_categorias_pendientes_bubble">5</div>
    </a>
  </div>

  <div style="flex: 1"></div>

  <div class="auth-actions">
    <button class="login-btn">Iniciar Sesión</button>
    <a class="register-link" href="#">Crear Cuenta</a>
  </div>
</nav>
