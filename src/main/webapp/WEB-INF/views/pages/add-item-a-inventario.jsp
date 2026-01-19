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
      <p style="font-size: 36; font-weight: 500">Añadir Item a Inventario</p>

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

    <form style="display: flex; flex-direction: column; gap: 16px; max-width: 420px; margin-top: 16px; justify-content: center;">
      <div class="field">
        <select name="productoId" class="field__input">
          <option value="">Drogas Existentes</option>
            <c:forEach var="d" items="${drogas}">
              <option value="${d.id}">${d.nombre}</option>
            </c:forEach>
        </select>
        <p class="auth-foot">
          No encuentra la droga?
          <button class="auth-link" style="background-color: transparent; border: none;">Cargar nueva droga</button>
        </p>
      </div>

      <label class="field">
        <span class="field__label">Ingresar stock total (en ${d.unidad}):*</span>
        <input type="number" name="cantidad" min="0" step="1" placeholder="0" class="field__input" style="height: 152px;font-size: 96px;color: #0b1113;/* display: flex; *//* justify-content: center;"/>
      </label>

      <label class="field" >
        <span class="field__label">Precio Unitario ($/${d.unidad}):*</span>
        <input class="field__input" type="number" name="cantidad" min="0" step="1" placeholder="0" style="height: 152px;font-size: 96px;color: #0b1113;/* display: flex; *//* justify-content: center;" required/>
      </label>

    </form>
  
  </div>
</html>
