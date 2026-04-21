#!/bin/bash
# git-helpers.sh - Comandos útiles para Git Flow

# Iniciar nueva feature
gfeature() {
    if [ -z "$1" ]; then
        echo "Uso: gfeature <nombre-feature>"
        return 1
    fi
    git checkout develop
    git pull origin develop
    git checkout -b "feature/$1"
    echo "✅ Rama feature/$1 creada desde develop"
}

# Iniciar nuevo fix
gfix() {
    if [ -z "$1" ]; then
        echo "Uso: gfix <nombre-fix>"
        return 1
    fi
    git checkout develop
    git pull origin develop
    git checkout -b "fix/$1"
    echo "✅ Rama fix/$1 creada desde develop"
}

# Terminar feature y crear PR
gfinish() {
    CURRENT_BRANCH=$(git branch --show-current)
    
    if [[ ! $CURRENT_BRANCH =~ ^(feature|fix)/ ]]; then
        echo "❌ No estás en una rama feature/ o fix/"
        return 1
    fi
    
    git add .
    git commit -m "feat: completar $CURRENT_BRANCH" || true
    git push -u origin "$CURRENT_BRANCH"
    
    echo "✅ Rama $CURRENT_BRANCH subida a remoto"
    echo "📝 Crea un PR desde GitHub: $CURRENT_BRANCH → develop"
}

# Sincronizar develop
gsync() {
    git checkout develop
    git pull origin develop
    echo "✅ Develop actualizado"
}

# Ver estado de ramas
gstatus() {
    echo "🌿 Ramas locales:"
    git branch -v
    
    echo ""
    echo "🔄 Ramas remotas:"
    git branch -r | head -10
    
    echo ""
    echo "📊 Log reciente:"
    git log --oneline -5
}

# Limpiar ramas mergeadas locales
gclean() {
    git checkout develop
    git pull origin develop
    git branch --merged develop | grep -v "develop" | grep -v "main" | xargs git branch -d
    echo "✅ Ramas mergeadas eliminadas"
}