import controller.ProductController;
import model.Product;
import view.MenuView;
import view.ProductView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Instantiate Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Create an empty list to hold products
        List<Product> products = new ArrayList<>();

        // Pass the list of products to the ProductController constructor
        ProductController controller = new ProductController(products);

        // Display menu and handle user input
        while (true) {
            MenuView.displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice.toLowerCase()) {
                case "l" -> {
                    // Read data from transaction source file
                    controller.readDataFromFile("data/transaction.dat");
                    // Get the products from the controller
                    //products = controller.getProducts();
                    // Instantiate the view and display the products
                    ProductView view = new ProductView();
                    view.displayProducts(products);
                }
                case "m", "M" -> controller.random();
                case "w", "W" -> {
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
