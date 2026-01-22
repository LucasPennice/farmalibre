<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="checkout-container checkout-pending">
  <!-- Icono de pendiente -->
  <div class="checkout-icon-pending">
    <span>⏳</span>
  </div>
  
  <!-- Mensaje de pendiente -->
  <h1 class="checkout-titulo">Pago Pendiente</h1>
  <p class="checkout-mensaje">Tu pago está siendo procesado.</p>
  
  <!-- Información de estatus -->
  <div class="checkout-info-pending">
    <p>Recibirás una confirmación en tu correo cuando se complete el pago.</p>
    <p>Este proceso puede tomar hasta 24 horas en algunos casos.</p>
  </div>
  
  <!-- Detalles -->
  <div class="checkout-detalles">
    <p><strong>¿Qué sucede ahora?</strong></p>
    <ul style="text-align: left; display: inline-block;">
      <li>Tu pago está en revisión</li>
      <li>Te notificaremos cuando se confirme</li>
      <li>Puedes verificar el estado en tu perfil</li>
    </ul>
  </div>
  
  <!-- Botones de acción -->
  <div class="checkout-acciones">
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Volver al Inicio</a>
    <a href="${pageContext.request.contextPath}/perfil" class="btn btn-secondary">Ver Mi Perfil</a>
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
  
  .checkout-pending {
    background: #fffde7;
    border: 2px solid #fbc02d;
  }
  
  .checkout-icon-pending {
    font-size: 60px;
    color: #fbc02d;
    margin-bottom: 20px;
    animation: pulse 2s infinite;
  }
  
  @keyframes pulse {
    0%, 100% {
      opacity: 1;
    }
    50% {
      opacity: 0.6;
    }
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
  
  .checkout-info-pending {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    color: #666;
    border-left: 4px solid #fbc02d;
  }
  
  .checkout-info-pending p {
    margin: 10px 0;
  }
  
  .checkout-detalles {
    background: #f0f0f0;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    color: #333;
  }
  
  .checkout-detalles p {
    margin: 0 0 15px 0;
    font-weight: bold;
  }
  
  .checkout-detalles ul {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  
  .checkout-detalles li {
    padding: 8px 0;
    color: #666;
  }
  
  .checkout-detalles li:before {
    content: "• ";
    color: #fbc02d;
    font-weight: bold;
    margin-right: 10px;
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
    background: #fbc02d;
    color: #333;
  }
  
  .btn-primary:hover {
    background: #f9a825;
  }
  
  .btn-secondary {
    background: #2196f3;
    color: white;
  }
  
  .btn-secondary:hover {
    background: #0b7dda;
  }
</style>
