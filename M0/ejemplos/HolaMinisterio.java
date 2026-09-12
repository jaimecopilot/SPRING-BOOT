public class HolaMinisterio {
    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println("Hola, " + args[0] + ", bienvenido al Ministerio de Educación");
        } else {
            System.out.println("Hola, Ministerio de Educación");
        }
    }
}
