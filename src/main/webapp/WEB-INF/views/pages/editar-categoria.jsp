<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <div style="
    display: flex;
    flex-direction: column;
    align-items: center;
">

    <form style="display: flex; flex-direction: column; gap: 16px; width: 100%; margin-top: 16px; justify-content: center;" action="${pageContext.request.contextPath}/do-editar-categoria" method="post">
    <div
      style="
        display: flex;
        align-items: center;
        width: 100%;
        justify-content: space-between;
        margin-bottom: 12px
      "
    >
      <p style="font-size: 36; font-weight: 500">Editar Categoría</p>

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
        <span class="field__label">Id*</span>
        <input class="field__input" type="text" value="${categoriaId}" name="categoriaId" readonly/>
      </label>
      
      <label class="field">
        <span class="field__label">Nombre Categoria*</span>
        <input class="field__input" type="text" name="nombreCategoria" value="${nombreCategoria}" required/>
      </label>
    </form>
  
  </div>
  
</html>
