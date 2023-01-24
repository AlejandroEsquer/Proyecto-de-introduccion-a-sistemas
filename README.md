# Proyecto-de-introduccion-a-sistemas
Este proyecto es una aplicación de chat entre dos usuarios uno denominado principal y un secundario, desarrollado en Java,en la cual se utilizan sockets
para el intercambio de mensajes y archivos, también pueden mandarse fotos y videos.
Además cuenta con una funcionalidad en la que el usuario principal puede elegir robarle un archivo al usuario secundario sin que este se de cuenta.
Para esto, primero obtenemos el nombre de la carpeta en la que se encuentran todos sus archivos y posteriormente vamos listando todos los archivos
que se encuentren en la carpeta que el usuario principal vaya escogiendo, hasta llegar al archivo que se desea que sea transferido.
Luego, se checa si existe un archivo con el mismo nombre en la carpeta del usuario princiapl, de ser así, se pide que cambié el nombre del nuevo archivo.
También le permite al usuario principal cancelar la bsqueda del archivo cuando este lo desee.
