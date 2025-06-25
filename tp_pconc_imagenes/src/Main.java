public class Main {
    public static void main(String[] args) {
        verifyArgs(args);
        int bufferLimit= Integer.parseInt(args[2]);
        WorkerCounter wc= new WorkerCounter();
        Buffer bf= new Buffer(bufferLimit);
        
        System.err.println("temp");
    }

    private static void verifyArgs(String[] args) {
        if(args[0].isEmpty() || !args[0].endsWith(".jpg")){
            System.out.println("El primer argumento debe ser un la direccion de una imagen en formato jpg");
            System.exit(1);
        }

        if(args[1].isEmpty() || !args[1].endsWith(".jpg")){
            System.out.println("El segundo argumento debe ser un la direccion de destino de la imagen generada en formato jpg. Ej: './test.jpg' ");
            System.exit(1);
        }

        try {
            Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("El tercer argumento debe ser un numero para el tamaño del buffer");
            System.exit(1);
        }

        try {
            Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("El cuarto argumento debe ser un numero para la cantidad de Workers");
            System.exit(1);
        }

        //falta verificar el ultimo argumento, hay que chequear como se quiere utilizar el vector
    }
}
