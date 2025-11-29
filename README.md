# Gestor de Archivos para Android

Aplicación Android desarrollada para explorar, visualizar y gestionar archivos en el almacenamiento interno y externo del dispositivo. Incluye soporte para lectura de varios tipos de archivos, navegación intuitiva, temas personalizados y funciones completas de manipulación de archivos con enfoque en seguridad y rendimiento.

---

## Funcionalidades Principales

###  Exploración de archivos
- Navegación por almacenamiento interno y externo *(si disponible)*
- Visualización jerárquica de carpetas y archivos
- Barra de navegación tipo **breadcrumbs** para directorios
- Metadatos de archivos: tamaño, fecha y tipo

###  Soporte de visualización de archivos
- Archivos de texto: `.txt`, `.md`, etc.
- Archivos JSON y XML con formato legible
- Imágenes con zoom y rotación
- Archivos no soportados ➝ opción *Abrir con otra aplicación*

###  Interfaz de Usuario
- Temas personalizables:
  - **Guinda (IPN)**
  - **Azul (ESCOM)**
- Adaptación automática a modo claro/oscuro del sistema
- Diseño responsivo para distintos tamaños de pantalla

###  Almacenamiento Local
- Historial de archivos recientes
- Sistema de favoritos (SharedPreferences o Room)
- Caché de miniaturas de imágenes para mejor rendimiento
- Operaciones de archivo: copiar, mover, renombrar, eliminar

###  Permisos y Seguridad
- Gestión adecuada de permisos de almacenamiento
- Manejo de excepciones para rutas protegidas/inaccesibles
- Cumplimiento de políticas de acceso de Android
- Validación para operaciones de escritura y eliminación

---

##  Tecnologías y Herramientas

| Categoría | Tecnología |
|---|---|
Lenguaje | Kotlin / Java  
Arquitectura | MVVM / MVP *(según implementación)*  
Persistencia | SharedPreferences / Room  
UI | Jetpack Compose / XML *(según implementación)*  
Cache | LruCache / FileCache  
Gestión de permisos | AndroidX Activity / Jetpack APIs  

---

##  Instalación y Ejecución
---
En este caso los puntos importantes a considerar son los elementos que me permitieron el funcionamiento de mi código.
Primero tengo que hacer modificaciones en mi AndroidManifest.xml, en el cual tengo que brindar permisos para que el almacenamiento se guarde una vez salga de la aplicación.

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/01eae5b4-8c44-4cb7-9408-a833b4a773f2" />

Al tener esto tambien tengo que abrir los permisos para los archivos que tengo dentro de mi dispositivo móvil. Esto se logra gracias al FileProvider.

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/b4e41879-51ca-4e88-a6c6-708599875f5a" />
De ahí en adelante se hacen las especificaciones del Activity con la que voy a trabajar.

Luego creo una carpeta de "datos" en el que se tiene el almacenamiento para la base de datos que me ayudara a guardar mis archivos que son favoritos y los más recientes.
Primero tengo mi AppDatabase.kt:
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/11204bdf-4709-4a98-a205-c5043aeafbfb" />

Como segundo paso importante creo un FileDao.kt:
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/068e0a07-bf40-4b29-ad66-e87bbc76b518" />

Finalmente creo un FileEntity:
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/bfc31858-bfb2-4cc7-925d-d5a722e5b079" />

Para el manejo de los Fragments lo hago de la misma manera que en las practicas anteriores, pero adaptandolo a lo solicitado.
Sin embargo, implemente un nuevo paquete para la opcion de visualizacion de imagenes y archivos (Viewer):

ImageViewerActivity.kt.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/92015a5a-2bc4-4fc3-a4e4-0b78387b96d1" />

TextViewerActivity.kt.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/30301686-45cb-465c-b2e6-c529d7422dc5" />

De ahí en adelante el manejo de los archivos xml, pero es impornate que se muestren los colores que nos indico el profesor.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/5c3df7d6-1bec-459b-99c0-2a196bb70f3b" />

Así mismo el manejo del tema claro.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/14fe4ad4-80d9-461c-bc64-13376a848cc6" />

Y tema oscuro.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/49d3881e-74cd-47af-b030-aee7e24b2f3f" />

## Funcionamiento de la aplicación
---
Al momento de ingresar tengo la selección de las carpetas que tiene mi dispositivo.
![Imagen de WhatsApp 2025-11-08 a las 22 55 19_2844e884](https://github.com/user-attachments/assets/4ce328d0-f72b-4ca1-81d6-72209ebfe83d)

Luego selecciono en este caso mi capeta de fotos y de ahi se ven todas mis carpetas.
![Imagen de WhatsApp 2025-11-08 a las 22 55 19_82aec361](https://github.com/user-attachments/assets/433b13ec-c673-4e49-b07d-0e09cdab248b)

Cuando tengo mi carpeta seleccionada, busco mis imagenes donde me lanzara un mensaje de aprobación para el acceso a archivos.
![Imagen de WhatsApp 2025-11-08 a las 22 55 19_f4658c04](https://github.com/user-attachments/assets/eadb5d1a-716d-4ca4-ab8d-936e5528d325)

Al seleccionar mi carpeta podre visualizar todos mis archivos.
![Imagen de WhatsApp 2025-11-08 a las 22 55 19_bdd2b6e7](https://github.com/user-attachments/assets/1674a7db-102d-4c37-b662-80eca9cd4db3)

Tengo tambien la opcion de cambiar la vista que hace que mis archivos se vean de diferente manera.
![Imagen de WhatsApp 2025-11-08 a las 22 55 19_b3b61beb](https://github.com/user-attachments/assets/28ec996d-a412-46a7-9ce9-1c59d0eb33e9)

En la sección de Recientes o favotitos se vera los archivos correspondientes a los antes mencionados.
![Imagen de WhatsApp 2025-11-08 a las 22 55 20_6e7f8c45](https://github.com/user-attachments/assets/903912b3-8642-453b-8465-0a964ad9d937)

Si ingreso a alguna imahenn podre hacer zoom o compartir.
![Imagen de WhatsApp 2025-11-08 a las 22 55 20_95231b96](https://github.com/user-attachments/assets/1ad523ca-2974-41c8-8b10-5b47a192573b)

En la sección de Tema de cambio, puedo ver los botones del cambio.
![Imagen de WhatsApp 2025-11-08 a las 22 55 20_c4dfbb91](https://github.com/user-attachments/assets/164e3952-78b4-4f46-a424-8c271969fd14)

Si selecciono el boton guinda puedo pasar a mi modo claro.
![Imagen de WhatsApp 2025-11-08 a las 22 55 20_8ffffd12](https://github.com/user-attachments/assets/f2690ddf-09b6-422f-92d8-b77a27d47dee)

Veo la parte de los cambios en mi pantalla de Recientes o Favoritos.
![Imagen de WhatsApp 2025-11-08 a las 22 55 21_ca1711da](https://github.com/user-attachments/assets/8a98b0b6-7f8f-4785-b822-65e4e07141df)

Luego asigno una imagen de favoritos y se ve este archivo dentro de la lista.
![Imagen de WhatsApp 2025-11-08 a las 22 55 21_2c2c1271](https://github.com/user-attachments/assets/c0f048d8-04ac-48f1-9c36-e8c3830300fd)


## Práctica 6 Manejo de sensores del dispositivo móvil
---

En este caso se nos solicito cumplir con almenos un ejercicio de los solicitados por el profesor. En nuestro caso seleccionamos el primer ejercicio de implementar los sensores.
Como estamos utilizando la aplicación del gestor de archivos entonces uno de los sensores obligatorios es el de la autenticación biométrica para acceder a carpetas protegidas. Esto se refiere a que tenemos que ingresar nuestra huella deactilar para poder ingresar a la carpeta de archivos previamente seleccionada.

Por tal motivo se mencionan los cambios más significativos que hice en mi proyecto.
En el caso del AndroidManifest.xml, agregue dentro de los permisos el uso de datos biometricos.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/0a178525-7c9c-41a6-910f-eae48b4baab3" />

Al tener estos permisos me dirijo a mi buil.gradle.kts (Module:app), ya que de esta manera podre sincronizar mi proyecto con Gradle Files y no presentar error al momento de  ingresarlo en mi AndroidManifest.

Es importante mencionar que este se coloca dentro de mis dependencias.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2e6f8253-711f-401b-ac4c-d976a97f6f1d" />

Al tener estas implemetaciones correctas entonces ya puedo empezar a modificar mis códigos antes desarrollados.
Primero tengo el DashboardFragment.kt.

Verificamos si el contexto es válido antes de checar biometría
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/d0350e64-c2f9-413c-8bc8-8ad8f4d6adbc" />

Creo mi funcion de checar la compatibilidad con el dato biometrico.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/d8ae4a1f-7867-4743-a251-1a7332cc01bf" />

En caso de que su huella no sea identificada se genera los siguientes casos.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/227eba9a-7955-43a6-9c8b-e099f3be523e" />

Estos serian los cambios principales que implemente en mi DashboardFragment.kt, ahora veremos cuales fueron los cambios que realice en el archivo xml, de este mismo apartado.
En este caso solo implemente un cuadro de texto que tiene el mensaje de accesso bloquedo.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/86236089-a259-4297-92c1-8f73ae8b4129" />

De esta forma finalizo todo lo correspondiente al dato biomedico.
Para el siguiente sensor que realice fue el de Sensor de Luz Ambiental.
El cual mide la intensidad de la luz en el entorno. En la app muestra el valor en Lux (por ejemplo, si tapas el sensor superior del teléfono, el número baja, si lo pones bajo una lámpara este valor subira).

En mi código de NotificationsFragment.kt, importo mi MainActivity para acceder a los datos estáticos y de esta forma puedo manejar mis funcionalidades.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/84642d21-e07f-4cc8-bd16-37b270cedfed" />

Para la adaptacion al cambio de tema ingrese el valor que se tiene dentro del sistema.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/626bb3e3-b69e-4c28-9a2c-00d49dedd4fc" />

Finalmente para el ultimo sensor seleccione el del acelerómetro que mide la fuerza de aceleración aplicada al dispositivo en los tres ejes físicos (X, Y, Z), incluyendo la gravedad.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/08a5f67b-e30d-43e2-88bb-b82a294b3532" />

Para mi archivo xml tengo las siguientes modificaciones.

El titulo de los sensores utilizados.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/4a39f80e-400b-4642-b79e-eb8eacefeb4f" />

Los datos de luz.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/15cd3831-29a2-487c-bb3a-9b1dad86d55e" />

Y finalmente los datos de acelerometro.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/26a0b24d-6b51-467d-9ae5-3ffc3704aa8e" />

Una vez que se lograron realizar los cambios pertinentes para el cumplimiento de la practica 6, podremos visualizar en nuestra  aplicación la implementación de los sensores.

Los cambios que implementamos es en la pantalla de "Recientes o Favoritos", donde no podremos ingresar de forma inmediata sino que antes se solicita ingresar por el escaner de huella digital. Es importante mecional, que en esta parte no permite la toma de captiras de pantalla por seguridad.
![Imagen de WhatsApp 2025-11-29 a las 14 17 42_b47d91ae](https://github.com/user-attachments/assets/b93bebd2-745b-4582-828b-fe79b9b42628)

Aquí se observa como solicita ingresar la huella digital.
![Imagen de WhatsApp 2025-11-29 a las 14 18 26_a4449703](https://github.com/user-attachments/assets/f6e1fb10-055b-4d20-8f94-a25848e7921f)

Al ingresar la huella digital, te muestra el despliegue de los archivos de tus documentos favoritos.
![Imagen de WhatsApp 2025-11-29 a las 14 17 42_0ff45f25](https://github.com/user-attachments/assets/4dcabaa7-6ab8-4cb8-a277-6a48338adcc9)

En la pantalla de "Temas de cambio", se ve la implementación de los sensores. En este caso tanto la luz ambiental y el acelerómetro estan activados y se ve el cambio de valores en tiempo real.
![Imagen de WhatsApp 2025-11-29 a las 14 17 43_b4908e31](https://github.com/user-attachments/assets/143ee06a-2e23-4054-867e-9ba594bd1aa3)

Aqui deshabilito el sensor de "Luz Ambiental".
![Imagen de WhatsApp 2025-11-29 a las 14 17 44_ef857136](https://github.com/user-attachments/assets/c6deb219-738e-4fef-a204-10f2af726bf1)

Y aqui deshabilito el sensor de "Acelerómetro".
![Imagen de WhatsApp 2025-11-29 a las 14 17 44_44d37de5](https://github.com/user-attachments/assets/b693f5b3-a0ee-46e4-90fd-ed0335d9bd7f)

Con el boton de tema azul se cambio del color en la barra de navegación.
![Imagen de WhatsApp 2025-11-29 a las 14 17 44_661a29c3](https://github.com/user-attachments/assets/a7bc1a33-01e2-471a-a915-c449a97c00d0)

En el caso del boton de "Adaptación automática" se adapatara dependiendo si en mi telefono tengo el modo oscuro o claro. Para esta prueba mi telefono se encuentra en el modo oscuro.
![Imagen de WhatsApp 2025-11-29 a las 14 17 45_6eac1fdf](https://github.com/user-attachments/assets/2f46a89a-00b0-4036-a44d-a30a507e5a62)

Entonces al momento de dar clic sobre el boton de forma automatica nos mueve al modo oscuro.
![Imagen de WhatsApp 2025-11-29 a las 14 17 45_1eb0b923](https://github.com/user-attachments/assets/2a9c2cd7-b5c3-44c9-a267-f878c0a2aadb)

Para ver que si funciona en la pantalla de autenticación en modo oscuro, lo compruebo desde otro telefono.
![Imagen de WhatsApp 2025-11-29 a las 14 18 39_58ed8964](https://github.com/user-attachments/assets/c511b02f-8a39-4552-9e3c-e23a2da20518)








