# Juego de la Vida - TPE Final Libre - Programacion 2 TUDAI - Tandil
## Descripción del Proyecto
Este proyecto se crea a partir de la consigna del TPE Libre - Juego de la vida (2026).pdf.

## Índice
1. [Estructura del Proyecto](#estructura-del-proyecto)
2. [Requisitos](#requisitos)
3. [Compilación](#compilación)
4. [Ejecución](#ejecución)
5. [Arquitectura y Diseño](#arquitectura-y-diseño)
6. [Principios SOLID](#principios-solid-aplicados)
7. [Cómo Extender el Proyecto](#cómo-extender-el-proyecto)

---

## Estructura del Proyecto

```
JuegoDeLaVida/
├── src/
│   ├── module-info.java          # Configuración del módulo
│   ├── juego/
│   │   └── JuegoDeLaVida.java    # Punto de entrada de la aplicación
│   ├── modelo/
│   │   ├── Tablero.java          # Lógica del tablero de juego
│   │   ├── Celda.java            # Representación de una célula
│   │   ├── Estado.java           # Interfaz para patrones de estado
│   │   ├── EstadoVivo.java       # Implementación: célula viva
│   │   ├── EstadoMuerto.java     # Implementación: célula muerta
│   │   ├── EstadoEnfermo.java    # Implementación: célula enferma
│   │   └── EstadoLatente.java    # Implementación: célula latente
│   ├── vista/
│   │   └── VistaJuego.java       # Interfaz CLI del usuario
│   └── io/
│       └── CargadorTablero.java  # Carga estados desde archivo
├── bin/                          # Archivos compilados (.class)
└── ejemplos/                     # Archivos de configuración de ejemplo
    ├── ejemplo1.txt
    └── ejemplo2.txt
    └── ejemplo3.txt

```

---

## Requisitos

- **Java**: JDK 11 o superior
- **Sistema Operativo**: Windows, macOS, Linux
- **Compilador**: `javac` (incluido en JDK)

---

## Compilación

### Opción 1: Compilación Manual desde Línea de Comandos

```bash
cd JuegoDeLaVida
javac -d bin src/modelo/*.java src/juego/*.java src/vista/*.java src/io/*.java src/module-info.java
```

### Opción 2: Usando IDE (IntelliJ IDEA, Eclipse, etc.)

1. Abre el proyecto en tu IDE
2. Marca la carpeta `src` como source folder
3. Ejecuta la opción "Build Project"

---

## Ejecución

### Desde Línea de Comandos

```bash
cd JuegoDeLaVida
java -cp bin juego.JuegoDeLaVida
```

### Desde IDE

1. Abre el archivo `JuegoDeLaVida.java`
2. Ejecuta el método `main()` (botón de play o tecla F5)

### Opciones de Ejecución

Al iniciar, el programa ofrece dos modalidades:

#### 1. **Cargar desde Archivo**
```
Ingrese la ruta del archivo (ej: ejemplos/ejemplo1.txt):
```
El archivo debe tener el siguiente formato:
```
<filas> <columnas>
O.X.O
.O...
X...O
O.E.O
...O.
```

Caracteres válidos:
- `O` - Célula viva
- `.` - Célula muerta
- `E` - Célula enferma
- `X` - Célula latente


- **Nota sobre el Formato**: El programa es insensible a mayúsculas/minúsculas al leer archivos, y cualquier carácter no reconocido será tratado automáticamente como una célula muerta '.'.


#### 2. **Generación Aleatoria**
```
Ingrese filas: 10
Ingrese columnas: 15
```
Genera un tablero aleatorio con 30% de células vivas.

### Controles Durante la Simulación

```
¿Cuántas generaciones ejecutar? (0 para indefinido): 50
Intervalo entre pasos (1s=1000ms): 500
```

- `0 generaciones` = ejecuta indefinidamente hasta estabilización
- `Intervalo en ms` = velocidad de la simulación

---

## Arquitectura y Diseño

### Separación Modelo-Vista

El proyecto implementa una arquitectura limpia que separa claramente la lógica del juego de su presentación:

```
┌─────────────────────────────────────────────────┐
│           CAPA DE PRESENTACIÓN (CLI)           │
│              VistaJuego.java                   │
│  - Interfaz de usuario                         │
│  - Lectura de entrada                          │
│  - Coordinación del flujo                      │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│         CAPA DE APLICACIÓN                     │
│         CargadorTablero.java                   │
│  - Parseo de archivos                          │
│  - Manejo de I/O                               │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│         CAPA DE MODELO (Lógica Pura)          │
│      Tablero, Celda, Estado                    │
│  - Algoritmo del juego                         │
│  - Cálculo de generaciones                     │
│  - Reglas y transiciones de estado             │
└─────────────────────────────────────────────────┘
```

### Clases Principales y Responsabilidades

#### 1. **Tablero.java** - Orquestación del Juego
- **Responsabilidad**: Gestionar el estado global del tablero y la evolución de generaciones
- **Métodos clave**:
  - `avanzarGeneracion()`: Calcula la siguiente generación
  - `contarVecinosVivos(int fila, int col)`: Cuenta células vivas adyacentes
  - `mostrar()`: Imprime el tablero
  - `setCelda()` / `getCelda()`: Acceso a células específicas

**Lógica de evolución**:
```java
// Para cada celda:
// 1. Contar vecinos vivos
// 2. Calcular siguiente estado (delegado a Estado)
// 3. Aplicar cambios simultáneamente
// 4. Reportar si hubo cambios
```

#### 2. **Celda.java** - Unidad Atómica
- **Responsabilidad**: Representar una célula individual y su evolución
- **Métodos clave**:
  - `calcularSig(int vecinosVivos)`: Delega a Estado para calcular siguiente
  - `evolucionar()`: Transiciona al siguiente estado
  - `isViva()`: Consulta si la célula está viva

**Patrón**: Actúa como contexto en el patrón State

#### 3. **Estado.java (Interfaz)** - Patrón Strategy
- **Responsabilidad**: Definir el contrato para comportamientos de estado
- **Métodos**:
  - `boolean isViva()`: ¿Es una célula viva?
  - `char getRepresentacion()`: Símbolo visual
  - `Estado SigEstado(int vecinosVivos)`: Calcula transición

**Ventaja**: Nueva lógica sin modificar `Celda` o `Tablero`

#### 4. **Implementaciones de Estado**

**EstadoVivo.java**
- Reglas:
- Muere si tiene < 2 o > 3 vecinos vivos
- Probabilidad de enfermarse: 25%
- Representación: `O`

**EstadoMuerto.java**
- Reglas:
- Se vuelve viva con exactamente 3 vecinos vivos (reproducción)
- Permanece muerta en cualquier otro caso
- Representación: `.`

**EstadoEnfermo.java**
- Se considera viva
- Muere en la siguiente generación
- Estado transitorio para agregar dinámica
- Representación: `E`

**EstadoLatente.java**
- Se considera muerta
- Despierta con exactamente 1 vecino vivo
- Simula períodos de letargo
- Representación: `X`

#### 5. **VistaJuego.java** - Interfaz de Usuario
- **Responsabilidad**: Orquestar la interacción con el usuario
- **Métodos clave**:
  - `iniciar()`: Menú principal
  - `configurarArchivo()`: Carga desde archivo
  - `configurarManual()`: Generación aleatoria
  - `bucleDeEjecucion()`: Loop principal de simulación
  - `leerEntero()`: Validación de entrada

**Flujo**:
```
1. Mostrar menú
2. Usuario elige carga de archivo o aleatorio
3. Se crea Tablero
4. Loop: mostrar → avanzar → esperar
```

#### 6. **CargadorTablero.java** - Entrada/Salida
- **Responsabilidad**: Parsear archivos de configuración
- **Métodos clave**:
  - `cargarDesdeArchivo(String ruta)`: Carga tablero desde archivo
  - `crearEstadoSegunCaracter(char c)`: Mapeo de caracteres a estados

**Validaciones**:
- Verifica filas y columnas
- Maneja archivos con líneas cortas (completa con muertos)
- Lanza excepciones descriptivas

---

## Principios SOLID Aplicados

### 1. **Single Responsibility Principle (SRP)**
Cada clase tiene una única responsabilidad bien definida:
- `Tablero` → Gestión del tablero y evolución
- `Celda` → Representación de célula y delegación
- `Estado` → Comportamiento específico del estado
- `VistaJuego` → Presentación e interacción
- `CargadorTablero` → Entrada/Salida

**Beneficio**: Cambios en un aspecto no afectan otros

### 2. **Open/Closed Principle (OCP)**
El sistema es abierto para extensión pero cerrado para modificación:
- Nuevos estados pueden crearse implementando la interfaz `Estado`
- Sin cambiar código existente en `Tablero` o `Celda`
- El cargador se actualiza de forma aislada

**Ejemplo**: Agregar `EstadoRoboto` no requiere recompilar lógica

### 3. **Liskov Substitution Principle (LSP)**
Todas las implementaciones de `Estado` son intercambiables:
```java
Estado estado1 = new EstadoVivo();
Estado estado2 = new EstadoEnfermo();
Estado estado3 = new EstadoLatente();
// Todas pueden usarse en Celda sin problemas
```

**Garantía**: Contrato consistente

### 4. **Interface Segregation Principle (ISP)**
La interfaz `Estado` define solo lo necesario:
```java
public interface Estado {
    boolean isViva();
    char getRepresentacion();
    Estado SigEstado(int vecinosVivos);
}
```

**No incluye métodos innecesarios**: Cada clase implementa lo que necesita

### 5. **Dependency Inversion Principle (DIP)**
- `Tablero` depende de la abstracción `Estado`, no de implementaciones
- `Celda` recibe `Estado` en su constructor (inyección)
- La lógica está desacoplada
- El Modelo es independiente de la configuración inicial.

```java
// Correcto (DIP):
private Estado estadoActual;  // Depende de abstracción

// Incorrecto (sin DIP):
private EstadoVivo estadoActual;  // Depende de implementación
```

---

## Cómo Extender el Proyecto

### Escenario 1: Agregar un Nuevo Estado

Supongamos que quieres agregar un estado `EstadoRoboto` que:
- Es considerado "vivo"
- Se representa con `R`
- Muere si tiene más de 4 vecinos vivos (resiste mejor que vivo)

#### Paso 1: Crear la Nueva Clase de Estado

Crea `EstadoRoboto.java` en `src/modelo/`:

```java
package modelo;

public class EstadoRoboto implements Estado {
    private final int LIMITE_VECINOS = 4;

    @Override
    public boolean isViva() {
        return true;  // Es considerado vivo
    }

    @Override
    public char getRepresentacion() {
        return 'R';  // Símbolo visual
    }

    @Override
    public Estado SigEstado(int vecinosVivos) {
        if (vecinosVivos > LIMITE_VECINOS) {
            return new EstadoMuerto();  // Muere por sobrepoblación
        }
        return this;  // Sobrevive
    }
}
```

#### Paso 2: Actualizar el Cargador

Modifica `CargadorTablero.java` en el método `crearEstadoSegunCaracter()`:

```java
private static Estado crearEstadoSegunCaracter(char c) {
    return switch (Character.toUpperCase(c)) {
        case 'O' -> new EstadoVivo();
        case 'E' -> new EstadoEnfermo();
        case 'X' -> new EstadoLatente();
        case 'R' -> new EstadoRoboto();      // ← NUEVA LÍNEA
        case '.' -> new EstadoMuerto();
        default  -> new EstadoMuerto();
    };
}
```

---

## Notas Técnicas

### Patrones de Diseño Utilizados
- **State Pattern** (Patrón Estado): `Celda` + `Estado` para transiciones
- **Template Method**: Flujo de ejecución en `VistaJuego`
- **Factory Pattern**: Creación de estados en `CargadorTablero`

---

## Preguntas Frecuentes

**P: ¿Cómo agrego un nuevo estado personalizado?**
A: Crea una clase que implemente `Estado` y actualiza `CargadorTablero.crearEstadoSegunCaracter()`.

**P: ¿Puedo cambiar las reglas**
A: Sí, modifica los métodos `SigEstado()` en las clases de estado. Otros componentes no se ven afectados.

**P: ¿Qué pasa si un archivo tiene filas de diferente longitud?**
A: Se completa con células muertas (`.`) automáticamente.

---

**Última actualización**: Abril 2026

