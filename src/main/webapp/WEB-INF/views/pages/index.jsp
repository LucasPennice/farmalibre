<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div>
  <div class="topbar">
    <div class="search-wrapper">
      <img src="assets/images/lupa.png" />
      <input
        type="text"
        placeholder="Buscar droga por nombre, composición, o categoría"
      />
    </div>

    <a href="/carrito" class="cart">
      <img src="assets/images/carrito.png" />
      <p class="badge">5</p>
    </a>

    <div class="foto_farmacia">
      <img src="" />
    </div>

    <p class="farmacia_nombre">Droguería San Marcos</p>
  </div>

  <div class="drogas-grid">
    <c:forEach var="d" items="${drogaDTOs}">
      <div class="droga-card">
        <div class="droga-main">
          <div class="formula">${d.formula}<span>2</span></div>

          <div class="nombre">${d.nombre}</div>
        </div>
      </div>
    </c:forEach>
  </div>
</div>
