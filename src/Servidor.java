import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Servidor {
    private static final int PUERTO = 12345;
    private static final String DIR_ARCHIVOS = "C:/Users/edu_c/OneDrive/Escritorio/archivos/";

    public static void main(String[] args) {
        try {

            Files.createDirectories(Paths.get(DIR_ARCHIVOS));

            ServerSocket serverSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en el puerto " + PUERTO);

            while (true) {
                Socket socket = serverSocket.accept();
                new ManejadorCliente(socket).start();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    private static class ManejadorCliente extends Thread {
        private Socket socket;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

                String comando = entrada.readLine();
                if (comando != null) {
                    if (comando.startsWith("buscar:")) {
                        String titulo = comando.substring(7);
                        buscarArchivo(salida, titulo);
                    } else if (comando.startsWith("recuperar:")) {
                        String id = comando.substring(10);
                        recuperarArchivo(salida, id);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en manejador cliente: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar socket: " + e.getMessage());
                }
            }
        }

        private void buscarArchivo(PrintWriter salida, String titulo) {
            try {
                File directorio = new File(DIR_ARCHIVOS);
                System.out.println("Buscando en: " + directorio.getAbsolutePath());

                File[] archivos = directorio.listFiles((dir, name) ->
                        name.toLowerCase().contains(titulo.toLowerCase()));

                if (archivos != null && archivos.length > 0) {
                    for (File archivo : archivos) {
                        salida.println("id:" + archivo.getName());
                    }
                } else {
                    salida.println("no_encontrado");
                }
            } catch (Exception e) {
                salida.println("error:" + e.getMessage());
            }
        }

        private void recuperarArchivo(PrintWriter salida, String id) {
            try {
                File archivo = new File(DIR_ARCHIVOS + id);
                if (archivo.exists() && archivo.isFile()) {
                    String contenido = new String(Files.readAllBytes(archivo.toPath()));
                    salida.println("contenido:" + contenido);
                } else {
                    salida.println("no_encontrado");
                }
            } catch (Exception e) {
                salida.println("error:" + e.getMessage());
            }
        }
    }
}