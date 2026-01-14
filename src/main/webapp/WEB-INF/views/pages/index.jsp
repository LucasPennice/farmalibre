<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div>
  <div class="topbar">
    <div class="search-wrapper">
      <img src="${pageContext.request.contextPath}/assets/images/lupa.png" />
      <input
        type="text"
        placeholder="Buscar droga por nombre, composición, o categoría"
      />
    </div>

    <a href="/farmalibre/carrito" class="cart">
      <img src="${pageContext.request.contextPath}/assets/images/carrito.png" />
      <p class="badge">5</p>
    </a>

    <a href="/farmalibre/perfil" class="perfil_index_contenedor anchor_remove_styles">
      <div class="foto_farmacia">
        <img src="" />
      </div>

      <p class="farmacia_nombre">Droguería San Marcos</p>
      </div>
    </a>

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
