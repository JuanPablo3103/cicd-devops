# Proyecto CI/CD con GitHub Actions, JaCoCo y SonarCloud

Proyecto Java + Maven que demuestra un flujo de **Integración Continua (CI/CD)**:
en cada `push` o Pull Request a `main`, GitHub Actions compila el proyecto,
ejecuta las pruebas unitarias, genera el reporte de cobertura con **JaCoCo** y
envía el análisis de calidad a **SonarCloud**.

La aplicación de ejemplo es una calculadora de descuentos (`DiscountCalculator`).

## Estructura del proyecto

```
cicd-devops/
├── .github/workflows/ci.yml          # Workflow de GitHub Actions (el pipeline)
├── src/main/java/com/example/discount/
│   ├── DiscountCalculator.java        # Lógica de negocio
│   └── InvalidPurchaseException.java  # Excepción personalizada
├── src/test/java/com/example/discount/
│   └── DiscountCalculatorTest.java    # Pruebas unitarias (JUnit 5)
├── pom.xml                            # Configuración de Maven + JaCoCo + Surefire
├── sonar-project.properties           # Configuración de SonarCloud
└── .gitignore
```

## Qué hace el pipeline (`mvn clean verify`)

1. **Compila** el código.
2. **Ejecuta** las 5 pruebas unitarias con Surefire.
3. **Genera** el reporte de cobertura JaCoCo en `target/site/jacoco/`.
4. **Verifica** que la cobertura de líneas sea **≥ 95%** por clase (regla `check`).
   Si baja del 95%, el build falla.
5. **Envía** el análisis a SonarCloud.

---

# 🚀 GUÍA PASO A PASO (de cero a entrega)

## Paso 0 — Requisitos previos

- Una cuenta de **GitHub** (https://github.com). Es gratis.
- Una cuenta de **SonarCloud** (https://sonarcloud.io) — inicia sesión con tu GitHub.
- (Opcional) **Git** instalado en tu computador. Si no quieres instalar nada,
  puedes hacer casi todo desde la web de GitHub.

> Esta actividad es en parejas o grupos de máximo 3. Uno crea el repositorio y
> agrega a los demás como colaboradores (Settings → Collaborators).

## Paso 1 — Crear el repositorio en GitHub

1. Entra a GitHub → botón **New** (nuevo repositorio).
2. Nombre: `cicd-devops` (o el que prefieras).
3. Marca **Public** (el reto pide que sea accesible para revisión).
4. **No** marques "Add a README" (ya tienes uno).
5. Crea el repositorio.

## Paso 2 — Subir los archivos

**Opción A — Con Git (recomendada):**
```bash
# Dentro de la carpeta cicd-devops descomprimida:
git init
git add .
git commit -m "Proyecto inicial con CI/CD, JaCoCo y SonarCloud"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/cicd-devops.git
git push -u origin main
```

**Opción B — Desde la web (sin instalar nada):**
En tu repositorio vacío usa **Add file → Upload files** y arrastra TODA la carpeta.
Importante: respeta las rutas (la carpeta oculta `.github/workflows/` debe quedar igual).
Confirma con **Commit changes**.

> Al hacer el primer push verás en la pestaña **Actions** que el workflow
> "Java CI" se ejecuta. El paso de SonarCloud fallará hasta completar el Paso 3.

## Paso 3 — Configurar SonarCloud

1. Entra a https://sonarcloud.io e inicia sesión con GitHub.
2. **Analyze new project** / el botón **+** → **Analyze new project**.
3. Autoriza a SonarCloud a ver tu organización/repositorio de GitHub.
4. Selecciona tu repositorio `cicd-devops` e impórtalo.
5. SonarCloud te mostrará dos datos clave:
   - **Organization Key** (ej: `tu-usuario`)
   - **Project Key** (ej: `tu-usuario_cicd-devops`)
6. En **Administration → Analysis Method**: **desactiva**
   "Automatic Analysis" (usaremos GitHub Actions, no el análisis automático).
   Si quedan ambos activos, el pipeline da error.

### Edita `sonar-project.properties`

Reemplaza los valores de ejemplo por los tuyos:
```
sonar.projectKey=TU_PROJECT_KEY
sonar.organization=TU_ORGANIZATION_KEY
```
Guarda y súbelo (commit) al repositorio.

## Paso 4 — Crear el secreto SONAR_TOKEN

1. En SonarCloud: foto de perfil → **My Account → Security**.
2. Genera un **token** (dale un nombre, ej. `github-actions`) y **cópialo**
   (solo se muestra una vez).
3. En GitHub, en tu repositorio: **Settings → Secrets and variables → Actions
   → New repository secret**.
4. Nombre exacto: `SONAR_TOKEN`. Valor: pega el token. Guarda.

> El workflow ya lee este secreto: `SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}`.

## Paso 5 — Verificar que el pipeline funciona (CHECK VERDE ✅)

1. Haz cualquier commit pequeño (o re-ejecuta el workflow desde **Actions →
   Re-run jobs**).
2. En **Actions** debe aparecer "Java CI" con un ✔️ verde.
3. En **SonarCloud** debe aparecer el análisis con la cobertura (~100%).

Esto cubre las evidencias pedidas: ejecución del pipeline + reporte de SonarCloud.

## Paso 6 — Demostrar un FALLO del pipeline y su corrección (para el video)

El reto pide mostrar un caso donde el pipeline falla por las pruebas y luego se
corrige. Hazlo así, idealmente mediante un **Pull Request** (evidencia en PRs):

1. Crea una rama: `git checkout -b demo-fallo` (o desde la web: **Branches**).
2. **Introduce un error a propósito** en `DiscountCalculator.java`. Por ejemplo,
   cambia el descuento premium grande de `0.20` a `0.25`:
   ```java
   if (premiumCustomer && amount >= 500) {
       return 0.25;   // <-- antes 0.20 (error introducido)
   }
   ```
   Esto hará fallar la prueba `shouldApplyPremiumDiscountForLargePurchase`
   (esperaba 800 y obtendrá 750).
3. Haz commit, push y abre un **Pull Request** hacia `main`.
4. En el PR verás el check en **rojo** ❌. Entra a **Actions** y muestra en el
   log el test que falla. **(Graba esto en el video.)**
5. **Corrige**: regresa el valor a `0.20`, haz commit y push a la misma rama.
6. El check ahora pasa a **verde** ✅. **(Graba esto también.)**
7. Haz **merge** del PR a `main`.

## Paso 7 — Grabar y subir el video a YouTube

Graba tu pantalla (OBS Studio, la grabadora de Windows `Win+G`, o similar)
mostrando, como mínimo:

- ✅ La ejecución automática del pipeline en GitHub Actions.
- ✅ La generación del análisis/reporte en SonarCloud.
- ✅ El pipeline fallando porque falla una prueba (Paso 6).
- ✅ La corrección del problema y el pipeline en verde.

No es obligatorio hablar ni poner texto. Sube el video a YouTube en modo
**Oculto / No listado** y copia el enlace.

## Paso 8 — Entrega

Copia y pega en la plataforma:

- 🔗 **Enlace del repositorio** en GitHub (debe estar público).
- 🔗 **Enlace del video** en YouTube.

---

## Solución de problemas

- **"You must define the following mandatory properties: sonar.projectKey..."**
  → Te faltó editar `sonar-project.properties` con tus claves reales (Paso 3).
- **El paso de SonarCloud falla con error de autenticación**
  → Revisa que el secreto se llame exactamente `SONAR_TOKEN` y que el token sea válido.
- **"Both Automatic Analysis and CI-based analysis are enabled"**
  → Desactiva Automatic Analysis en SonarCloud (Paso 3.6).
- **El build falla en "check" de JaCoCo (coverage)**
  → Significa que la cobertura bajó del 95%. Agrega pruebas o no toques las clases sin probar.
- **No aparece la pestaña Actions ejecutándose**
  → Verifica que el archivo esté en la ruta exacta `.github/workflows/ci.yml`.

## Notas técnicas

- El workflow instala Java 25 (Temurin); el código se compila con compatibilidad
  Java 21 (`maven.compiler.release=21`), lo cual es válido y compatible.
- JaCoCo exige 95% de cobertura de líneas por clase. Las pruebas actuales cubren
  el 100%, así que el build pasa.
