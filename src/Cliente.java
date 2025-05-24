import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nOpciones:");
            System.out.println("1. Buscar archivo txt");
            System.out.println("2. Recuperar archivo por ID");
            System.out.println("3. Salir");
            System.out.print("Seleccione: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarArchivo(scanner);
                    break;
                case 2:
                    recuperarArchivo(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private static void buscarArchivo(Scanner scanner) {
        System.out.print("Escriba el nombre del archivo a buscar: ");
        String titulo = scanner.nextLine();

        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            salida.println("buscar:" + titulo);
            String respuesta = entrada.readLine();

            if (respuesta == null) {
                System.out.println("El servidor no respondió");
                return;
            }

            if (respuesta.equals("no_encontrado")) {
                System.out.println("No se encontraron archivos con ese título");
            } else {
                System.out.println("Archivos encontrados:");
                do {
                    System.out.println("- " + respuesta.substring(3));
                    respuesta = entrada.readLine();
                } while (respuesta != null && respuesta.startsWith("id:"));
            }
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    private static void recuperarArchivo(Scanner scanner) {
        System.out.print("Ingrese ID del archivo a recuperar: ");
        String id = scanner.nextLine();

        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            salida.println("recuperar:" + id);
            String respuesta = entrada.readLine();

            if (respuesta == null) {
                System.out.println("El servidor no respondió");
                return;
            }

            if (respuesta.equals("no_encontrado")) {
                System.out.println("No se encontró el archivo con ese ID");
            } else if (respuesta.startsWith("contenido:")) {
                System.out.println("\nContenido del archivo:");
                System.out.println(respuesta.substring(10));
            } else if (respuesta.startsWith("error:")) {
                System.out.println("Error: " + respuesta.substring(6));
            }
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}