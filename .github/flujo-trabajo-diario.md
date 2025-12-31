# 🎯 FLUJO DE TRABAJO DIARIO

## 📋 ANTES DE COMENZAR (Configuración inicial - UNA SOLA VEZ)

```bash

# 1. Navegar a tu proyecto
cd ~/git/farmalibre

# 2. Configurar variable de entorno (agrégalo a ~/.bashrc)
echo 'export CATALINA_HOME="/opt/tomcat-10"' >> ~/.bashrc
source ~/.bashrc

# 3. Instalar dependencias
npm install

# 4. Dar permisos a scripts
chmod +x scripts/*.sh

# 5. Configurar Tomcat para desarrollo
npm run setup
```

# 🚀 FLUJO DE DESARROLLO NORMAL (Cada día/sesión)

## Opción A: Con dos terminales (RECOMENDADO)

### Terminal 1 - Servidor de desarrollo:

```bash
# Iniciar Tomcat si no está corriendo
npm run tomcat:start


# O si prefieres el watch automático completo
npm run dev
```

### Terminal 2 - Para trabajar:

```bash
# Solo abre VS Code o tu editor
code .

# O trabaja directamente desde terminal
# Los cambios se recargan automáticamente
```

## Opción B: Todo en una terminal (simplificado)

```bash
# 1. Iniciar todo
./scripts/start-dev.sh  # (crear este script, ver abajo)

# 2. Trabajar normalmente en VS Code
# 3. Los cambios se aplican automáticamente

```

# 📁 ESTRUCTURA DE CARPETAS Y QUÉ HACER CON CADA CAMBIO

```text

farmalibre/
├── src/main/java/        # Cambios en Servlets/Java
│   ├── AutoSave.java     # → Se recompila automáticamente
│   └── OtroServlet.java  # → Hot reload en ~3 segundos
├── src/main/webapp/      # Cambios en Vistas/Recursos
│   ├── index.jsp         # → Cambio INSTANTÁNEO
│   ├── css/estilos.css   # → Cambio INSTANTÁNEO
│   ├── js/app.js         # → Cambio INSTANTÁNEO
│   └── WEB-INF/
│       └── web.xml       # → Requiere reinicio (raro cambiar)
└── pom.xml              # Cambios en dependencias → mvn compile
```

## ⚡ FLUJO PASO A PASO CUANDO HACES CAMBIOS

### 1. Cuando modificas un SERVLET o CLASE Java:

```java

// En HolaServlet.java
public void doGet(...) {
    // Modificas algo aquí
    response.getWriter().println("NUEVO TEXTO");
}
```

Automáticamente ocurre:

1. Guardas el archivo (Ctrl+S)
2. Script detecta cambio
3. Ejecuta mvn compile automáticamente
4. Copia la clase compilada a Tomcat
5. Tomcat recarga la clase (hot deploy)
6. Listo en 2-3 segundos

Si necesitas forzar manualmente:

```bash
# En otra terminal
mvn compile
# O
npm run compile
```

### 2. Cuando modificas JSP/HTML/CSS/JS:

```html
<!-- En index.jsp -->
<h1>Nuevo título</h1>
<!-- Cambias esto -->
```

Automáticamente ocurre:

1. Guardas el archivo (Ctrl+S)
2. Script copia el archivo directamente a Tomcat
3. Cambio INSTANTÁNEO (refresca navegador con F5)

### 3. Cuando agregas NUEVAS CLASES o ARCHIVOS:

1. Crea el nuevo archivo .java o .jsp
2. Guárdalo
3. Se procesa automáticamente igual que modificaciones

Para nuevas dependencias en pom.xml:

```bash
mvn compile  # Manual, luego sigue automático
```

# 🔧 COMANDOS CLAVE PARA DIFERENTES SITUACIONES

```bash
# 📦 COMPILACIÓN Y DEPLOY
npm run compile      # Compilar rápido (sin tests)
mvn clean compile    # Limpiar y compilar
npm run deploy       # Crear WAR y copiar (para producción)

# 🔄 GESTIÓN DE TOMCAT
npm run tomcat:start    # Iniciar Tomcat
npm run tomcat:stop     # Detener Tomcat
npm run tomcat:restart  # Reiniciar completo
npm run quick          # Recarga rápida (toca web.xml)

# 🐛 DEBUGGING
mvn compile -X        # Compilar con debug
./scripts/restart.sh  # Reinicio rápido cuando hay problemas

# 🧹 LIMPIEZA
mvn clean             # Limpiar target
rm -rf $CATALINA_HOME/webapps/farmalibre  # Limpiar deploy
```

---

# Scripts Útiles para el Flujo

Para utilizar el script `git-helpers.sh`

Lo primero que tenes que hacer es agregarlo a la shell 

``` bash
echo "source $(pwd)/git-helpers.sh" >> ~/.bashrc
source ~/.bashrc

# Si usas zsh
echo "source $(pwd)/git-helpers.sh" >> ~/.zshrc
source ~/.zshrc
```

Y luego darle permisos 

``` bash
sudo chmod +x ./scripts/git-helpers.sh

```
**Ejemplo de un dia creando una nueva feature**

``` bash
# Trabajar en una nueva funcionalidad
gfeature mi-nueva-feature

# Trabajar diariamente
git add .
git commit -m "feat(entidad): descripción clara"
git push

# Al terminar la feature
gfinish  # Crea PR automáticamente

# Revisar PR de tu colega
git checkout develop
git pull origin develop
git checkout -b review-pr-1
# Probar cambios, comentar, aprobar

# Sincronizar diariamente
gsync
```