<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="es">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Farmalibre Auth</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css" />
    <script>
      document.addEventListener("DOMContentLoaded", () => {
        const fileInput = document.querySelector('input[name="fotoPerfil"]');
        const grid = document.querySelector('.image-preview-grid');
        if (!fileInput || !grid) return;

        fileInput.addEventListener('change', (e) => {
          grid.innerHTML = '';
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
  </head>
    <body>
      <main class="auth-shell">
        <section class="auth-left">
          <img src="${pageContext.request.contextPath}/assets/images/Logo.png" class="auth_logo" />

          <div class="auth-card">
            <h1 class="auth-title">Información para conocerlo mejor</h1>

            <form class="auth-form" action="${pageContext.request.contextPath}/do-complete-onboarding-usuario" method="post" enctype="multipart/form-data">
              <label class="field">
                <span class="field__label">Dirección del responsable*</span>
                <input class="field__input" type="text" name="direccion" autocomplete="address" required/>
              </label>

              <label class="field">
                <span class="field__label">Foto de perfil*</span>
                <div class="field__input field_input_photo_container">
                  <input class="field__input_photo" type="file" name="fotoPerfil" accept="image/*" required />
                  <div class="image-preview-grid">
                    <div class="update_img_label">
                      <img src="${pageContext.request.contextPath}/assets/images/camera.png" />
                      <p>Formatos aceptados: JPG, JPEG, PNG. Hasta 5mb</p>  
                    </div>
                  </div>
                </div>
              </label>

              <div class="field">
                <span class="field__label">Esta opción puede modificarse en cualquier momento desde su perfil</span>
                <div class="photo-type-toggle">
                  <label class="toggle-option">
                    <input type="radio" name="esProveedor" value="true" checked />
                    <span>Proveedor & Comprador</span>
                  </label>
                  <label class="toggle-option">
                    <input type="radio" name="esProveedor" value="false" />
                    <span>Comprador</span>
                  </label>
                </div>
              </div>

        
              <button class="primary-btn" type="submit">Siguiente</button>
            </form>
          </div>
        </section>

        <div class="hero-panel"></div>
      </main>
    </body>
  </html>