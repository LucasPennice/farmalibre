<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <div style="
    display: flex;
    flex-direction: column;
    align-items: center;
">
    <div
      style="
        display: flex;
        align-items: center;
        width: 100%;
        justify-content: space-between;
        margin-bottom: 12px
      "
    >
      <p style="font-size: 36; font-weight: 500">Actualizar Stock De ${itemToUpdate.nombreDroga}</p>

      <a
        href="add-item-a-inventario"
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
        "
      >
        Guardar
      </a>
    </div>

    <form style="display: flex; flex-direction: column; gap: 16px; max-width: 420px; margin-top: 16px; justify-content: center;" action="${pageContext.request.contextPath}/do-cargar-nuevo-tipo-droga" method="post">
      <label class="field">
        <span class="field__label">Ingresar stock total (en ${itemToUpdate.unidad}):*</span>
        <input type="number" name="cantidad" min="0" step="1" value="${itemToUpdate.disponible}" class="field__input" style="height: 152px;font-size: 96px;color: #0b1113;/* display: flex; *//* justify-content: center;"/>
      </label>

      <label class="field" >
        <span class="field__label">Precio Unitario ($/${itemToUpdate.unidad}):*</span>
        <input class="field__input" type="number" name="cantidad" min="0" step="1" value="${itemToUpdate.precioUnitario}" style="height: 152px;font-size: 96px;color: #0b1113;/* display: flex; *//* justify-content: center;" required/>
      </label>

    </form>
  
  </div>
</html>
