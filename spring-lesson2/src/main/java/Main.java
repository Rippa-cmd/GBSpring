import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import persist.Product;
import persist.ProductCart;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        scanner = new Scanner(System.in);
        Map<String, ProductCart> carts = new HashMap<>();

        while (true) {
            String cmd = scanner.nextLine();
            if (cmd.isEmpty())
                continue;
            if ("exit".equals(cmd)) {
                break;
            }
            String[] cmds = cmd.split(" ");
            if (cmd.startsWith("new cart")) {
                carts.put(cmds[2], context.getBean(ProductCart.class));
            } else if ("add".equals(cmds[1])) {
                if (carts.containsKey(cmds[0]))
                    carts.get(cmds[0]).addProduct(Long.parseLong(cmds[2]));
            } else if ("delete".equals(cmds[1])) {
                if (carts.containsKey(cmds[0]))
                    carts.get(cmds[0]).deleteProduct(Long.parseLong(cmds[2]));
            }
        }
        scanner.close();
    }
}
