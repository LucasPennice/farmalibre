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

      <form action="${pageContext.request.contextPath}/do-delete-selected-items" style="all: unset">
        <!-- Menu de items seleccionados -->
        <div
          style="
            display: flex;
            padding-left: 16px;
            align-items: center;
            justify-content: flex-start;
            gap: 12px;
            background-color: #fafafa;
            border-bottom: 1px solid #999999;
            height: 60px;
          "
        >
          <p>1 Item Seleccionado/s</p>
          <a
            href=""
            type="submit"
            style="
              all: unset;
              background-color: #fd4949;
              color: white;
              height: 32px;
              width: 223px;
              border-radius: 12px;
              border: none;
              font-size: 14px;
              font-weight: 500;
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
            "
            >Borrar Seleccionados</a
          >
        </div>

        <!-- Tabla -->
          <c:forEach var="entry" items="${i}" varStatus="items">
            <div
              style="
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 1px solid #999999;
                height: 60px;
              "
            >
              <div style="width: 40px; display: flex; justify-content: center; align-items: center;">
                <input type="checkbox" name="selectedItems" value="itemId" />
              </div>

              <p style="flex: 1; text-align: center">${i.composicion}</p>
              <p style="flex: 1; text-align: center">${i.nombreDroga}</p>
              <p style="flex: 1; text-align: center">${i.disponible}</p>
              <p style="flex: 1; text-align: center">${i.unidad}</p>
              <div style="flex: 1; text-align: center">
                <p>${i.nombreCategoria}</p>
              </div>

          
            <div style="width: 40px; position: relative; display: flex; justify-content: center; align-items: center;">
              <div id="rowMenuBtn" style="cursor: pointer; display: flex; align-items: center; justify-content: center; width: 40px; height: 40px;">
                <img src="${pageContext.request.contextPath}/assets/images/ellipses.png" />
              </div>

              <div id="rowDropdown" style="
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
                <a href="${pageContext.request.contextPath}/actualizar-item" style="display: block; padding: 10px 12px; text-decoration: none; color: #111;">Actualizar Stock (Editar)</a>
                <a href="#" style="display: block; padding: 10px 12px; text-decoration: none; color: #fd4949; border-top: 1px solid #eee;">Eliminar</a>
              </div>
            </div>
          </c:forEach>
        </div>
      </form>
    </div>
  <script>
    const btn = document.getElementById("rowMenuBtn");
    const menu = document.getElementById("rowDropdown");

    btn.addEventListener("click", function (e) {
      e.stopPropagation();
      menu.style.display = menu.style.display === "none" ? "block" : "none";
    });

    document.addEventListener("click", function () {
      menu.style.display = "none";
    });
  </script>
  </body>
</html>
