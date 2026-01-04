#!/bin/bash
set -e

echo "🚀 Setup inicial de Farmalibre"
echo ""

# 1. Verificar dependencias
echo "1️⃣  Verificando dependencias..."
which mysql > /dev/null || { echo "❌ MySQL no está instalado"; exit 1; }
which java > /dev/null || { echo "❌ Java no está instalado"; exit 1; }
which mvn > /dev/null || { echo "❌ Maven no está instalado"; exit 1; }
echo "✅ Dependencias OK"
echo ""

# 2. Cargar variables
echo "2️⃣  Cargando variables..."
if [ ! -f .env.dev ]; then
    echo "❌ No se encontró .env.dev"
    exit 1
fi
export $(grep -v '^#' .env.dev | xargs)
echo "✅ Variables cargadas"
echo ""

# 3. Crear bases de datos
echo "3️⃣  Creando bases de datos..."
DB_USER=${DB_USER:-root}
DB_PASS=${DB_PASS:-}

mysql -u "$DB_USER" -p"$DB_PASS" -e "CREATE DATABASE IF NOT EXISTS farmacia_db; CREATE DATABASE IF NOT EXISTS farmacia_test;" 2>/dev/null
echo "✅ Bases de datos creadas"
echo ""

# 4. Ejecutar schema
echo "4️⃣  Ejecutando schema.sql..."
SCHEMA_FILE="src/main/resources/schema.sql"
mysql -u "$DB_USER" -p"$DB_PASS" farmacia_db < "$SCHEMA_FILE" 2>/dev/null
mysql -u "$DB_USER" -p"$DB_PASS" farmacia_test < "$SCHEMA_FILE" 2>/dev/null
echo "✅ Schema ejecutado"
echo ""

# 5. Compilar proyecto
echo "5️⃣  Compilando proyecto..."
mvn clean package -q
echo "✅ Proyecto compilado"
echo ""

echo "🎉 Setup completado!"
echo ""
echo "Próximos pasos:"
echo "  npm run tomcat:start    # Iniciar Tomcat"
echo "  npm run dev             # Watch en desarrollo"