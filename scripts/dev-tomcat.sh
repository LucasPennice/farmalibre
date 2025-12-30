#!/bin/bash
# scripts/dev-tomcat.sh - VERSIÓN CORREGIDA

echo "🔍 Verificando configuración..."

# Verificar CATALINA_HOME
if [ -z "$CATALINA_HOME" ]; then
    echo "❌ ERROR: CATALINA_HOME no está configurado"
    echo "   Ejecuta: export CATALINA_HOME=\"/ruta/a/tomcat-10\""
    echo "   O edita este script para establecer la ruta manualmente"
    exit 1
fi

if [ ! -d "$CATALINA_HOME" ]; then
    echo "❌ ERROR: Tomcat no encontrado en: $CATALINA_HOME"
    echo "   Verifica la ruta de CATALINA_HOME"
    exit 1
fi

# Configuración
APP_NAME="farmalibre"
PROJECT_DIR=$(cd "$(dirname "$0")/.." && pwd)  # Sube un nivel desde scripts/
EXPLODED_DIR="$PROJECT_DIR/target/$APP_NAME"

echo "🎯 Configuración detectada:"
echo "   Proyecto: $PROJECT_DIR"
echo "   Tomcat: $CATALINA_HOME"
echo "   App: $APP_NAME"
echo ""

echo "🚀 Configurando Tomcat para desarrollo hot-reload..."

# 1. Crear directorios si no existen
mkdir -p "$CATALINA_HOME/conf/Catalina/localhost"

# 2. Crear configuración de contexto
cat > "$CATALINA_HOME/conf/Catalina/localhost/$APP_NAME.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<Context docBase="$EXPLODED_DIR"
         reloadable="true"
         antiResourceLocking="true"
         antiJARLocking="true"
         crossContext="true">
    
    <WatchedResource>WEB-INF/web.xml</WatchedResource>
    <WatchedResource>WEB-INF/classes</WatchedResource>
    
    <!-- Recargar automáticamente -->
    <Manager className="org.apache.catalina.session.StandardManager"/>
    
</Context>
EOF

echo "✅ Configuración de contexto creada"

# 3. Backup de tomcat-users.xml
TOMCAT_USERS="$CATALINA_HOME/conf/tomcat-users.xml"
if [ -f "$TOMCAT_USERS" ]; then
    if [ ! -f "${TOMCAT_USERS}.backup" ]; then
        cp "$TOMCAT_USERS" "${TOMCAT_USERS}.backup"
        echo "✅ Backup creado: ${TOMCAT_USERS}.backup"
    fi
    
    # Verificar si ya tiene la configuración
    if ! grep -q "manager-script" "$TOMCAT_USERS"; then
        # Insertar configuración de usuario
        sed -i '/<\/tomcat-users>/i \
    <role rolename="manager-gui"/>\
    <role rolename="manager-script"/>\
    <role rolename="manager-jmx"/>\
    <role rolename="manager-status"/>\
    <user username="admin" password="admin" roles="manager-gui,manager-script,manager-jmx,manager-status"/>' "$TOMCAT_USERS"
        echo "✅ Usuario manager configurado"
    else
        echo "⚠️  Usuario manager ya estaba configurado"
    fi
else
    echo "⚠️  No se encontró: $TOMCAT_USERS"
    echo "   Configúralo manualmente más tarde"
fi

# 4. Habilitar auto-deploy (modificar server.xml con cuidado)
SERVER_XML="$CATALINA_HOME/conf/server.xml"
if [ -f "$SERVER_XML" ]; then
    if ! grep -q 'autoDeploy="true"' "$SERVER_XML"; then
        sed -i 's/<Host name="localhost".*>/<Host name="localhost" appBase="webapps" unpackWARs="true" autoDeploy="true">/' "$SERVER_XML"
        echo "✅ Auto-deploy habilitado"
    else
        echo "⚠️  Auto-deploy ya estaba habilitado"
    fi
else
    echo "⚠️  No se encontró: $SERVER_XML"
fi

# 5. Compilar proyecto inicialmente
echo "📦 Compilando proyecto..."
cd "$PROJECT_DIR"
mvn clean compile war:exploded

if [ $? -ne 0 ]; then
    echo "❌ Error compilando el proyecto"
    exit 1
fi

# 6. Detener Tomcat si está corriendo
echo "🛑 Deteniendo Tomcat si está corriendo..."
"$CATALINA_HOME/bin/shutdown.sh" 2>/dev/null || true
sleep 2

# 7. Limpiar despliegue anterior
rm -rf "$CATALINA_HOME/webapps/$APP_NAME" 2>/dev/null || true
rm -rf "$CATALINA_HOME/work/Catalina/localhost/$APP_NAME" 2>/dev/null || true

# 8. Crear symlink para desarrollo rápido
ln -sf "$EXPLODED_DIR" "$CATALINA_HOME/webapps/$APP_NAME"
echo "✅ Symlink creado"

# 9. Iniciar Tomcat
echo "🔥 Iniciando Tomcat en modo desarrollo..."
"$CATALINA_HOME/bin/startup.sh"

if [ $? -ne 0 ]; then
    echo "❌ Error iniciando Tomcat"
    echo "   Verifica los permisos de ejecución: chmod +x $CATALINA_HOME/bin/*.sh"
    exit 1
fi

echo ""
echo "=========================================="
echo "🎯 ¡DESARROLLO CONFIGURADO!"
echo "=========================================="
echo "📱 Aplicación: http://localhost:8080/$APP_NAME"
echo "🛠️  Manager:    http://localhost:8080/manager"
echo "🔑 Usuario:    admin / admin"
echo ""
echo "📁 Estructura:"
echo "   Proyecto:    $PROJECT_DIR"
echo "   Desplegado:  $EXPLODED_DIR"
echo ""
echo "⚡ Comandos útiles:"
echo "   npm run dev    - Modo watch (en otra terminal)"
echo "   mvn compile    - Recompilar Java"
echo "   ./restart.sh   - Reiniciar Tomcat rápido"
echo ""
echo "💡 Los cambios en:"
echo "   - Java: Se compilan y recargan automáticamente"
echo "   - Web: Se copian instantáneamente"
echo "=========================================="