import controller.ProductController;
import model.Product;
import view.MenuView;
import view.ProductView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static ProductView view = new ProductView();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        // Pass the list of products to the ProductController constructor
        ProductController controller = new ProductController(products);
        controller.start();
        // Display menu and handle user input
        aa:
        while (true) {
            MenuView.displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice.toLowerCase()) {
                case "l" -> {
                    // Read data from transaction source file
                    //controller.readDataFromFile("data/transaction.dat");
                    view.displayProducts(products);
                }
                case "m" -> {
                    while (true){
                        System.out.println("1. Write");
                        System.out.println("2. Read");
                        System.out.println("3. Back");
                        System.out.print("Choose : ");
                        int op = scanner.nextInt();
                        switch (op){
                            case 1 -> controller.randomWrite();
                            case 2 -> {
                                controller.randomRead("data/transaction.dat");
                                view.randomDisplay(products);
                            }
                            case 3 -> {
                                System.out.println("Back to menu");
                                continue aa;
                            }
                        }
                    }
                }
                case "cl" -> ProductController.clearFile();
                case "w" -> {
                    // Create new product
                    controller.createProduct();
                }
                case "r", "R" -> {
                    System.out.print("Enter product code: ");
                    String productCode = scanner.nextLine().trim();
                    Product product = controller.getProductDetailByCode(productCode);
                    ProductView view = new ProductView();
                    //view.displayProductDetails(product);
                    break;
                }
                case "e", "E" -> System.out.println("Edit option chosen");
                case "d", "D" -> System.out.println("Delete option chosen");
                case "s", "S" -> System.out.println("Search option chosen");
                case "o" -> {
                    ProductView view = new ProductView();
                    view.setRow();
                }
                case "c", "C" -> System.out.println("Commit option chosen");
                case "b", "B" -> System.out.println("Backup option chosen");
                case "k", "K" -> System.out.println("Back up option chosen");
                case "t", "T" -> System.out.println("Restore option chosen");
                case "h", "H" -> System.out.println("Help option chosen");
                case "x", "X" -> {
                    System.out.println("Exit option chosen");
                    return; // Exit the program
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
