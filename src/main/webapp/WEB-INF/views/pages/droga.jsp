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
        <p class="droga-quantity-label">Cantidad <span style="font-weight: 300; font-size: 12px">(Disponible ${cantidadStockDroga})</span></p>
        <p class="droga-quantity-hint">Ingresá la cantidad deseada; se distribuirá entre proveedores empezando por el más económico</p>
        <input
          id="cantidadInput"
          class="droga-quantity-select"
          type="number"
          min="1"
          max="${cantidadStockDroga}"
          value="1"
        />
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
                <input
                  class="droga-table-select droga-table-input"
                  type="number"
                  min="0"
                  max="${s.disponible}"
                  value="0"
                  data-available="${s.disponible}"
                />
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
      var cantidadInput = document.getElementById('cantidadInput');
      var costoEnvioEl = document.getElementById('costoEnvio');
      var distribInputs = Array.from(document.querySelectorAll('.droga-table-input'));
      var errorEl = document.createElement('div');

      // TODO reemplazar luego, por ahora queda fijo asi
      var ENVIO_FIJO_POR_PROVEEDOR = 5000;
      errorEl.style.color = '#d32f2f';
      errorEl.style.fontSize = '12px';
      errorEl.style.marginTop = '6px';
      cantidadInput.parentElement.appendChild(errorEl);

      function calcularEnvioYMensaje(total) {
        var proveedoresUsados = distribInputs.filter(function (input) {
          return Number(input.value || 0) > 0;
        }).length;

        if (!total || total <= 0) {
          costoEnvioEl.textContent = '—';
          document.querySelector('.droga-shipping-info').style.display = 'none';
          return;
        }

        // Envío dummy: $5000 por proveedor
        var envio = proveedoresUsados * ENVIO_FIJO_POR_PROVEEDOR;
        costoEnvioEl.textContent = envio ? (envio + '$') : '—';

        // Mostrar/ocultar mensaje si hay más de un proveedor
        var info = document.querySelector('.droga-shipping-info');
        if (info) info.style.display = proveedoresUsados > 1 ? 'block' : 'none';
      }

      function setButtonsDisabled(disabled) {
        document.querySelectorAll('.droga-btn').forEach(function (btn) {
          btn.style.pointerEvents = disabled ? 'none' : 'auto';
          btn.style.opacity = disabled ? '0.5' : '1';
        });
      }

      function recalcularEnvio(cantidad) {
        if (!minPrecio || !cantidad) {
          costoEnvioEl.textContent = '—';
          return;
        }
        var subtotal = minPrecio * cantidad;
        var envio = Math.round(subtotal * 0.08);
        costoEnvioEl.textContent = envio + '$';
      }

      function autocompletarDistribucion(total) {
        var restante = total;
        distribInputs.forEach(function (input) {
          var max = Number(input.dataset.available) || 0;
          var asignado = Math.max(0, Math.min(restante, max));
          input.value = asignado;
          restante -= asignado;
        });
      }

      function validarDistribucion() {
        var total = Number(cantidadInput.value || 0);
        var suma = 0;

        distribInputs.forEach(function (input) {
          var max = Number(input.dataset.available) || 0;
          var val = Math.max(0, Number(input.value || 0));
          if (val > max) {
            val = max;
            input.value = val;
          }
          suma += val;
        });

        if (!total || total < 0) {
          errorEl.textContent = 'Ingresá una cantidad válida.';
          setButtonsDisabled(true);
          recalcularEnvio(0);
          return false;
        }

        if (suma > total) {
          errorEl.textContent = 'La distribución supera la cantidad seleccionada.';
          setButtonsDisabled(true);
          recalcularEnvio(total);
          return false;
        }

        if (suma < total) {
          errorEl.textContent = 'La distribución entre proveedores no es igual a la cantidad seleccionada.';
          setButtonsDisabled(true);
          recalcularEnvio(total);
          return false;
        }

        if (suma == total) {
          errorEl.textContent = '';
          setButtonsDisabled(false);
          calcularEnvioYMensaje(total);
          return true;
        }

        errorEl.textContent = '';
        setButtonsDisabled(false);
        recalcularEnvio(total);
        return true;
      }

      // Cuando cambia la cantidad total, auto-distribuimos respetando stock disponible
      cantidadInput.addEventListener('input', function () {
        var total = Number(cantidadInput.value || 0);
        autocompletarDistribucion(total);
        validarDistribucion();
      });

      // Cuando el usuario ajusta la distribución manual
      distribInputs.forEach(function (input) {
        input.addEventListener('input', validarDistribucion);
      });

      // Inicializar
      autocompletarDistribucion(Number(cantidadInput.value || 0));
      validarDistribucion();
    })();
  </script>
</div>
