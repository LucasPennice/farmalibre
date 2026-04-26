<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <div style="
    display: flex;
    flex-direction: column;
    align-items: center;
">

    <form style="display: flex; flex-direction: column; gap: 16px; width: 100%; margin-top: 16px; justify-content: center;" 
          action="${pageContext.request.contextPath}/do-actualizar-perfil" 
          method="post" 
          enctype="multipart/form-data">
    <div
      style="
        display: flex;
        align-items: center;
        width: 100%;
        justify-content: space-between;
        margin-bottom: 12px
      "
    >
      <p style="font-size: 36; font-weight: 500">Perfil</p>

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

    
    <h2>Información Comprador</h2>
              <label class="field">
                <span class="field__label">Nombre Del Responsable*</span>
                <input class="field__input" type="text" value="${perfilData.nombreDelResponsable}" name="nombreDelResponsable" autocomplete="address" required/>
              </label>
              
              <label class="field">
                <span class="field__label">Email de Contacto*</span>
                <input class="field__input" type="text" name="emailDeContacto" value="${perfilData.emailDeContacto}" autocomplete="address" required/>
              </label>
              
              <label class="field">
                <span class="field__label">Dirección del responsable*</span>
                <input class="field__input" type="text" name="direccionDelResponsable" value="${perfilData.direccionDelResponsable}" autocomplete="address" required/>
              </label>

              <label class="field">
                <span class="field__label">Foto de perfil*</span>
                <div class="field__input field_input_photo_container" style="height: 200px;">
                  <input class="field__input_photo" type="file" name="fotoPerfil" accept="image/png,image/jpeg" />
                  <input type="hidden" name="mantenerFotoActual" value="true" />
                  <div class="image-preview-grid">
                    <div class="update_img_label">
                      <img id="fotoPerfilPreview" class="image-preview-item"
                           src="${pageContext.request.contextPath}/usuario-foto?id=${usuario.id}"
                           style="max-width: 200px; border-radius: 12px;" />
                    </div>
                  </div>
                </div>
              </label>

    <c:if test="${perfilData.esProveedor}">
    
            <h2>Información Proveedor</h2>

            <label class="field">
                <span class="field__label">Razón Social*</span>
                <input class="field__input" type="text" name="razonSocial" value="${perfilData.razonSocial}" autocomplete="name" required/>
              </label>

              <label class="field">
                <span class="field__label">Nombre Fantasía*</span>
                <input class="field__input" type="text" name="nombreFantasia" value="${perfilData.nombreFantasia}" autocomplete="" required/>
              </label>

              <label class="field">
                <span class="field__label">CUIT*</span>
                <input class="field__input" type="text" name="CUIT" value="${perfilData.CUIT}" autocomplete="" required/>
              </label>

              <div class="field">
                <span class="field__label">Tipo</span>
                <div class="photo-type-toggle">
                  <label class="toggle-option">
                    <input type="radio" name="tipoPersona" value="${perfilData.tipoPersona}" value="true" checked />
                    <span>Física</span>
                  </label>
                  <label class="toggle-option">
                    <input type="radio" name="tipoPersona" value="${perfilData.tipoPersona}" value="false" />
                    <span>Jurídica</span>
                  </label>
                </div>
              </div>
        </c:if>
    </form>
  
  </div>
  <script>
      document.addEventListener("DOMContentLoaded", () => {
        const fileInput = document.querySelector('input[name="fotoPerfil"]');
        const grid = document.querySelector('.image-preview-grid');
        const preview = document.getElementById('fotoPerfilPreview');
        if (!fileInput || !grid) return;

        fileInput.addEventListener('change', (e) => {
          grid.innerHTML = '';
          const keepFlag = document.querySelector('input[name="mantenerFotoActual"]');
          if (keepFlag) keepFlag.value = "false";
          if (preview) preview.remove();
          Array.from(e.target.files).forEach(file => {
            const reader = new FileReader();
            reader.onload = ev => {
              const img = document.createElement('img');
              img.src = ev.target.result;
              img.className = 'image-preview-item';
              grid.appendChild(img);
            };
            reader.readAsDataURL(file);
          });
        });
      });
    </script>
</html>
