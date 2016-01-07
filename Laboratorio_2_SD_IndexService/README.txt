Instrucciones de ejecuci�n:

Se debe abrir el proyecto de NetBeans y correr primero el archivo "IndexService.java", luego "FrontService.java" y finalmente "Cliente.java". Las consultas se hacen desde la consola de "Cliente.java" y en ese mismo sitio se mostrar� la respuesta (top 10) de los documentos que cumplen de mejor manera la consulta. Tambi�n se mostrar�n mensajes en "IndexService.java" y "FrontService.java", que reflejen la informaci�n que reciben, manipulan y env�an.

Instrucciones de configuraci�n:

El archivo config.ini posee un par�metro de entrada que corresponde a la cantidad de hebras que va a manejar el frontService para responder las consultas de manera concurrente. El valor debe ser entero mayor a 0 y debe haber un salto de linea posterior al n�mero.