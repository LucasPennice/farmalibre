<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<nav class="navbar" style="position: sticky; top: 0; z-index: 1000;">
  <a href="/farmalibre">
    <img src="${pageContext.request.contextPath}/assets/images/Logo.png" class="logo" />
  </a>

  <div class="contenedor_categorias">
    <h1>Categorías</h1>

    <c:forEach items="${categoriasAprobadas}" var="c">
      <c:set var="activeId" value="${param.categoriaId}" />
      <a
        href="${pageContext.request.contextPath}/?categoriaId=${c.id}"
        class="categoria-link ${c.id == activeId ? 'categoria-link-activo' : ''}"
      >
        ${c.nombre}
      </a>
    </c:forEach>
  </div>

  <div style="flex: 1"></div>

  <div class="contenedor_categorias">
    <c:if test="${not empty usuario}">
      <h1>Acciones Rápidas</h1>

      <c:if test="${not usuario.esProveedor}">
        <a href="${pageContext.request.contextPath}/onboarding_proveedor" class="contenedor_acciones_rapidas">
          <div class="contenedor_acciones_rapidas_icono">
            <img src="${pageContext.request.contextPath}/assets/images/acciones.png" class="accion_rapida_icono" />
          </div>

          <p>Hacerme Proveedor</p>
        </a>
      </c:if>
  
      <c:if test="${usuario.esProveedor}">
        <a href="${pageContext.request.contextPath}/inventario" class="contenedor_acciones_rapidas">
          <div class="contenedor_acciones_rapidas_icono">
            <img src="${pageContext.request.contextPath}/assets/images/acciones.png" class="accion_rapida_icono" />
          </div>

          <p>Actualizar Inventario</p>
        </a>
      </c:if>
    
      <c:if test="${usuario.esAdmin}">
        <a href="${pageContext.request.contextPath}/administrar-categorias" class="contenedor_acciones_rapidas">
          <div class="contenedor_acciones_rapidas_icono">
            <img src="${pageContext.request.contextPath}/assets/images/acciones.png" class="accion_rapida_icono" />
          </div>

          <p>Administrar Categorías</p>

          <div class="aprobar_categorias_pendientes_bubble">${cantidadCategoriasPendientes}</div>
        </a>
      </c:if>
    </c:if>


  </div>

  <div style="flex: 1"></div>

  <c:if test="${not empty usuario and not empty usuario.wishlist}">
    <div class="contenedor_categorias">
      <h1>Wishlist</h1>
      <c:forEach var="item" items="${usuario.wishlist}">
        <a href="${pageContext.request.contextPath}/comprar-droga?drogaId=${item.drogaId}" class="contenedor_acciones_rapidas">
          <div class="contenedor_acciones_rapidas_icono" style="background: ${item.tieneDisponibilidad ? '#2e7d32' : '#c62828'}; border-radius: 50%; width: 28px; height: 28px; display: flex; align-items: center; justify-content: center;">
            <span style="color: white; font-size: 10px; font-weight: bold;">${item.inicialesProveedor}</span>
          </div>
          <p>${item.nombreDroga}</p>
        </a>
      </c:forEach>
    </div>
  </c:if>

  <div class="auth-actions">
    <c:choose>
      <c:when test="${not empty sessionScope.usuario_id}">
        <a href="${pageContext.request.contextPath}/auth/do-logout" class="register-link">
          <img src="${pageContext.request.contextPath}/assets/images/logout.png" class="accion_rapida_icono" />
          Cerrar Sesión
        </a>
      </c:when>
      <c:otherwise>
        <a href="${pageContext.request.contextPath}/auth/login" class="login-btn">
          <img src="${pageContext.request.contextPath}/assets/images/login.png" class="accion_rapida_icono" />
          Iniciar Sesión
        </a>
        <a class="register-link" href="${pageContext.request.contextPath}/auth/register">Crear Cuenta</a>
      </c:otherwise>
    </c:choose>
  </div>
</nav>
