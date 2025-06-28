Informe de Pruebas Unitarias - Proyecto Letras y Papeles Backend

1. Introducción

Este informe detalla el proceso de diseño, implementación, configuración y ejecución de pruebas unitarias para el proyecto "Letras y Papeles Backend". El objetivo principal es asegurar la calidad y el correcto funcionamiento de los componentes individuales del sistema, así como proporcionar una justificación clara de la metodología empleada y los resultados obtenidos.

2. Configuración del Entorno de Pruebas

El entorno de pruebas unitarias se configuró utilizando las siguientes herramientas y dependencias, gestionadas a través de Maven (`pom.xml`):

*   **JUnit 5 (JUnit Jupiter):** Framework principal para la escritura de pruebas unitarias. Se eligió por su flexibilidad, extensibilidad y soporte para características modernas de Java.
    ```xml
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.11.2</version>
        <scope>test</scope>
    </dependency>
    ```
*   **Mockito:** Framework de mocking utilizado para crear objetos simulados (mocks) de las dependencias de las clases bajo prueba. Esto permite aislar la unidad de código que se está probando, asegurando que las pruebas sean verdaderamente "unitarias" y no dependan de la lógica o el estado de otros componentes.
    ```xml
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>
    ```
*   **Spring Boot Starter Test:** Proporciona un conjunto completo de utilidades para probar aplicaciones Spring Boot, incluyendo soporte para JUnit, Mockito y otras librerías de aserción.
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    ```
*   **JaCoCo Maven Plugin:** Herramienta para generar informes de cobertura de código. Se integró en el ciclo de vida de Maven para automatizar la recolección de métricas de cobertura después de la ejecución de las pruebas. Esto es crucial para visualizar qué partes del código están siendo probadas y cuáles no.
    ```xml
    <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.8</version>
        <executions>
            <execution>
                <goals>
                    <goal>prepare-agent</goal>
                </goals>
            </execution>
            <execution>
                <id>report</id>
                <phase>post-integration-test</phase>
                <goals>
                    <goal>report</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ```
La combinación de estas herramientas asegura un entorno de pruebas robusto, que permite la creación de pruebas unitarias aisladas y la medición de su efectividad a través de la cobertura de código.

3. Diseño e Implementación de Pruebas Unitarias

La estrategia de pruebas unitarias se centró en cubrir las capas de servicio y controlador, así como las entidades clave del dominio.

*   **Pruebas de Entidad:** Se implementaron pruebas básicas para las entidades (ej. `ClienteTest.java`) para verificar el correcto funcionamiento de constructores, getters, setters, métodos `equals()` y `hashCode()`, y la lógica interna de la entidad (ej. `testAgregarRole`). Esto asegura que los objetos de dominio se comporten como se espera.

*   **Pruebas de Servicio (Service Layer):** Las pruebas en la capa de servicio (ej. `ClienteServiceTest.java`) se enfocaron en la lógica de negocio. Se utilizó Mockito para simular el comportamiento de las dependencias (como los repositorios y el `PasswordEncoder`), garantizando que solo la lógica del servicio esté bajo prueba. Se cubrieron escenarios de éxito (ej. `testObtenerClientePorId`, `testRegistrarCliente`, `testActualizarCliente`, `testEliminarCliente`) y escenarios de error (ej. `testRegistrarCliente_EmailYaExiste`), validando el manejo de excepciones y condiciones inesperadas.

*   **Pruebas de Controlador (Controller Layer):** Las pruebas en la capa de controlador (ej. `ClienteControllerTest.java`) se centraron en verificar que los endpoints de la API REST respondan correctamente a las solicitudes HTTP y que interactúen adecuadamente con la capa de servicio. Mockito se utilizó para simular el `ClienteService`, asegurando que las pruebas del controlador no dependan de la implementación real del servicio. Se validaron los códigos de estado HTTP (ej. `HttpStatus.OK`), los cuerpos de respuesta y la invocación correcta de los métodos del servicio.

Esta aproximación por capas permite una detección temprana de defectos y facilita la depuración, ya que cada componente se prueba de forma aislada.

4. Ejecución y Análisis de Resultados

Las pruebas unitarias se ejecutaron utilizando Maven. El comando para ejecutar todas las pruebas y generar el informe de cobertura es:

```bash
mvn test
```

Tras la ejecución, el plugin JaCoCo generó un informe de cobertura de código en el directorio `target/site/jacoco/`. El archivo `index.html` proporciona un resumen detallado de la cobertura por paquete y clase, incluyendo métricas como instrucciones cubiertas/perdidas, ramas, líneas, complejidad y métodos.

**Resultados de Cobertura (según `index.html`):**

El informe de JaCoCo mostró una cobertura del 100% en instrucciones, líneas, complejidad y métodos para la mayoría de las clases en los paquetes `com.letrasypapeles.backend.controller` y `com.letrasypapeles.backend.service`. Esto indica que la gran mayoría del código de negocio y de la API está siendo ejercitado por las pruebas unitarias.

Esta alta cobertura es un indicador fuerte de la calidad de las pruebas implementadas, asegurando que el código base es robusto y que los cambios futuros tienen menos probabilidades de introducir regresiones no detectadas.

5. Conclusiones

El proyecto "Letras y Papeles Backend" demuestra una sólida implementación de pruebas unitarias, con una excelente cobertura de código en las capas de servicio y controlador. La configuración del entorno de pruebas es adecuada y permite una ejecución eficiente y la generación de informes de cobertura.
