<%@ page isELIgnored="false" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="es">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Farmalibre Auth</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css" />
  </head>
    <body>
      <main class="auth-shell">
        <section class="auth-left">
          <img src="${pageContext.request.contextPath}/assets/images/Logo.png" class="auth_logo" />

          <div class="auth-card">
            <h1 class="auth-title">Gracias por elegirnos para vender sus dulces drogas</h1>

            <form class="auth-form" action="${pageContext.request.contextPath}/do-complete-onboarding-proveedor" method="post">
              <label class="field">
                <span class="field__label">Razón Social*</span>
                <input class="field__input" type="text" name="razonSocial" autocomplete="name" required/>
              </label>

              <label class="field">
                <span class="field__label">Nombre Fantasía*</span>
                <input class="field__input" type="text" name="nombreFantasia" autocomplete="" required/>
              </label>

              <label class="field">
                <span class="field__label">CUIT*</span>
                <input class="field__input" type="text" name="CUIT" autocomplete="" required/>
              </label>

              <div class="field">
                <span class="field__label">Tipo</span>
                <div class="photo-type-toggle">
                  <label class="toggle-option">
                    <input type="radio" name="tipoPersona" value="true" checked />
                    <span>Física</span>
                  </label>
                  <label class="toggle-option">
                    <input type="radio" name="tipoPersona" value="false" />
                    <span>Jurídica</span>
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