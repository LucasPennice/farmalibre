<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!-- Vista de detalle de Droga -->
<div class="droga-container">
  <!-- Volver -->
  <a href="${pageContext.request.contextPath}/" class="droga-back-link">
    <span class="droga-back-icon">⟵</span>
    Volver
  </a>

  <!-- Encabezado principal -->
  <div class="droga-header">
    <!-- Panel de información de la droga -->
    <div class="droga-info-panel">
      <div class="droga-formula">
        ${droga.formula}
      </div>
      <div class="droga-details">
        <h2 class="droga-name">${droga.nombre}</h2>
        <span class="droga-category-badge">
          ${droga.categoria}
        </span>
      </div>
    </div>

    <!-- Panel de compra rápida -->
    <div class="droga-purchase-panel">
      <div class="droga-quantity-section">
        <p class="droga-quantity-label">Cantidad</p>
        <p class="droga-quantity-hint">Productos seleccionados del proveedor más económico siempre que sea posible</p>
        <select id="cantidadSelect" class="droga-quantity-select">
          <option value="1">1 ${droga.unidad}</option>
          <option value="5">5 ${droga.unidad}</option>
          <option value="10">10 ${droga.unidad}</option>
          <option value="25">25 ${droga.unidad}</option>
          <option value="50">50 ${droga.unidad}</option>
        </select>
      </div>
      <div class="droga-shipping-info">
        El costo de envío aumentó porque se requieren más de un proveedor para satisfacer su pedido
      </div>
      <div class="droga-shipping-cost">Costo Envío: <span id="costoEnvio">—</span></div>

      <div class="droga-actions">
        <a href="${pageContext.request.contextPath}/carrito" class="droga-btn droga-btn-primary">Comprar ahora</a>
        <a href="${pageContext.request.contextPath}/carrito" class="droga-btn droga-btn-secondary">Agregar al carrito</a>
      </div>
    </div>
  </div>

  <!-- Tabla de proveedores -->
  <div class="droga-table-container">
    <table class="droga-table">
      <thead>
        <tr>
          <th>Laboratorio</th>
          <th>Precio Unitario</th>
          <th>Disponibilidad</th>
          <th>Cantidad a comprar</th>
        </tr>
      </thead>
      <tbody>
        <c:set var="minPrecio" value="" />
        <c:forEach var="s" items="${stockDrogas}">
          <c:if test="${s.droga.id == droga.idDroga}">
            <!-- Calcular mejor precio para panel derecho -->
            <c:choose>
              <c:when test="${empty minPrecio}">
                <c:set var="minPrecio" value="${s.precioUnitario}" />
              </c:when>
              <c:otherwise>
                <c:if test="${s.precioUnitario lt minPrecio}">
                  <c:set var="minPrecio" value="${s.precioUnitario}" />
                </c:if>
              </c:otherwise>
            </c:choose>

            <tr>
              <td>${s.proveedor.nombreFantasia}</td>
              <td>$${s.precioUnitario} / ${droga.unidad}</td>
              <td>${s.disponible}</td>
              <td>
                <select class="droga-table-select">
                  <option>1 ${droga.unidad}</option>
                  <option>5 ${droga.unidad}</option>
                  <option>10 ${droga.unidad}</option>
                </select>
              </td>
            </tr>
          </c:if>
        </c:forEach>
      </tbody>
    </table>
  </div>

  <script>
    (function () {
      var minPrecio = Number('${minPrecio}');
      var select = document.getElementById('cantidadSelect');
      var costoEnvioEl = document.getElementById('costoEnvio');

      function actualizarCosto() {
        var cantidad = Number(select.value || 0);
        if (!minPrecio || !cantidad) {
          costoEnvioEl.textContent = '—';
          return;
        }
        var subtotal = minPrecio * cantidad;
        var envio = Math.round(subtotal * 0.08);
        costoEnvioEl.textContent = envio + '$';
      }

      select.addEventListener('change', actualizarCosto);
      actualizarCosto();
    })();
  </script>
</div>
