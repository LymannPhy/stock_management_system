import controller.ProductController;
import model.Product;
import view.MenuView;
import view.ProductView;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static view.Color.reset;
import static view.Color.yellow;

public class Main {
    static ProductView view = new ProductView();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        // Pass the list of products to the ProductController constructor
        ProductController controller = new ProductController(products);
        controller.start();
        boolean dataRestored = false;
        // Display menu and handle user input
        aa:
        while (true) {
            MenuView.displayMenu();
            System.out.print("➡\uFE0F Enter your choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice.toLowerCase()) {
                case "l" -> {
                    // Read data from transaction source file
                    //controller.readDataFromFile("data/transaction.dat");
                    view.displayProducts(products);
                }
                case "m" -> {
                    try {
                        while (true) {
                            System.out.println("1. Write");
                            System.out.println("2. Read");
                            System.out.println("3. Back");
                            System.out.print("Choose : ");
                            try {
                                int op = scanner.nextInt();
                                switch (op) {
                                    case 1 -> controller.randomWrite();
                                    case 2 -> {
                                        controller.randomRead("data/product.dat");
                                        view.displayProducts(products);
                                    }
                                    case 3 -> {
                                        System.out.println("Back to menu");
                                        continue aa;
                                    }
                                    default ->
                                            System.out.println("Invalid choice, please enter a number between 1 and 3");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number.");
                                scanner.next(); // Clear the invalid input from the scanner
                            }
                        }
                    }
                    catch(Error error) {
                        System.out.println(error.getMessage());
                    }
                }
                //case "cl" -> ProductController.clearFile();
                case "w" -> {
                    // Create new product
                    controller.createProduct();
                }
                case "r" -> {
                    System.out.print("Enter product code: ");
                    String productCode = scanner.nextLine().trim();
                    Product product = controller.getProductDetailByCode(productCode);
                    ProductView view = new ProductView();
                    view.displayProductDetails(product);
                }
                case "e" -> {
                    System.out.print("Enter Product Code: ");
                    String productCode = scanner.nextLine().trim();
                    Product product = controller.getProductDetailByCode(productCode);
                    ProductView view = new ProductView();
                    view.displayProductDetails(product);
                    if (product == null) {
                        break;
                    } else {
                        controller.editProduct(productCode);
                    }

                }
                case "d"-> {
                    System.out.print("Enter product code to delete: ");
                    String productCode = scanner.nextLine().trim();
                    Product product = controller.getProductDetailByCode(productCode);
                    ProductView view = new ProductView();
                    view.displayProductDetails(product);
                    if (product == null) {
                        break;
                    } else {
                        controller.deleteProductByCode(productCode);
                    }

                }
                case "s"-> {
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
                case "o" -> view.setRow();
                case "c"-> {
                    // Commit changes
                    controller.commitChanges();
                }
                case "b"-> System.out.println("Backup option chosen");
                case "k" -> {
                    controller.handleBackupDecision();
                }
                case "t" -> {
                    System.out.println("Restore option chosen");
                    controller.restoreData();
                    dataRestored = true;
                }
                case "h" -> {
                    MenuView.displayHelp();
                }
                case "x"-> {
                    if (!controller.areChangesCommitted()) {
                        System.out.println("Changes have not been committed yet.");
                        System.out.print("Do you want to commit changes before exiting?[Y/n]: ");
                        String commitDecision = scanner.nextLine().trim().toLowerCase();
                        if (commitDecision.equals("y")) {
                            controller.commitChanges();
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
