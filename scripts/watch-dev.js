// scripts/watch-dev.js - VERSIÓN CORREGIDA
const fs = require('fs');
const chokidar = require('chokidar');
const { exec } = require('child_process');
const path = require('path');

// VERIFICACIÓN DE VARIABLES DE ENTORNO
const TOMCAT_HOME = process.env.CATALINA_HOME;

if (!TOMCAT_HOME) {
    console.error('❌ ERROR: CATALINA_HOME no está configurado');
    console.error('   Ejecuta: export CATALINA_HOME="/ruta/a/tomcat-10"');
    console.error('   O agrégalo a ~/.bashrc permanentemente');
    process.exit(1);
}

if (!fs.existsSync(TOMCAT_HOME)) {
    console.error(`❌ ERROR: Tomcat no encontrado en: ${TOMCAT_HOME}`);
    console.error('   Verifica la ruta de CATALINA_HOME');
    process.exit(1);
}

// Configuración
const APP_NAME = 'farmalibre';
const PROJECT_DIR = path.resolve(__dirname, '..');  // Sube un nivel desde scripts/
const EXPLODED_DIR = path.join(PROJECT_DIR, 'target', APP_NAME);
const TOMCAT_CLASSES = path.join(TOMCAT_HOME, 'webapps', APP_NAME, 'WEB-INF', 'classes');

console.log('🎯 Configuración detectada:');
console.log(`   Proyecto: ${PROJECT_DIR}`);
console.log(`   Tomcat: ${TOMCAT_HOME}`);
console.log(`   App: ${APP_NAME}`);
console.log('');

// Verificar estructura del proyecto
if (!fs.existsSync(path.join(PROJECT_DIR, 'pom.xml'))) {
    console.error('❌ ERROR: No se encontró pom.xml en el directorio del proyecto');
    console.error(`   Directorio actual: ${PROJECT_DIR}`);
    process.exit(1);
}

console.log('👀 Iniciando watch para desarrollo...');

// Crear directorio target si no existe
if (!fs.existsSync(EXPLODED_DIR)) {
    console.log('📦 Compilando proyecto por primera vez...');
    exec('mvn clean compile war:exploded', { cwd: PROJECT_DIR }, (error) => {
        if (error) {
            console.error('❌ Error compilando proyecto');
            process.exit(1);
        }
        startWatching();
    });
} else {
    startWatching();
}

function startWatching() {
    // Crear symlink si no existe
    const tomcatAppDir = path.join(TOMCAT_HOME, 'webapps', APP_NAME);
    if (!fs.existsSync(tomcatAppDir)) {
        fs.symlinkSync(EXPLODED_DIR, tomcatAppDir, 'dir');
        console.log('✅ Symlink creado para desarrollo rápido');
    }

    // Watch para archivos Java
    const javaWatcher = chokidar.watch('src/main/java/**/*.java', {
        cwd: PROJECT_DIR,
        ignored: /(^|[\/\\])\../,
        persistent: true,
        ignoreInitial: true
    });

    javaWatcher.on('change', (filePath) => {
        const fullPath = path.join(PROJECT_DIR, filePath);
        console.log(`🔄 Cambio en Java: ${path.basename(filePath)}`);
        
        // Compilación rápida
        exec('mvn compile -q -DskipTests', { cwd: PROJECT_DIR }, (error) => {
            if (error) {
                console.error('❌ Error de compilación');
                return;
            }
            
            // Copiar clase compilada directamente
            const relativePath = path.relative(path.join(PROJECT_DIR, 'src/main/java'), fullPath);
            const classFile = fullPath
                .replace('src/main/java', 'target/classes')
                .replace('.java', '.class');
            
            const destFile = path.join(TOMCAT_CLASSES, relativePath.replace('.java', '.class'));
            
            // Crear directorio destino si no existe
            fs.mkdirSync(path.dirname(destFile), { recursive: true });
            
            if (fs.existsSync(classFile)) {
                fs.copyFileSync(classFile, destFile);
                console.log(`✅ Clase actualizada: ${path.basename(destFile)}`);
                
                // Forzar recarga tocando web.xml
                const webXml = path.join(tomcatAppDir, 'WEB-INF', 'web.xml');
                if (fs.existsSync(webXml)) {
                    const now = new Date();
                    fs.utimesSync(webXml, now, now);
                    console.log('🔄 Tomcat recargado');
                }
            }
        });
    });

    // Watch para recursos web
    const webWatcher = chokidar.watch([
        'src/main/webapp/**/*',
        'src/main/resources/**/*'
    ], {
        cwd: PROJECT_DIR,
        ignored: ['**/*.java', '**/target/**'],
        persistent: true,
        ignoreInitial: true
    });

    webWatcher.on('all', (event, filePath) => {
        if (event === 'change' || event === 'add') {
            const fullPath = path.join(PROJECT_DIR, filePath);
            const relativePath = path.relative(path.join(PROJECT_DIR, 'src/main/webapp'), fullPath);
            const destPath = path.join(EXPLODED_DIR, relativePath);
            
            // Crear directorio si no existe
            const destDir = path.dirname(destPath);
            if (!fs.existsSync(destDir)) {
                fs.mkdirSync(destDir, { recursive: true });
            }
            
            fs.copyFileSync(fullPath, destPath);
            console.log(`📁 ${event}: ${relativePath}`);
        }
    });

    console.log('✅ Watch activo para:');
    console.log('   - src/main/java/**/*.java');
    console.log('   - src/main/webapp/**/*');
    console.log('');
    console.log(`📡 Servidor: http://localhost:8080/${APP_NAME}`);
    console.log('⚡ Listo para desarrollo! Guarda los archivos y se recargarán automáticamente.');
}