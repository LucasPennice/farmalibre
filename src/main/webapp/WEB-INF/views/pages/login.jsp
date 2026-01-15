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
          <header class="brand">
            <div class="brand__name">
              Farma<span class="brand__accent">Libre</span>
            </div>
          </header>

          <div class="auth-card">
            <h1 class="auth-title">Login</h1>

            <form class="auth-form" action="${pageContext.request.contextPath}/auth/register" method="post">
              <label class="field">
                <span class="field__label">Nombre Completo*</span>
                <input class="field__input" type="text" name="nombre" autocomplete="name" required/>
              </label>

              <label class="field">
                <span class="field__label">Email De Contacto*</span>
                <input class="field__input" type="email" name="email" autocomplete="email" required/>
              </label>

              <label class="field">
                <span class="field__label">Contraseña*</span>
                <input class="field__input" type="password" name="password" autocomplete="new-password" required/>
              </label>

              <button class="primary-btn" type="submit">Crear Cuenta</button>

              <p class="auth-foot">
                Ya tiene una cuenta?
                <a class="auth-link" href="#">Ingresar Aquí</a>
              </p>
            </form>
          </div>
        </section>

        <aside class="auth-right" aria-hidden="true">
          <div class="hero-panel"></div>
        </aside>
      </main>
    </body>
  </html>