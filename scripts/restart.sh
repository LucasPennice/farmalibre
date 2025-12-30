#!/bin/bash
# scripts/restart.sh - Reinicio rápido de Tomcat

if [ -z "$CATALINA_HOME" ]; then
    echo "❌ CATALINA_HOME no configurado"
    exit 1
fi

APP_NAME="farmalibre"

echo "🔄 Reinicio rápido de Tomcat..."

# Tocar web.xml para forzar recarga
WEB_XML="$CATALINA_HOME/webapps/$APP_NAME/WEB-INF/web.xml"
if [ -f "$WEB_XML" ]; then
    touch "$WEB_XML"
    echo "✅ Recarga forzada"
else
    echo "⚠️  web.xml no encontrado, recargando contexto..."
    # Alternativa: reiniciar Tomcat
    "$CATALINA_HOME/bin/shutdown.sh" 2>/dev/null
    sleep 2
    "$CATALINA_HOME/bin/startup.sh"
fi

echo "✅ Listo en http://localhost:8080/$APP_NAME"