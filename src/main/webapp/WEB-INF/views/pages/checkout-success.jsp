<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="checkout-container checkout-success">
  <!-- Icono de éxito -->
  <div class="checkout-icon-success">
    <span>✓</span>
  </div>
  
  <!-- Mensaje de éxito -->
  <h1 class="checkout-titulo">¡Pago Exitoso!</h1>
  <p class="checkout-mensaje">Tu compra ha sido procesada correctamente.</p>
  
  <!-- Detalles del pago -->
  <div class="checkout-detalles">
    <div class="checkout-fila">
      <span class="checkout-label">Payment ID:</span>
      <span class="checkout-valor">${param.payment_id}</span>
    </div>
    <div class="checkout-fila">
      <span class="checkout-label">Preference ID:</span>
      <span class="checkout-valor">${param.preference_id}</span>
    </div>
  </div>
  
  <!-- Próximos pasos -->
  <div class="checkout-pasos">
    <h3>Próximos pasos:</h3>
    <ul>
      <li>Recibirás un correo de confirmación en breve</li>
      <li>Tu pedido será procesado y enviado</li>
      <li>Puedes seguir tu compra desde tu perfil</li>
    </ul>
  </div>
  
  <!-- Botones de acción -->
  <div class="checkout-acciones">
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Volver al Inicio</a>
    <a href="${pageContext.request.contextPath}/perfil" class="btn btn-secondary">Ver Mis Compras</a>
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
  
  .checkout-success {
    background: #f0f7f0;
    border: 2px solid #4caf50;
  }
  
  .checkout-icon-success {
    font-size: 60px;
    color: #4caf50;
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
  
  .checkout-detalles {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    text-align: left;
  }
  
  .checkout-fila {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid #eee;
  }
  
  .checkout-fila:last-child {
    border-bottom: none;
  }
  
  .checkout-label {
    font-weight: bold;
    color: #333;
  }
  
  .checkout-valor {
    color: #666;
    font-family: monospace;
  }
  
  .checkout-pasos {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    text-align: left;
  }
  
  .checkout-pasos h3 {
    margin-top: 0;
    color: #333;
  }
  
  .checkout-pasos ul {
    list-style-position: inside;
    color: #666;
  }
  
  .checkout-pasos li {
    padding: 8px 0;
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
    background: #4caf50;
    color: white;
  }
  
  .btn-primary:hover {
    background: #45a049;
  }
  
  .btn-secondary {
    background: #2196f3;
    color: white;
  }
  
  .btn-secondary:hover {
    background: #0b7dda;
  }
</style>
