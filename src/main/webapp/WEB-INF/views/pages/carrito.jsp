<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Utils.MockCheckoutUtil" %>

<div class="carrito-container">
  <!-- Volver -->
  <%-- Volver --%>
  <c:choose>
    <c:when test="${not empty forward and not fn:contains(forward, '/do-') and not fn:contains(forward, '/auth/')}">
      <a href="${forward}" class="droga-back-link">
        <span class="droga-back-icon">⟵</span>
        Volver
      </a>
    </c:when>
    <c:otherwise>
      <a href="${pageContext.request.contextPath}/" class="droga-back-link">
        <span class="droga-back-icon">⟵</span>
        Volver al Inicio
      </a>
    </c:otherwise>
  </c:choose>

  <div class="carrito-content">
    <!-- Área de items del carrito -->
    <div class="carrito-items-section">
      <c:choose>
        <c:when test="${empty carrito or empty carrito.items}">
          <!-- Estado vacío -->
          <div class="carrito-vacio">
            <h2>Carrito Vacío</h2>
          </div>
        </c:when>
        <c:otherwise>
          <!-- Agrupar items por droga -->
          <c:set var="drogasProcesadas" value="" />
          <c:forEach var="item" items="${carrito.items}">
            <c:set var="drogaId" value="${item.droga.id}" />
            
            <!-- Solo procesar si esta droga no fue procesada antes -->
            <c:if test="${not fn:contains(drogasProcesadas, drogaId)}">
              <!-- Marcar como procesada -->
              <c:set var="drogasProcesadas" value="${drogasProcesadas},${drogaId}," />
              
              <!-- Crear card por droga -->
              <div class="carrito-droga-card">
                <!-- Encabezado de la droga -->
                <div class="carrito-droga-header">
                  <div class="carrito-droga-formula">${item.droga.composicion}</div>
                  <h3 class="carrito-droga-nombre">${item.droga.nombre}</h3>
                </div>

                <!-- Items de esta droga (por proveedor) -->
                <div class="carrito-droga-items">
                  <c:set var="subtotalDroga" value="0" />
                  <c:forEach var="itemDroga" items="${carrito.items}">
                    <c:if test="${itemDroga.droga.id == drogaId}">
                      <c:set var="subtotalItem" value="${itemDroga.cantidad * itemDroga.precioUnitario}" />
                      <c:set var="subtotalDroga" value="${subtotalDroga + subtotalItem}" />
                      
                      <div class="carrito-item-row">
                        <span class="carrito-item-proveedor">${itemDroga.proveedor.nombreFantasia}</span>
                        <span class="carrito-item-cantidad">${itemDroga.cantidad}grs (${itemDroga.precioUnitario}\$)</span>
                        <span class="carrito-item-envio">Envío incluido</span>
                        <span class="carrito-item-precio">${subtotalItem}\$</span>
                      </div>
                    </c:if>
                  </c:forEach>
                </div>

                <!-- Acciones de la droga -->
                <div class="carrito-droga-footer">
                  <a href="${pageContext.request.contextPath}/comprar-droga?drogaId=${item.droga.id}" class="carrito-link">Ir a publicación</a>
                  <a href="${pageContext.request.contextPath}/do-eliminar-item-droga?drogaId=${item.droga.id}" class="carrito-link carrito-link-danger">Eliminar</a>
                  <div class="carrito-droga-subtotal">${subtotalDroga}\$</div>
                </div>
              </div>
            </c:if>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Resumen de compra (siempre visible) -->
<div class="carrito-resumen">
      <h3 class="carrito-resumen-titulo">Resumen de Compra</h3>
      
      <c:choose>
        <c:when test="${not empty carrito and not empty carrito.items}">
          <div class="carrito-resumen-linea">
            <span>Costo Drogas</span>
            <span>${carrito.costoDrogas}$</span>
          </div>
          
          <div class="carrito-resumen-linea">
            <span>Costo Envío</span>
            <span>${carrito.costoEnvio}$</span>
          </div>
          
           <div class="carrito-resumen-total">
             <span>Total</span>
             <span>${carrito.total}$</span>
           </div>
           
           <form action="${pageContext.request.contextPath}/do-checkout" method="POST" class="carrito-form-pago">
             <input type="hidden" name="mockMode" value="true" />
             <button type="submit" class="carrito-btn-pagar">Proceder al pago</button>
             
             <!-- Botón de simulación solo para desarrollo -->
             <c:if test="${mockModeEnabled == true}">
               <button type="submit" name="mock_payment" value="true" class="carrito-btn-pagar btn-mock">Simular Pago Exitoso</button>
             </c:if>
           </form>
        </c:when>
        <c:otherwise>
          <!-- Resumen vacío -->
          <div class="carrito-resumen-vacio">
            <p>No hay items en el carrito</p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>