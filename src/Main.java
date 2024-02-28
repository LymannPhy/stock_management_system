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
                case "l", "L" -> {
                    // Read data from transaction source file
                    controller.readTransactionDataFromFile();
                    // Get the products from the controller
                    products = controller.getProducts();
                    // Instantiate the view and display the products
                    ProductView view = new ProductView();
                    view.displayProducts(products);
                }
                case "m", "M" -> System.out.println("Random option chosen");
                case "w", "W" -> {
                    // Create new product
                    controller.createProduct();
                }
                case "r", "R" -> {
                    System.out.print("Enter product code: ");
                    String productCode = scanner.nextLine().trim();
                    Product product = controller.getProductDetailByCode(productCode);
                    ProductView view = new ProductView();
                    view.displayProductDetails(product);
                }
                case "e", "E" -> System.out.println("Edit option chosen");
                case "d", "D" -> {
                    System.out.print("Enter product code to delete: ");
                    String productCode = scanner.nextLine().trim();
                    controller.deleteProductByCode(productCode);
                }
                case "s", "S" -> {
                    System.out.print("Enter product name to search: ");
                    String searchTerm = scanner.nextLine().trim();
                    List<Product> searchResults = controller.searchProductByName(searchTerm);
                    if (searchResults.isEmpty()) {
                        System.out.println("No matching products found.");
                    } else {
                        ProductView view = new ProductView();
                        view.displayProducts(searchResults);
                    }
                    break;
                }
                case "o", "O" -> System.out.println("Set Row option chosen");
                case "c", "C" -> {
                    // Commit changes
                    controller.commitChanges();
                }
                case "b", "B" -> System.out.println("Backup option chosen");
                case "k", "K" -> System.out.println("Back up option chosen");
                case "t", "T" -> System.out.println("Restore option chosen");
                case "h", "H" -> {
                    MenuView help = new MenuView();
                    help.displayHelp();
                }
                case "x", "X" -> {
                    if (controller.hasUncommittedTransactions()) {
                        System.out.println("You have uncommitted transactions.");
                        System.out.print("Do you want to save or lose data?[Y/n]: ");
                        String decision = scanner.nextLine().trim().toLowerCase();
                        if (decision.equals("y")) {
                            controller.commitChanges();
                        } else {
                            System.out.println("Exiting the program without saving data.");
                        }
                    }
                    System.out.println("Exiting the program.");
                    return;
                }

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
