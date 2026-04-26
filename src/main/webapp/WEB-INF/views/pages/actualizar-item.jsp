<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <div style="display: flex; flex-direction: column; align-items: center;">

    <p style="font-size: 36px; font-weight: 500; width: 100%; margin-bottom: 12px;">
      Actualizar Stock De ${itemToUpdate.nombreDroga}
    </p>

    <form
      style="display: flex; flex-direction: column; gap: 16px; max-width: 420px; width: 100%; margin-top: 16px;"
      action="${pageContext.request.contextPath}/do-actualizar-inventario"
      method="post"
    >
      <input type="hidden" name="drogaId" value="${itemToUpdate.drogaId}" />

      <label class="field">
        <span class="field__label">Ingresar stock total (en ${itemToUpdate.unidad}):*</span>
        <input
          type="number"
          name="disponible"
          min="0"
          step="1"
          value="${itemToUpdate.disponible}"
          class="field__input"
          style="height: 152px; font-size: 96px; color: #0b1113;"
          required
        />
      </label>

      <label class="field">
        <span class="field__label">Precio Unitario ($/${itemToUpdate.unidad}):*</span>
        <input
          class="field__input"
          type="number"
          name="precioUnitario"
          min="0.01"
          step="0.01"
          value="${itemToUpdate.precioUnitario}"
          style="height: 152px; font-size: 96px; color: #0b1113;"
          required
        />
      </label>

      <button
        type="submit"
        style="
          background-color: #2763fd;
          color: white;
          height: 40px;
          border-radius: 12px;
          border: none;
          font-size: 14px;
          font-weight: 500;
          cursor: pointer;
        "
      >
        Guardar
      </button>
    </form>

  </div>
</html>
