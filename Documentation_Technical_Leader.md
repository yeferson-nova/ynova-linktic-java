# Documentación Técnica del Sistema Backend

## 1. Visión General de la Arquitectura

El sistema se ha diseñado bajo los principios de una **Arquitectura Hexagonal (Puertos y Adaptadores)**, implementada en un ecosistema de **Microservicios**. Esta estructura busca desacoplar la lógica de negocio de los detalles de implementación externos.

### 1.1. Arquitectura Hexagonal
La arquitectura hexagonal aísla el núcleo del dominio de las tecnologías externas.

*   **Dominio (Core)**: Encapsula la lógica de negocio y las reglas del sistema. Es agnóstico a frameworks y bases de datos.
    *   `Inventory.java` en `com.ynova.inventory.domain.model`.
    *   `Product.java` en `com.ynova.product.domain.model`.
*   **Puertos (Ports)**: Interfaces que definen los contratos de interacción entrante (Casos de Uso) y saliente (Repositorios, Clientes Externos).
    *   *Puerto de Entrada*: `UpdateStockUseCase.java` en `com.ynova.inventory.domain.port.in`.
    *   *Puerto de Salida*: `InventoryRepositoryPort.java` en `com.ynova.inventory.domain.port.out`.
*   **Adaptadores (Adapters)**: Implementaciones concretas que conectan el núcleo con el mundo exterior (Controladores REST, Persistencia JPA, Clientes HTTP).
    *   *Adaptador de Entrada*: `InventoryController.java` en `com.ynova.inventory.infrastructure.adapter.in.web`.
    *   *Adaptador de Salida*: `InventoryRepositoryAdapter.java` en `com.ynova.inventory.infrastructure.adapter.out.persistence`.

**Justificación:**
Esta separación de responsabilidades permite que cambios en la infraestructura no impacten la lógica de negocio, alineándose con el principio de Inversión de Dependencias (DIP).

### 1.2. Microservicios
El sistema se descompone en servicios autónomos basados en Contextos Delimitados (Bounded Contexts):
*   **Product Service**: Responsable del ciclo de vida y catálogo de productos.
*   **Inventory Service**: Responsable del control de stock y reglas de validación de inventario.

**Justificación:**
Facilita el escalado independiente y el despliegue aislado de cada componente.

---

## 2. Patrones de Diseño y Decisiones Técnicas

La implementación utiliza patrones de diseño para estructurar la solución y resolver problemas de interacción entre componentes.

### 2.1. Strategy Pattern (Patrón Estrategia)
*   **Implementación**:
    *   Interfaz: `StockValidationStrategy.java` en `com.ynova.inventory.domain.strategy`.
    *   Concreción: `DefaultStockValidationStrategy.java` en `com.ynova.inventory.domain.strategy`.
*   **Decisión Arquitectónica**: Mecanismo para validar el stock sujeto a reglas variables.
*   **Beneficio**: Permite intercambiar algoritmos de validación sin modificar el cliente que los utiliza, siguiendo el principio **Open/Closed (OCP)**.

### 2.2. Repository Pattern (Patrón Repositorio)
*   **Implementación**:
    *   Interfaz de Dominio: `ProductRepositoryPort.java` en `com.ynova.product.domain.port.out`.
    *   Implementación de Infraestructura: `ProductRepositoryAdapter.java` en `com.ynova.product.infrastructure.adapter.out.persistence`.
*   **Decisión Arquitectónica**: Abstracción de la capa de persistencia.
*   **Beneficio**: El dominio opera sobre una colección en memoria conceptual. Facilita las pruebas unitarias y permite cambiar el motor de base de datos con impacto reducido.

### 2.3. Data Transfer Object (DTO)
*   **Implementación**: `ProductDto.java` en `com.ynova.inventory.infrastructure.adapter.out.client`.
*   **Decisión Arquitectónica**: Desacoplamiento del modelo de dominio de la capa de presentación y comunicación.
*   **Beneficio**: Evita la exposición de la estructura interna del dominio y permite versionar la API independientemente del modelo de datos.

### 2.4. Builder Pattern (Patrón Constructor)
*   **Implementación**: Uso de Lombok `@Builder` en entidades de dominio y `ApiResponse.java` en `com.ynova.core.api`.
*   **Decisión Arquitectónica**: Construcción de objetos complejos.
*   **Beneficio**: Mejora la legibilidad en la instanciación de objetos y facilita la creación de instancias inmutables.

### 2.5. Observer / Event-Driven Architecture
*   **Implementación**:
    *   Evento: `InventoryLowEvent.java` en `com.ynova.inventory.domain.event`.
    *   Publicador: `ApplicationEventPublisher` invocado desde `UpdateStockService.java` en `com.ynova.inventory.application.usecase`.
*   **Decisión Arquitectónica**: Desacoplamiento de efectos secundarios (notificaciones).
*   **Beneficio**: El servicio de actualización de stock no requiere conocimiento de los consumidores del evento, reduciendo el acoplamiento.

### 2.6. Facade Pattern (Implícito en Controladores)
*   **Implementación**: `InventoryController.java` en `com.ynova.inventory.infrastructure.adapter.in.web`.
*   **Decisión Arquitectónica**: Interfaz unificada para el consumo de casos de uso.
*   **Beneficio**: Oculta la complejidad de la orquestación de servicios y adaptadores a los clientes de la API.

---

## 3. Observabilidad y Manejo de Errores

### 3.1. MDC (Mapped Diagnostic Context)
*   **Implementación**: `MDCFilter.java` en `com.ynova.core.logging`.
*   **Motivo de la Decisión**: Necesidad de rastrear peticiones individuales a través de múltiples capas y servicios en un entorno distribuido.
*   **Justificación**: En sistemas concurrentes y microservicios, los logs se entremezclan. Sin un identificador único, es imposible correlacionar logs de una misma transacción.
*   **Uso**: Se intercepta cada petición HTTP entrante mediante un filtro (`Filter`), generando un `traceId` único que se inyecta en el contexto del hilo (ThreadLocal) gestionado por SLF4J/Logback. Este ID se incluye automáticamente en cada línea de log generada durante el ciclo de vida de la petición.
*   **Descentralización**: Permite que servicios independientes y distribuidos compartan un contexto de rastreo (propagando el ID vía headers HTTP), facilitando la depuración distribuida sin acoplar los servicios a un sistema de monitoreo centralizado específico en tiempo de compilación.

### 3.2. Manejo de Errores Centralizado
*   **Implementación**: `GlobalExceptionHandler.java` en `com.ynova.core.exception`.
*   **Motivo de la Decisión**: Evitar la duplicación de lógica `try-catch` en cada controlador y garantizar respuestas de error uniformes.
*   **Justificación**: Un manejo de errores inconsistente dificulta el consumo de la API por parte de clientes (frontend, otros servicios). Centralizar esta lógica mejora la mantenibilidad y la experiencia del desarrollador.
*   **Uso**: Se utiliza `@RestControllerAdvice` de Spring para capturar excepciones lanzadas en cualquier parte de la pila de ejecución. Las excepciones de negocio (`BusinessException`) se transforman en respuestas estructuradas con códigos de error específicos, mientras que las excepciones no controladas se manejan como errores genéricos (500), asegurando que nunca se expongan stack traces al cliente.
*   **Descentralización**: Aunque la lógica es centralizada *dentro* del servicio (o compartida vía librería `lib-core`), permite que cada microservicio maneje sus propias excepciones de dominio de manera autónoma pero respetando un contrato de respuesta estándar (JSON con `code`, `message`, `traceId`), facilitando la integración en un ecosistema descentralizado.

---

## 4. Estándares de Calidad

El código fuente sigue estándares técnicos de desarrollo.

### 4.1. Código Autodocumentado
Se ha prescindido de comentarios en el código fuente, priorizando la legibilidad a través de la estructura del código:
*   **Nombres Expresivos**: Clases, métodos y variables con nombres descriptivos.
*   **Métodos Pequeños**: Funciones con responsabilidad única.
*   **Flujo Claro**: Lógica lineal.

### 4.2. Principios SOLID
*   **SRP**: Responsabilidad única por clase.
*   **OCP**: Sistema abierto a la extensión y cerrado a la modificación.
*   **LSP**: Cumplimiento de contratos en interfaces.
*   **ISP**: Segregación de interfaces.
*   **DIP**: Dependencia de abstracciones.

---

## 5. Guía de Instalación y Ejecución

Pasos detallados para descargar y ejecutar el proyecto en un entorno local.

### Prerrequisitos
*   Java 17 o superior
*   Maven 3.8+
*   Docker y Docker Compose
*   Git

### Paso 1: Clonar el Repositorio
Descarga el código fuente desde el repositorio oficial.
```bash
git clone <URL_DEL_REPOSITORIO>
cd ynova-linktic-java
```

### Paso 2: Compilar el Proyecto
Navega al directorio raíz (`backend-solution`) y compila los módulos usando Maven. Esto generará los artefactos `.jar` necesarios.
```bash
cd backend-solution
mvn clean package -DskipTests
```
*Nota: Se omiten los tests (`-DskipTests`) para agilizar la primera construcción, pero se recomienda ejecutarlos (`mvn test`) para verificar la integridad.*

### Paso 3: Ejecutar con Docker Compose
El proyecto incluye un archivo `docker-compose.yml` que orquesta los servicios (Base de datos, Product Service, Inventory Service).

```bash
docker-compose up --build -d
```
*   `--build`: Fuerza la construcción de las imágenes Docker.
*   `-d`: Ejecuta los contenedores en segundo plano (detached mode).

### Paso 4: Verificar el Despliegue
Asegúrate de que todos los contenedores estén corriendo y saludables.
```bash
docker-compose ps
```
Deberías ver los servicios `product-service`, `inventory-service` y `postgres` en estado `Up`.

### Paso 5: Probar los Servicios
Puedes acceder a los endpoints principales:

*   **Product Service**: `http://localhost:8080/api/v1/products`
*   **Inventory Service**: `http://localhost:8081/api/v1/inventory`

Ejemplo de prueba con `curl`:
```bash
# Crear un producto
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Laptop", "price": 1200.00, "description": "High performance laptop"}'
```

---

## 6. Conclusión

El proyecto `ynova-linktic-java` implementa una solución de backend basada en microservicios y arquitectura hexagonal. La estructura y los patrones de diseño aplicados tienen como objetivo facilitar la escalabilidad, el mantenimiento y la testabilidad del sistema.
