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
7. [Manejo de Errores](#manejo-de-errores)
8. [Cómo Extender el Proyecto](#cómo-extender-el-proyecto)
9. [Reglas del Juego](#reglas-del-juego)
10. [Troubleshooting](#troubleshooting)

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
- Reglas de Conway clásicas
- Muere si tiene < 2 o > 3 vecinos vivos
- Probabilidad de enfermarse: 25%
- Representación: `O`

**EstadoMuerto.java**
- Permanece muerto (célula neutra)
- Nunca cambia de estado (estable)
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

```java
// Correcto (DIP):
private Estado estadoActual;  // Depende de abstracción

// Incorrecto (sin DIP):
private EstadoVivo estadoActual;  // Depende de implementación
```

---

## Manejo de Errores

El proyecto implementa un manejo robusto de errores en múltiples niveles:

### 1. **Validación de Entrada del Usuario**
```java
// VistaJuego.java
private int leerEntero() {
    while (!scanner.hasNextInt()) {
        System.out.print("Por favor, ingrese un número válido: ");
        scanner.next();
    }
    return scanner.nextInt();
}
```
**Evita**: Excepciones por entrada no numérica

### 2. **Validación de Archivos**
```java
// CargadorTablero.java
if (!sc.hasNextInt()) {
    throw new Exception("Formato inválido: falta número de filas");
}
```
**Evita**: Archivos malformados silenciosos

### 3. **Validación de Posiciones**
```java
// Tablero.java
private boolean isPosValida(int nf, int nc) {
    return nf>=0 && nf<getFilas() && nc>=0 && nc<getColumnas();
}
```
**Evita**: Acceso a índices fuera de rango

### 4. **Captura y Reporte de Excepciones**
```java
try {
    this.tablero = CargadorTablero.cargarDesdeArchivo(ruta);
} catch (FileNotFoundException e) {
    System.err.println("Error: Archivo no encontrado");
} catch (Exception e) {
    System.err.println("Error: " + e.getMessage());
}
```
**Ventaja**: Mensaje claro al usuario

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

#### Paso 3: Compilar y Probar

```bash
javac -d bin src/modelo/*.java src/juego/*.java src/vista/*.java src/io/*.java
java -cp bin juego.JuegoDeLaVida
```

Crea un archivo `prueba.txt`:
```
5 5
O.R.O
.R...
R...R
O.R.O
...O.
```

Carga el archivo y ¡listo! **Sin cambiar otra clase del sistema.**

### Escenario 2: Modificar las Reglas del Juego

Para cambiar las reglas de `EstadoVivo` (p. ej., nueva regla: muere con menos de 1 vecino):

**Archivo**: `src/modelo/EstadoVivo.java`

```java
@Override
public Estado SigEstado(int vecinosVivos) {
    // Nueva regla: más estricta
    if(vecinosVivos < 1 || vecinosVivos > 3) {
        return new EstadoMuerto();
    }
    
    if(Math.random() < PROB_ENFER) {
        return new EstadoEnfermo();
    }
    
    return this;
}
```

**Ventaja**: Solo este archivo se recompila

### Escenario 3: Agregar una Nueva Capa de Presentación (GUI)

El diseño permite agregar una interfaz gráfica sin tocar `Tablero`, `Celda` o `Estado`:

```java
package gui;

import javax.swing.*;
import modelo.Tablero;

public class VistaGUI extends JFrame {
    private Tablero tablero;
    private JPanel panel;
    
    public void mostrarTablero() {
        // Implementar renderizado visual
        // Usa los mismos métodos de Tablero
    }
    
    public void iterarGeneracion() {
        // Usa tablero.avanzarGeneracion()
        // Redibuja
    }
}
```

**Punto importante**: `Tablero` no cambia. Solo se agrega una nueva vista.

### Escenario 4: Agregar Persistencia (Guardar/Cargar)

Extiende `CargadorTablero` con método para guardar:

```java
public static void guardarEnArchivo(Tablero tablero, String ruta) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
        pw.println(tablero.getFilas() + " " + tablero.getColumnas());
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                pw.print(tablero.getCelda(i, j).mostrar());
            }
            pw.println();
        }
    } catch (IOException e) {
        System.err.println("Error guardando: " + e.getMessage());
    }
}
```

**Clave**: El modelo no se toca, solo I/O

---

## Archivos de Ejemplo

### ejemplo1.txt
```
5 5
O.X.O
.O...
X...O
O.E.O
...O.
```

### ejemplo2.txt
```
10 10
..........
.O.O......
.O.O......
..........
....X.X...
....X.X...
..........
.....E....
..........
..........
```

Para usar los ejemplos en Windows:
```
Ingrese la ruta del archivo (ej: ejemplos/ejemplo1.txt): ejemplos/ejemplo1.txt
```

---

## Reglas del Juego

### Estados Estándar (Variante de Conway)
- **Célula Viva** (O):
  - Muere si tiene < 2 o > 3 vecinos vivos (soledad o sobrepoblación)
  - Sobrevive con 2 o 3 vecinos
  - Puede enfermar con 25% de probabilidad por generación

- **Célula Muerta** (.):
  - Permanece muerta (nunca nace)
  - Estado estable y pasivo

### Estados Extendidos (Variante del Proyecto)
- **Célula Enferma** (E):
  - Se considera viva para efectos del conteo de vecinos
  - Muere en la siguiente generación (siempre)
  - Añade dinamismo temporal

- **Célula Latente** (X):
  - Se considera muerta
  - Despierta (se vuelve viva) con exactamente 1 vecino vivo
  - Simula reproducción espontánea con baja presión

---

## Troubleshooting

### "Error: Archivo no encontrado"
**Causa**: Ruta incorrecta
**Solución**: Verifica que:
1. El archivo existe
2. La ruta es relativa desde donde ejecutas: `java -cp bin juego.JuegoDeLaVida`
3. Uso de barras: `ejemplos/ejemplo1.txt` (no `ejemplos\ejemplo1.txt` en algunos sistemas)

### "Error: Formato inválido"
**Causa**: Archivo mal formado
**Verificar**: 
- Primera línea tiene dos números: `5 5`
- Resto tienen caracteres válidos: `O`, `.`, `E`, `X`

```
5 5      ← Correcto
O.X.O    ← Válido
.O...    ← Válido
```

### "Compilación falla: Cannot find symbol"
**Verificar**:
1. Java 11+: `javac -version`
2. Todos los archivos presentes:
```bash
ls src/modelo/
ls src/juego/
ls src/vista/
ls src/io/
```

### Simulación muy lenta
**Causas posibles**:
- Tablero muy grande (> 100x100)
- Intervalo muy pequeño
**Soluciones**:
- Reducir tamaño del tablero
- Aumentar intervalo: `¿Intervalo? 100` (vs 1)

---

## Notas Técnicas

### Patrones de Diseño Utilizados
- **State Pattern** (Patrón Estado): `Celda` + `Estado` para transiciones
- **Strategy Pattern**: Múltiples estrategias de comportamiento
- **Template Method**: Flujo de ejecución en `VistaJuego`
- **Factory Pattern**: Creación de estados en `CargadorTablero`

### Características de Java Utilizadas
- **Interfaces**: Definición de contratos (`Estado`)
- **Switch Expression** (Java 14+): Mapeo elegante en `crearEstadoSegunCaracter()`
- **Try-with-resources**: Manejo automático de Scanner
- **Herencia e Implementación**: Polimorfismo en `Estado`

### Complejidad Computacional
- **Por generación**: O(f × c) donde f=filas, c=columnas
- **Conteo de vecinos**: O(1) constante (máximo 8 vecinos)
- **Memoria**: O(f × c) para matriz de celdas
- **Escalabilidad**: Lineal con tamaño del tablero

---

## Autores y Licencia

Proyecto desarrollado para **TUDAI - Programación II** - Tandil

---

## Preguntas Frecuentes

**P: ¿Cómo agrego un nuevo estado personalizado?**
A: Crea una clase que implemente `Estado` y actualiza `CargadorTablero.crearEstadoSegunCaracter()`.

**P: ¿Puedo cambiar las reglas de Conway?**
A: Sí, modifica los métodos `SigEstado()` en las clases de estado. Otros componentes no se ven afectados.

**P: ¿Funciona en tableros de 1000x1000?**
A: Sí, pero dependerá del hardware. La velocidad es lineal con el tamaño.

**P: ¿Cómo agrego una interfaz gráfica?**
A: Crea una nueva clase que use `Tablero` como antes. El modelo no cambia.

**P: ¿Puedo guardar estados?**
A: Actualmente no, pero podrías extender `CargadorTablero` con un método `guardarEnArchivo()`.

**P: ¿Qué pasa si un archivo tiene filas de diferente longitud?**
A: Se completa con células muertas (`.`) automáticamente.

---

**Última actualización**: Abril 2026

