package controller;

import model.Product;

import java.io.*;
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

    public void random(){
        while (true){
            System.out.println("1. Write");
            System.out.println("2. Read");
            System.out.println("3. Back");
            System.out.print("Choose : ");
            int op = scanner.nextInt();
            switch (op){
                case 1 -> randomWrite();
                case 2 -> System.out.println("Coming soon...!");
                case 3 -> {
                    return;
                }
            }
        }
    }

    // choice 1
    static AtomicInteger j = new AtomicInteger(0);
    static AtomicInteger amount = new AtomicInteger(0);

    // test 1M record = 1.39s
    public static void randomWrite() {
        System.out.print("> Enter random amount = ");
        int amount1 = scanner.nextInt();
        String sDigit = String.valueOf(amount1);
        int digit = sDigit.length();
        amount.set(amount1);
        System.out.print("> Are you sure to random " + amount + " products? [y/n]: ");
        scanner.nextLine();
        String save = scanner.nextLine();
        if (Objects.equals(save, "y")) {

            long startTime = System.nanoTime(); // Start time
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat"))) {
                for (int i =  0; i < amount1; i++) {
                    Product newProduct = new Product("CSTAD-" + (i +   1), "P" + (i +   1),   10.00d,   10, LocalDate.now().toString());
                    String serializedProduct = serializeProduct(newProduct);
                    writer.write(serializedProduct);
                    writer.newLine();
                    if ((i + 1) % (amount1 / 10) == 0 || i == amount1 - 1) {
                        int progress = ((i + 1) * 100) / amount1;
                        System.out.print("\rLoading [" + progress + "%] ");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error writing to transaction file: " + e.getMessage());
            }

            System.out.println("\r"+amount + " Product(s) created successfully.");
            long endTime = System.nanoTime(); // End time
            long resultTime = endTime - startTime;
            System.out.println("Writing " + amount + " products spent: " + (resultTime /  1_000_000_000.0) + " seconds.");
        }
    }


    // Load next product number from file or database
    private int loadNextProductNumber() {
        // Implement loading logic from file or database
        return 1; // Default to 1 if not found
    }

    public void createProduct() {
        Scanner scanner = new Scanner(System.in);

        // Generate product code automatically
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

        // Add the new product to the list of products
        products.add(newProduct);
        usedProductCodes.add(code); // Add the code to the set of used codes

        String serializedProduct = serializeProduct(newProduct);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat", true))) {
            writer.write(serializedProduct + "\n");
            System.out.println("Product created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to transaction file: " + e.getMessage());
        }
    }

    public static void clearFile(){
        try (FileWriter writer = new FileWriter("data/transaction.dat", false)) {
            // Writing an empty string to overwrite and clear the file's content
            writer.write("");
            System.out.println("Clear data success...!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Generate product code
    private String generateProductCode() {
        String codePrefix = "CSTAD-";
        String code = codePrefix + nextProductNumber;
        nextProductNumber++; // Increment the product number for the next product
        return code;
    }

    // Method to read data from the file and parse it into Product objects
    public void readDataFromFile(String filename) {
        products.clear();
        usedProductCodes.clear();
        long startTime = System.nanoTime();

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
        long endTime = System.nanoTime();
        long resultTime = endTime - startTime;
        System.out.println("Read " + " products spend : " + ( resultTime / 1_000_000_000.0) + " seconds.");
    }

    public List<Product> getProducts() {
        return products;
    }

    // Method to get product detail by code
    public Product getProductDetailByCode(String code) {
        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                return product;
            }
        }
        return null; // Product not found
    }

    private static String serializeProduct(Product product) {
        return String.format("%s,%s,%.2f,%d,%s", product.getCode(), product.getName(), product.getPrice(), product.getQty(), product.getImported_at());
    }
}
