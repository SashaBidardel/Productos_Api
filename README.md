# 📦 ProductoAPI - Sistema de Gestión de Inventario

Esta es una API REST robusta construida con **Spring Boot 3**. El sistema permite gestionar un catálogo de productos y categorías, integrando un módulo de seguridad completo con autenticación, roles de usuario y un manejador global de excepciones.

---

## 🏗️ Arquitectura y Diseño

El proyecto sigue una arquitectura de **Capas** (Separación de responsabilidades):

* **Entidades (Model):** Uso de JPA para mapear tablas. Incluye una relación `@ManyToOne` entre Producto y Categoría.
* **Enums:** Implementación de `Role` (ADMIN, USER) para una gestión de permisos tipada y segura.
* **Repositorios:** Abstracción de datos con `JpaRepository`.
* **Servicios:** Capa de lógica de negocio donde se gestionan las reglas (ej: reasignación de productos al borrar categorías).
* **Controladores:** Endpoints REST que devuelven respuestas en formato JSON.
* **Manejo de Excepciones:** Uso de `@RestControllerAdvice` para capturar errores personalizados y devolver códigos HTTP coherentes (400, 404, 403).

---

## 🔐 Seguridad y Acceso

La API utiliza **Basic Auth** y es **Stateless**. Las contraseñas se almacenan cifradas con **BCrypt**.

### Usuarios Preconfigurados (DataInitializer)
Al arrancar, el sistema crea automáticamente estos usuarios:

| Usuario | Password | Rol | Permisos |
| :--- | :--- | :--- | :--- |
| `admin` | `1234` | **ADMIN** | Control total (CRUD completo) |
| `usuario` | `1234` | **USER** | Solo lectura (GET) |

También se crea la categoría "General" y unos productos de prueba (obviamente están en la categoria General que es la única 
y la predefinida por defecto en caso de no tener ninguna)
 
---

## 🚀 Guía de Pruebas en Postman

### 1. Registro de Usuarios (Abierto)
* **Endpoint:** `POST http://localhost:8080/api/registro`
* **Cuerpo (JSON):**
    ```json
    {
        "username": "nuevo_user",
        "password": "mi_password"
    }
    ```
* *Nota:* El sistema asigna automáticamente el rol `USER`.

### 2. Gestión de Productos (Requiere Auth)
* **Listar:** `GET /api/productos`
* **Crear (Solo Admin):** `POST http://localhost:8080/api/productos`
    * *JSON:* `{"nombre": "Raton", "precio": 20, "categoria": {"id": 1}}`
* **Borrar (Solo Admin):** `DELETE http://localhost:8080/api/productos/{id}`

### 3. Lógica Especial de Categorías
* **Borrar Categoría:** `DELETE http://localhost:8080/api/categorias/{id}`
    * **Regla 1:** Si intentas borrar la categoría "General" (ID 1), recibirás un `400 Bad Request`.
    * **Regla 2:** Si borras cualquier otra categoría, sus productos **no se borran**, se mueven automáticamente a "General".
* **Crear (Solo Admin):** `POST http://localhost:8080/api/categorias`
    * *JSON:* `{"nombre": "Hogar"}`
  
### 4. Logearse
* **Logearse** `POST http://localhost:8080/api/auth/login`
    * *JSON:* `{
    "username": "admin","password": "1234"}`
---

## 🛠️ Configuración e Instalación

1.  **Base de Datos:** Crea una base de datos en MySQL llamada `producto_db`.
2.  **Application Properties:**
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/producto_db
    spring.datasource.username=root
    spring.datasource.password=tu_password
    spring.jpa.hibernate.ddl-auto=update
    ```
3.  **Ejecución:**
    Ejecuta la clase `ProductoApiApplication.java` desde tu IDE. La API estará disponible en `http://localhost:8080`.

---

## ⚠️ Formato de Errores Personalizado
En caso de error (ej: buscar un ID que no existe), la API responderá siempre con este formato:
```json
{
    "error": "Error en la operación",
    "mensaje": "No se pudo encontrar el producto con ID: 99",
    "status": "404"
}