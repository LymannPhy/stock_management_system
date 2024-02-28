package controller;

import model.Product;
import view.ProductView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static model.Product.parseProductLine;

public class ProductController {
    private static List<Product> products;
    private HashSet<String> usedProductCodes;
    static Scanner scanner = new Scanner(System.in);
    private int nextProductNumber;
    private static volatile boolean isLoading = true;
    static String green = "\u001B[32m";
    String reset = "\u001B[0m";

    public ProductController(List<Product> products) {
        this.products = products;
        this.usedProductCodes = new HashSet<>();
        this.nextProductNumber = loadNextProductNumber(); // Load from file or database
    }

    public void backUp(){
        String orgPath  = "data/product.dat";
        String copyPath = "data/backup.dat";
        try (FileInputStream in = new FileInputStream(orgPath);
             FileOutputStream out = new FileOutputStream(copyPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("Backup successfully!");
        } catch (IOException e) {e.printStackTrace();}
    }

    public void restore(){
        deleteFile();
        String orgPath = "data/backup.dat";
        String copyPath  = "data/product.dat";
        try (FileInputStream in = new FileInputStream(orgPath);
             FileOutputStream out = new FileOutputStream(copyPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("Restore successfully!");
        } catch (IOException e) {e.printStackTrace();}
    }

    public static void deleteFile(){
        Path path = Paths.get("data/product.dat");
        try {
            Files.delete(path);
            //System.out.println("File deleted successfully.");
        } catch (IOException e) {
            System.out.println("Failed to delete the file.");
            e.printStackTrace();
        }
    }

    public void start(){
        //loading();
        readToList("data/product.dat");
    }

    public void readToList(String filename) {
        products.clear();
        usedProductCodes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = parseProductLine(line);
                if (product != null) {
                    products.add(product);
                    usedProductCodes.add(product.getCode());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }

    public void loading(){
        for (int i = 0; i <= 100; i+=2) {
            int totalBlocks = 50;
            int blocksToShow = (i * totalBlocks) / 100;
            System.out.print(" ".repeat(20) + " Loading [ " + i + "% ]");
            System.out.print(" ".repeat(10) + getProgressBar(blocksToShow, totalBlocks) + "\r");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(" "+reset);
    }

    private static String getProgressBar(int blocksToShow, int totalBlocks) {
        String progressBar = "█".repeat(Math.max(0, blocksToShow)) +
                " ".repeat(Math.max(0, totalBlocks - blocksToShow));
        return green + progressBar;
    }

    // choice 1
    public void randomWrite() {
        System.out.print("> Enter random amount = ");
        int amount = scanner.nextInt();
        String sDigit = String.valueOf(amount);
        System.out.print("> Are you sure to random " + amount + " products? [y/n]: ");
        scanner.nextLine();
        String save = scanner.nextLine();
        if (Objects.equals(save, "y")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat"))) {
                long startTime = System.nanoTime(); // Start time
                for (int i =  0; i < amount; i++) {
                    Product newProduct = new Product("CSTAD-" + (i +   1), "P" + (i +   1),   10.00d,   10, LocalDate.now().toString());
                    String serializedProduct = serializeProduct(newProduct);
                    writer.write(serializedProduct);
                    writer.newLine();
                    if ( (amount > 10000) && (i + 1) % (amount / 10) == 0 || i == amount - 1) {
                        int progress = ((i + 1) * 100) / amount;
                        System.out.print("\rLoading[" + progress + "%]");
                    }

                }
                System.out.println("\r"+amount + " Product(s) created successfully.");
                long endTime = System.nanoTime(); // End time
                long resultTime = endTime - startTime;
                System.out.println("Writing " + amount + " products spent: " + (resultTime /  1_000_000_000.0) + " seconds.");
            } catch (IOException e) {
                System.err.println("Error writing to transaction file: " + e.getMessage());
            }
        }
    }

    public void randomRead(String filename){
        products.clear();
        usedProductCodes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            long startTime = System.nanoTime();
            while ((line = reader.readLine()) != null) {
                Product product = parseProductLine(line);
                if (product != null) {
                    products.add(product);
                    usedProductCodes.add(product.getCode());
                }
            }
            long endTime = System.nanoTime();
            long resultTime = endTime - startTime;
            System.out.println("Read products spent: " + (resultTime /  1_000_000_000.0) + " seconds.");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }


    // Load next product number from file or database
    private int loadNextProductNumber() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/product_number.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading product number from file: " + e.getMessage());
        }
        return 1;
    }

    private String generateProductCode() {
        String codePrefix = "CSTAD-";
        int productNumber = nextProductNumber++;
        saveNextProductNumber();
        return codePrefix + productNumber;
    }

    private void saveNextProductNumber() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/product_number.txt"))) {
            writer.write(String.valueOf(nextProductNumber));
        } catch (IOException e) {
            System.err.println("Error writing product number to file: " + e.getMessage());
        }
    }

    public void createProduct() {
        Scanner scanner = new Scanner(System.in);

        String code = generateProductCode();

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter product quantity: ");
        int qty = scanner.nextInt();

        scanner.nextLine();

        LocalDate importedAt = LocalDate.now();
        String imported_at = importedAt.toString();

        Product newProduct = new Product();
        newProduct.setCode(code);
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setQty(qty);
        newProduct.setImported_at(imported_at);

        System.out.print("Are you sure to create a new product?[Y/n]: ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            products.add(newProduct);

            String serializedProduct = serializeProduct(newProduct);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat", true))) {
                writer.write(serializedProduct + "\n");
                System.out.println("Product has been created successfully!");
            } catch (IOException e) {
                System.err.println("Error writing to transaction file: " + e.getMessage());
            }
        } else {
            System.out.println("Product creation cancelled.");
        }
    }

    public void readTransactionDataFromFile() {
        products.clear();
        usedProductCodes.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/transaction.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = parseProductLine(line);
                if (product != null) {
                    products.add(product);
                    usedProductCodes.add(product.getCode());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transaction data from file: " + e.getMessage());
        }
    }

    public Product getProductDetailByCode(String code) {
        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean hasUncommittedTransactions() {
        File file = new File("data/transaction.dat");
        return file.exists() && file.length() > 0;
    }

    public void commitChanges() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/transaction.dat"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("data/product.dat", false))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Changes committed successfully!");
        } catch (IOException e) {
            System.err.println("Error committing changes: " + e.getMessage());
        }
    }

    public boolean isTransactionEmpty() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/transaction.dat"))) {
            return reader.readLine() == null;
        } catch (IOException e) {
            System.err.println("Error checking transaction file: " + e.getMessage());
            return false;
        }
    }

    public List<Product> searchProductByName(String name) {
        List<Product> searchResults = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                searchResults.add(product);
            }
        }
        return searchResults;
    }


    // Method to delete product by code from transaction file
    public void deleteProductByCode(String code) {
        File transactionFile = new File("data/transaction.dat");
        File tempFile = new File("data/temp.dat");

        try (BufferedReader reader = new BufferedReader(new FileReader(transactionFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line starts with the product code
                if (!line.startsWith(code)) {
                    // If not, write the line to the temporary file
                    writer.write(line + System.lineSeparator());
                    products.removeIf(p -> p.getCode().equalsIgnoreCase(code));
                }
            }
        } catch (IOException e) {
            System.err.println("Error deleting product from transaction file: " + e.getMessage());
            return;
        }

        // Replace the original transaction file with the temporary file
        try {
            // Delete the original transaction file
            if (!transactionFile.delete()) {
                throw new IOException("Failed to delete original transaction file.");
            }
            // Rename the temporary file to replace the original transaction file
            if (!tempFile.renameTo(transactionFile)) {
                throw new IOException("Failed to rename temporary file.");
            }
            System.out.println("Product deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error replacing transaction file: " + e.getMessage());
        }
    }
    public void editProduct(String code) {
        String pDate = "", pName = "";
        double pPrice = 0.0;
        int pQty = 0;
        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                pDate = product.getImported_at();
                pName = product.getName();
                pPrice = product.getPrice();
                pQty = product.getQty();
                break;
            }
        }
        Scanner sure = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        System.out.println("# What do you want to update?");
        System.out.println("1. All");
        System.out.println("2. NAME");
        System.out.println("3. UNIT PRICE");
        System.out.println("4. QTY");
        System.out.println("5. Back to Menu");
        System.out.print("> Choose Option [1-5] :");
        String choose = scanner.nextLine();
        switch (choose) {
            case "1" -> {
                try {
                    // Update all attribute
                    System.out.print("> Product's Name :");
                    String productName = scanner.nextLine();
                    System.out.print("> Product's Price:");
                    double productPrice = Double.parseDouble(scanner.nextLine());
                    System.out.print("> Product's QTY :");
                    int productQty = Integer.parseInt(scanner.nextLine());
                    ProductView view = new ProductView();
                    view.previewUpdate(code, productName, productPrice, productQty, pDate);
                    System.out.print("[!] Are you sure you want to Update?? [Y/N] :");
                    String want = sure.next();
                    if (want.equalsIgnoreCase("y")) {
                        for (Product product : products) {
                            if (product.getCode().equals(code)) {
                                product.setName(productName);
                                product.setPrice(productPrice);
                                product.setQty(productQty);
                                writeProductsToFile(); // Update all products to the file
                                System.out.println("Update Product Successfully!!");
                                return;
                            }
                        }
                    } else if (want.equalsIgnoreCase("n")) {
                        System.out.println("# You didn't Update Anything");
                    }
                } catch (Exception e) {
                    System.out.println("[!] Might be error, wrong input " + e.getMessage());
                }
            }
            case "2" -> {
                updateAttribute(code, scanner, "NAME", pName, pPrice, pQty, pDate);
            }
            case "3" -> {
                updateAttribute(code, scanner, "UNIT PRICE", pName, pPrice, pQty, pDate);
            }
            case "4" -> {
                updateAttribute(code, scanner, "QTY", pName, pPrice, pQty, pDate);
            }
            case "5" -> System.out.println("# Back to menu");
            default -> System.out.println("# Invalid Input Choose! Please try again....");
        }
    }

    private void updateAttribute(String code, Scanner scanner, String attributeName, String pName, double pPrice, int pQty, String pDate) {
        ProductView view = new ProductView();
        Scanner sure = new Scanner(System.in);
        try {
            System.out.print("> Product's " + attributeName + " :");
            String attributeValue = scanner.nextLine();
            switch (attributeName) {
                case "NAME" -> view.previewUpdate(code, attributeValue, pPrice, pQty, pDate);
                case "UNIT PRICE" -> view.previewUpdate(code, pName, Double.parseDouble((attributeValue)), pQty, pDate);
                case "QTY" -> view.previewUpdate(code, pName, pPrice, Integer.parseInt(attributeValue), pDate);
            }
            System.out.print("[!] Are you sure you want to Update?? [Y/N] :");
            String want = sure.next();
            if (want.equalsIgnoreCase("y")) {
                for (Product product : products) {
                    if (product.getCode().equals(code)) {
                        switch (attributeName) {
                            case "NAME" -> product.setName(attributeValue);
                            case "UNIT PRICE" -> product.setPrice(Double.parseDouble(attributeValue));
                            case "QTY" -> product.setQty(Integer.parseInt(attributeValue));
                        }
                        writeProductsToFile(); // Update all products to the file
                        System.out.println("Update Product's " + attributeName + " Successfully!!");
                        return;
                    }
                }
            } else if (want.equalsIgnoreCase("n")) {
                System.out.println("# You didn't Update anything");
            }
        } catch (Exception e) {
            System.out.println("[!] Might be error, wrong input " + e.getMessage());
        }
    }

    // Write all products to the file after update
    private void writeProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat"))) {
            for (Product product : products) {
                String serializedProduct = serializeProduct(product);
                writer.write(serializedProduct + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to transaction file: " + e.getMessage());
        }
    }


    private String serializeProduct(Product product) {
        return String.format("%s,%s,%.2f,%d,%s", product.getCode(), product.getName(), product.getPrice(), product.getQty(), product.getImported_at());
    }
}
