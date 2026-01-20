<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <div style="
    display: flex;
    flex-direction: column;
    align-items: center;
">

    <form style="display: flex; flex-direction: column; gap: 16px; width: 100%; margin-top: 16px; justify-content: center;" action="${pageContext.request.contextPath}/do-add-item-to-inventario" method="post">
      <div
        style="
          display: flex;
          align-items: center;
          width: 100%;
          justify-content: space-between;
          margin-bottom: 12px
        "
      >
        <p style="font-size: 36; font-weight: 500">Añadir Item a Inventario</p>

        <button
          type="submit"
          style="
            all: unset;
            background-color: #2763fd;
            color: white;
            height: 32px;
            width: 223px;
            border-radius: 12px;
            border: none;
            font-size: 14px;
            font-weight: 500;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
          "
        >
          Guardar
        </a>
      </div>
      
      <div class="field">
        <select name="drogaId" class="field__input">
          <option value="">Drogas Existentes</option>
            <c:forEach var="d" items="${drogas}">
              <option value="${d.id}" data-unidad="${d.unidad}">${d.nombre}</option>
            </c:forEach>
        </select>
        <p class="auth-foot">
          No encuentra la droga?
          <a class="auth-link" href="${pageContext.request.contextPath}/cargar-nuevo-tipo-droga" style="background-color: transparent; border: none;">Cargar nueva droga</a>
        </p>
      </div>

      <label class="field">
        <span class="field__label">Ingresar stock total (en <span id="unidad-stock">-</span>):*</span>
        <input type="number" name="disponible" min="0" step="1" placeholder="0" class="field__input"/>
      </label>

      <label class="field" >
        <span class="field__label">Precio Unitario ($/<span id="unidad-precio">-</span>):*</span>
        <input class="field__input" type="number" name="precioUnitario" min="0" step="1" placeholder="0" required/>
      </label>

    </form>
  
  </div>

<script>
  document.addEventListener("DOMContentLoaded", function () {
    const select = document.querySelector('select[name="productoId"]');
    const unidadStock = document.getElementById("unidad-stock");
    const unidadPrecio = document.getElementById("unidad-precio");

    function actualizarUnidad() {
      const option = select.options[select.selectedIndex];
      const unidad = option.dataset.unidad || "-";
      unidadStock.textContent = unidad;
      unidadPrecio.textContent = unidad;
    }

    // Inicializar al cargar la página
    actualizarUnidad();

    // Actualizar cuando cambia la selección
    select.addEventListener("change", actualizarUnidad);
  });
</script>
</html>
