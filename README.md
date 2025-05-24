# Configuración Inicial
## Importante: Configurar Ruta de Archivos
Antes de ejecutar el proyecto, **debes cambiar la ruta de archivos** en el código para que coincida con tu sistema.
### Paso 1: Localizar la constante
Busca esta línea en el código fuente:
```java
private static final String DIR_ARCHIVOS = "C:/Users/edu_c/OneDrive/Escritorio/archivos/";
```
### Paso 2: Cambiar la ruta
Reemplaza la ruta por la ubicación donde quieres almacenar los archivos en tu máquina:
```java
private static final String DIR_ARCHIVOS = "C:/Users/TU_USUARIO/ruta/a/tus/archivos/";
```
> **Nota:** Recuerda usar barras inclinadas `/` en lugar de barras invertidas `\` para mejor compatibilidad multiplataforma.
