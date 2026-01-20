<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <body>
    <div
      style="
        display: flex;
        align-items: center;
        width: 100%;
        justify-content: space-between;
        margin-bottom: 12px
      "
    >
      <p style="font-size: 36; font-weight: 500">Inventario</p>

      <a
        href="${pageContext.request.contextPath}/add-item-a-inventario"
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
        Añadir Item a Inventario
      </a>
    </div>

    <div style="border: 1px solid #999999; border-radius: 16px; overflow: hidden">
      <!-- Cabecera de la tabla -->
      <div
        style="
          display: flex;
          align-items: center;
          justify-content: space-between;
          background-color: #f2f2f2;
          border-bottom: 1px solid #999999;
          border-radius: 16px 16px 0px 0px;
          height: 55px;
        "
      >
        <!-- Espacio vacío de 40px -->
        <div style="width: 40px; display: flex; justify-content: center; align-items: center;">
        </div>
        <p style="flex: 1; text-align: center">Composición</p>
        <p style="flex: 1; text-align: center">Nombre</p>
        <p style="flex: 1; text-align: center">Cantidad</p>
        <p style="flex: 1; text-align: center">Unidad de Venta</p>
        <p style="flex: 1; text-align: center">Categoría</p>
        <!-- Espacio vacío de 40px -->
        <div style="width: 40px"></div>
      </div>

      <form action="${pageContext.request.contextPath}/do-delete-selected-items" method="post" style="all: unset">
        <input type="hidden" id="singleDeleteId" name="selectedItems" value="" disabled="disabled" />
        <!-- Menu de items seleccionados -->
        <div id="selectedBar" style="
            display: none;
            padding-left: 16px;
            align-items: center;
            justify-content: flex-start;
            gap: 12px;
            background-color: #fafafa;
            border-bottom: 1px solid #999999;
            height: 60px;
          ">
          <p id="selectedCount">0 Items Seleccionados</p>
          <button
            type="submit"
            style="
              all: unset;
              background-color: #fd4949;
              color: white;
              height: 32px;
              width: 223px;
              border-radius: 12px;
              border: none;
              font-size: 16px;
              font-weight: 500;
              display: flex;
              justify-content: center;
              align-items: center;
              cursor: pointer;
            "
          >Borrar Seleccionados</button>
        </div>

        <!-- Tabla -->
          <c:forEach var="i" items="${inventoryItems}">
            <div style="
                  width: 100%;
                  min-height: 100vh;
                ">
              <div
                style="
                  display: flex;
                  justify-content: space-between;
                  align-items: center;
                  height: 60px;
                  border-bottom: 1px solid #999999;
                "
              >
                <div style="width: 40px; display: flex; justify-content: center; align-items: center;">
                  <input type="checkbox" name="selectedItems" value="${i.stockDrogaId}" />
                </div>

                <p style="flex: 1; text-align: center">${i.composicion}</p>
                <p style="flex: 1; text-align: center">${i.nombreDroga}</p>
                <p style="flex: 1; text-align: center">${i.disponible}</p>
                <p style="flex: 1; text-align: center">${i.unidad}</p>
                <p style="flex: 1; text-align: center">
                  <c:if test="${not i.aprobacion_pendiente}">
                    <span style="
                        color: #0F1F12;
                        background-color: #78DD885A;
                        padding: 5px 16px;
                        border-radius: 12px;
                    ">${i.nombreCategoria}</span>
                  </c:if>
                  <c:if test="${i.aprobacion_pendiente}">
                    <span style="
                      color: #2D2901;
                      background-color: #FFC96C5A;
                      padding: 5px 16px;
                      border-radius: 12px;
                  ">${i.nombreCategoria}</span>
                  </c:if>
                </p>
            
              <div style="width: 40px; position: relative; display: flex; justify-content: center; align-items: center;">
                <div class="rowMenuBtn" style="cursor: pointer; display: flex; align-items: center; justify-content: center; width: 40px; height: 40px;">
                  <img src="${pageContext.request.contextPath}/assets/images/ellipses.png" />
                </div>

                <div class="rowDropdown" style="
                  display: none;
                  position: absolute;
                  right: 8px;
                  top: 40px;
                  background: #F2F2F2;
                  border: 1px solid #999999;
                  border-radius: 8px;
                  box-shadow: 0 4px 12px rgba(0,0,0,0.12);
                  min-width: 140px;
                  z-index: 1000;
                ">
                  <a href="${pageContext.request.contextPath}/actualizar-item?itemId=${i.stockDrogaId}" style="display: block; padding: 10px 12px; text-decoration: none; color: #111;">Actualizar Stock (Editar)</a>
                  <a href="#" onclick="submitSingleDelete('${i.stockDrogaId}'); return false;" style="display: block; padding: 10px 12px; text-decoration: none; color: #fd4949; border-top: 1px solid #eee;">Eliminar</a>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </form>
    </div>
  <script>
  document.addEventListener("click", function () {
    document.querySelectorAll(".rowDropdown").forEach(menu => {
      menu.style.display = "none";
    });
  });

  document.querySelectorAll(".rowMenuBtn").forEach((btn) => {
    btn.addEventListener("click", function (e) {
      e.stopPropagation();

      const container = btn.closest("div[style*='position: relative']");
      const menu = container.querySelector(".rowDropdown");

      document.querySelectorAll(".rowDropdown").forEach(m => {
        if (m !== menu) m.style.display = "none";
      });

      menu.style.display = menu.style.display === "none" ? "block" : "none";
    });
  });
  const selectedBar = document.getElementById("selectedBar");
  const selectedCount = document.getElementById("selectedCount");
  const checkboxes = document.querySelectorAll("input[type='checkbox'][name='selectedItems']");
  const singleDeleteInput = document.getElementById("singleDeleteId");

  function updateSelectedBar() {
    const checked = Array.from(checkboxes).filter(cb => cb.checked).length;

    if (checked > 0) {
      selectedBar.style.display = "flex";
      selectedCount.textContent = checked + (checked === 1 ? " Item Seleccionado" : " Items Seleccionados");
      // si hay selección múltiple, no enviar el hidden del borrado individual
      singleDeleteInput.value = "";
      singleDeleteInput.disabled = true;
    } else {
      selectedBar.style.display = "none";
      selectedCount.textContent = "0 Items Seleccionados";
      // mantener el hidden deshabilitado por defecto
      singleDeleteInput.value = "";
      singleDeleteInput.disabled = true;
    }
  }

  checkboxes.forEach(cb => cb.addEventListener("change", updateSelectedBar));
  
  function submitSingleDelete(stockDrogaId) {
    // limpiar cualquier selección previa
    document.querySelectorAll("input[type='checkbox'][name='selectedItems']").forEach(cb => cb.checked = false);

    // setear el id único a borrar y habilitar el hidden para que se envíe
    singleDeleteInput.value = stockDrogaId;
    singleDeleteInput.disabled = false;

    // enviar el mismo form que usa el borrado múltiple
    document.querySelector("form[action*='do-delete-selected-items']").submit();
  }
</script>
  </body>
</html>
