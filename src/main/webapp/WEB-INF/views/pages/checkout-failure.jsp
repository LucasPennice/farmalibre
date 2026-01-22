<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="checkout-container checkout-failure">
  <!-- Icono de error -->
  <div class="checkout-icon-failure">
    <span>✗</span>
  </div>
  
  <!-- Mensaje de fracaso -->
  <h1 class="checkout-titulo">Pago Cancelado</h1>
  <p class="checkout-mensaje">El pago no pudo ser procesado.</p>
  
  <!-- Razones posibles -->
  <div class="checkout-razones">
    <h3>Razones comunes:</h3>
    <ul>
      <li>Fondos insuficientes</li>
      <li>Datos de tarjeta incorrectos</li>
      <li>Pago rechazado por el banco</li>
      <li>Conexión perdida durante el proceso</li>
    </ul>
  </div>
  
  <!-- Información importante -->
  <div class="checkout-info">
    <p><strong>¡No te preocupes!</strong> Tu carrito se mantiene intacto. Puedes intentar de nuevo cuando lo desees.</p>
  </div>
  
  <!-- Botones de acción -->
  <div class="checkout-acciones">
    <a href="${pageContext.request.contextPath}/carrito" class="btn btn-primary">Volver al Carrito</a>
    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Continuar Comprando</a>
  </div>
</div>

<style>
  .checkout-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 40px;
    text-align: center;
    background: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  .checkout-failure {
    background: #fef5f5;
    border: 2px solid #f44336;
  }
  
  .checkout-icon-failure {
    font-size: 60px;
    color: #f44336;
    margin-bottom: 20px;
    font-weight: bold;
  }
  
  .checkout-titulo {
    color: #333;
    margin-bottom: 10px;
  }
  
  .checkout-mensaje {
    color: #666;
    margin-bottom: 30px;
    font-size: 16px;
  }
  
  .checkout-razones {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    text-align: left;
    border-left: 4px solid #f44336;
  }
  
  .checkout-razones h3 {
    margin-top: 0;
    color: #333;
  }
  
  .checkout-razones ul {
    list-style-position: inside;
    color: #666;
  }
  
  .checkout-razones li {
    padding: 8px 0;
  }
  
  .checkout-info {
    background: #e3f2fd;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    color: #1976d2;
    border-left: 4px solid #1976d2;
  }
  
  .checkout-info p {
    margin: 0;
  }
  
  .checkout-acciones {
    display: flex;
    gap: 15px;
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .btn {
    padding: 12px 30px;
    border: none;
    border-radius: 5px;
    text-decoration: none;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    display: inline-block;
  }
  
  .btn-primary {
    background: #f44336;
    color: white;
  }
  
  .btn-primary:hover {
    background: #da190b;
  }
  
  .btn-secondary {
    background: #2196f3;
    color: white;
  }
  
  .btn-secondary:hover {
    background: #0b7dda;
  }
</style>
