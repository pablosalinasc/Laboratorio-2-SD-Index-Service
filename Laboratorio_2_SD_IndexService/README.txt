Instrucciones de ejecución:

Se debe abrir el proyecto de NetBeans y correr primero el archivo "IndexService.java", luego "FrontService.java" y finalmente "Cliente.java". Las consultas se hacen desde la consola de "Cliente.java" y en ese mismo sitio se mostrará la respuesta (top 10) de los documentos que cumplen de mejor manera la consulta. También se mostrarán mensajes en "IndexService.java" y "FrontService.java", que reflejen la información que reciben, manipulan y envían.

Instrucciones de configuración:

El archivo config.ini posee un parámetro de entrada que corresponde a la cantidad de hebras que va a manejar el frontService para responder las consultas de manera concurrente. El valor debe ser entero mayor a 0 y debe haber un salto de linea posterior al número.