<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <div style="
    display: flex;
    flex-direction: column;
    align-items: center;
">

    <form style="display: flex; flex-direction: column; gap: 16px; width: 100%; margin-top: 16px; justify-content: center;" action="${pageContext.request.contextPath}/do-cargar-nuevo-tipo-droga" method="post">
    <div
      style="
        display: flex;
        align-items: center;
        width: 100%;
        justify-content: space-between;
        margin-bottom: 12px
      "
    >
      <p style="font-size: 36; font-weight: 500">Cargar Nuevo Tipo de Droga</p>

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
      </button>
    </div>

      <label class="field">
        <span class="field__label">Nombre Droga*</span>
        <input class="field__input" type="text" name="nombreDroga" required/>
      </label>

      <label class="field">
        <span class="field__label">Composicion*</span>
        <input class="field__input" type="text" name="composicion" required/>
      </label>

      <label class="field">
        <span class="field__label">Unidad* (Ej: Gramos)</span>
        <input class="field__input" type="text" name="unidad" required/>
      </label>

      <div class="field" id="toggle_add_categoria">
        <select name="nombreCategoria" class="field__input">
          <option value="">Categorías Existentes</option>
            <c:forEach var="c" items="${categorias}">
              <option value="${c.nombre}">${c.nombre}</option>
            </c:forEach>
        </select>
        <p class="auth-foot" style="display:flex; align-items:center">
          No encuentra la categoría?
          <div id="btn_add_categoria" class="auth-link" style="background-color: transparent; border: none; cursor: pointer;">Crear Nueva Categoría</div>
        </p>
      </div>

    </form>
  
  </div>
  
<script>
document.addEventListener("DOMContentLoaded", function() {
  const toggleContainer = document.getElementById("toggle_add_categoria");
  const toggleButton = document.getElementById("btn_add_categoria");
  const form = document.querySelector("form");

  if (!toggleContainer || !toggleButton || !form) {
    console.error("No se encontró toggle_add_categoria, btn_add_categoria o el form en el DOM");
    return;
  }

  toggleButton.addEventListener("click", function(e) {
    e.preventDefault();

    // Remover el selector de categorías del DOM
    if (toggleContainer && toggleContainer.parentNode) {
      toggleContainer.parentNode.removeChild(toggleContainer);
    }

    // Crear el campo de nueva categoría SOLO en este momento
    const label = document.createElement("label");
    label.className = "field";
    label.style.marginTop = "12px";

    const span = document.createElement("span");
    span.className = "field__label";
    span.textContent = "Nueva Categoría:*";

    const input = document.createElement("input");
    input.className = "field__input";
    input.type = "text";
    input.name = "nombreCategoria";
    input.placeholder = "Nombre de la categoría";
    input.required = true;

    label.appendChild(span);
    label.appendChild(input);

    // Insertar el nuevo campo al final del formulario
    form.appendChild(label);
  });
});
</script>
</html>
