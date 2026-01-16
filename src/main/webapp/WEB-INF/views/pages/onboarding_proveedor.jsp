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
                <span class="field__label">Nombre Completo*</span>
                <input class="field__input" type="text" name="nombre" autocomplete="name" required/>
              </label>

              <label class="field">
                <span class="field__label">Contraseña*</span>
                <input class="field__input" type="password" name="password" autocomplete="new-password" required/>
              </label>

              <button class="primary-btn" type="submit">Iniciar Sesión</button>

              <p class="auth-foot">
                No nos conocemos?
                <a class="auth-link" href="${pageContext.request.contextPath}/auth/register">Crear Cuenta</a>
              </p>
            </form>
          </div>
        </section>

        <div class="hero-panel"></div>
      </main>
    </body>
  </html>