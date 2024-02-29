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
                //case "k" -> controller.backUp();
                /*case "t" -> {
                    controller.restore();
                    controller.start();
                }*/
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
                                view.displayProducts(products);
                            }
                            case 3 -> {
                                System.out.println("Back to menu");
                                continue aa;
                            }
                        }
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
                case "e", "E" -> {
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
                case "o", "O" -> view.setRow();
                case "c", "C" -> {
                    // Commit changes
                    controller.commitChanges();
                }
                case "b", "B" -> System.out.println("Backup option chosen");
                case "k", "K" -> {
                    System.out.println("Back up option chosen");
                    controller.handleBackupDecision();
                }
                case "t", "T" -> {
                    System.out.println("Restore option chosen");
                    controller.restoreData();
                    dataRestored = true;
                }
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
