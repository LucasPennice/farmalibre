<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div>
  <div class="topbar">
    <div class="search-wrapper">
      <form  action="${pageContext.request.contextPath}/do-filter" method="post">
        <img src="${pageContext.request.contextPath}/assets/images/lupa.png"/>
        <input
          type="text"
          name="filter"
          placeholder="Buscar droga por nombre, composición, o categoría"
        />
      </form>
    </div>
    <c:if test="${not empty sessionScope.usuario_id}">
      <a href="/farmalibre/carrito" class="cart">
        <img src="${pageContext.request.contextPath}/assets/images/carrito.png" />
        <p class="badge">5</p>
      </a>
    </c:if>

    <c:if test="${not empty sessionScope.usuario_id}">
      <a href="/farmalibre/perfil" class="perfil_index_contenedor anchor_remove_styles">
        <div class="foto_farmacia">
          <img src="${pageContext.request.contextPath}/usuario-foto?id=${usuario.id}" style="height: 100%; width: 100%; border-radius: 100px;" />
        </div>

        <p class="farmacia_nombre">${usuario.nombreCompletoRes}</p>
        </div>
      </a>
    </c:if>
  </div>

  <div class="drogas_list_container">
    <c:forEach var="d" items="${drogaDTOs}">
      <a href="/farmalibre/comprar-droga?drogaId=${d.idDroga}" class="drogas_container">
        <div class="drogas_left_container">
          <h1>${d.formula}</h1>
          <h2>${d.nombre}</h2>
          <div>
            <p>${d.stockTotal} ${d.unidad}</p>
            <p>${d.cantidadProveedores} Proveedores</p>
          </div>
        </div>

        <div class="drogas_right_container">
          <c:forEach var="entry" items="${d.proveedores}" varStatus="status">
            
              <c:choose>
                  <c:when test="${status.last}">
                        
                      <div class="mejor_proveedor_contenedor">
                          <p class="mejor_proveedor_nombre">${entry.nombre}</p>
                          <p class="mejor_proveedor_precio">$${entry.precioUnitario}</p>
                      </div>
                  </c:when>

                  <c:otherwise>
                      <div class="proveedor_item">
                          <p class="proveedor_nombre">${entry.nombre}</p>
                          <p class="proveedor_precio">$${entry.precioUnitario}</p>
                      </div>
                  </c:otherwise>
              </c:choose>

          </c:forEach>
        </div>
      </a>
    </c:forEach>
  </div>
</div>
