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
      <p style="font-size: 36; font-weight: 500">Aprobar Categoria</p>
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
        <p style="flex: 1; text-align: center">Id</p>
        <p style="flex: 1; text-align: center">Nombre</p>
        <p style="flex: 1; text-align: center">Estado</p>
        <!-- Espacio vacío de 40px -->
        <div style="width: 40px"></div>
      </div>

        <!-- Tabla -->
          <c:forEach var="c" items="${categoriasPendientes}">
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
                <p style="flex: 1; text-align: center">${c.id}</p>
                <p style="flex: 1; text-align: center">${c.nombre}</p>
                <p style="flex: 1; text-align: center">
                   <span style="
                      color: #2D2901;
                      background-color: #FFC96C5A;
                      padding: 5px 16px;
                      border-radius: 12px;
                  ">Pendiente</span>
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
                  <a href="${pageContext.request.contextPath}/do-aprobar-categoria?categoriaId=${c.id}" style="display: block; padding: 10px 12px; text-decoration: none; color: #111;">Aprobar</a>
                  <a href="${pageContext.request.contextPath}/do-rechazar-categoria?categoriaId=${c.id}" style="display: block; padding: 10px 12px; text-decoration: none; color: #fd4949; border-top: 1px solid #eee;">Rechazar</a>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
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
    </script>
  </body>
</html>
