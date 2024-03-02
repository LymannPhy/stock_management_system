package controller;

import model.Product;
import view.Color;
import view.ProductView;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


import static model.Product.parseProductLine;

public class ProductController implements Color {
    private static List<Product> products;
    private HashSet<String> usedProductCodes;
    static Scanner scanner = new Scanner(System.in);
    private ProductView view = new ProductView();
    private int nextProductNumber;
    private static volatile boolean isLoading = true;
    static int amountProduct;
    Scanner input = new Scanner(System.in);
    private boolean changesCommitted;

    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    public ProductController(List<Product> products) {
        this.products = products;
        this.usedProductCodes = new HashSet<>();
        this.nextProductNumber = loadNextProductNumber();
        this.changesCommitted = false;

    }

    public void logo(){
        String logo = """
                \t\t\t\t\t ╔════════════════════════════════════════════════════════╗
                \t\t\t\t\t ║ \t\t ██████╗███████╗████████╗ █████╗ ██████╗          ║
                \t\t\t\t\t ║ \t\t██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗         ║
                \t\t\t\t\t ║ \t\t██║     ███████╗   ██║   ███████║██║  ██║         ║
                \t\t\t\t\t ║ \t\t██║     ╚════██║   ██║   ██╔══██║██║  ██║         ║
                \t\t\t\t\t ║ \t\t╚██████╗███████║   ██║   ██║  ██║██████╔╝         ║
                \t\t\t\t\t ║ \t\t ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝          ║
                \t\t\t\t\t ║ \t\t\t Center of Science Technology and             ║
                \t\t\t\t\t ║ \t\t\t\t Advanced Development                     ║
                \t\t\t\t\t ║ \t\t☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆        ║
                \t\t\t\t\t ║ \t\t\t\t\t Stock Manager                        ║
                \t\t\t\t\t ║ \t\t☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆        ║
                \t\t\t\t\t ╚════════════════════════════════════════════════════════╝
                """;
        System.out.println("\n"+ yellow +logo + reset);
    }

    public void start(){
        //loading();
        logo();
        index();
        //readToList("data/product.dat");
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

    public void index(){
        products.clear();
        usedProductCodes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/product.dat"))) {
            String line;
            int i=0;
            int count = 1;
            System.out.print("Loading : "+green);
            long startTime = System.nanoTime();
            while ((line = reader.readLine()) != null) {
                Product product = parseProductLine(line);
                if (product != null) {
                    products.add(product);
                    usedProductCodes.add(product.getCode());
                    if (count == 100) System.out.print("\r♨️ Data is Loading ...../|");
                    if (count == 400) {
                        System.out.print("\rData is Loading .....|\\\"");
                        count=1;
                    }
                    count++;
                    i++;
                }
            }
            long endTime = System.nanoTime(); // End time
            long resultTime = endTime - startTime;
            System.out.println(reset+"\r♨️ Loading spend: " + (resultTime /  1_000_000_000.0) + " seconds.");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
        System.out.println();
    }

    public void loading(){
        for (int i = 0; i <= 100; i+=2) {
            int totalBlocks = 30;
            int blocksToShow = (i * totalBlocks) / 100;
            System.out.print(" ".repeat(10) + " Loading [ " + i + "% ]");
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
        amountProduct = amount;
        System.out.print("> Are you sure to random " + amount + " products? [y/n]: ");
        scanner.nextLine();
        String save = scanner.nextLine();
        if (Objects.equals(save, "y")) {
            long startTime = System.nanoTime(); // Start time
            AtomicInteger generatedCount = new AtomicInteger(0);
            int lastProductNumber = getLastProductNumber(); // Get the last product number from the transaction file

            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < THREAD_POOL_SIZE; i++) {
                futures.add(executorService.submit(() -> {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat", true))) {
                        while (true) {
                            int currentCount = generatedCount.getAndIncrement();
                            if (currentCount >= amount) {
                                break;
                            }

                            String productCode = "CSTAD-" + (lastProductNumber + currentCount + 1); // Generate product code
                            // Create new product only if the code is not already used
                            if (!usedProductCodes.contains(productCode)) {
                                Product newProduct = new Product(productCode, "P" + (lastProductNumber + currentCount + 1), 10.00d, 10, LocalDate.now().toString());
                                String serializedProduct = serializeProduct(newProduct);
                                synchronized (writer) {
                                    writer.write(serializedProduct);
                                    writer.newLine();
                                }
                                usedProductCodes.add(productCode); // Add the code to the used codes set
                                if ((amount > 10000) && (currentCount + 1) % (amount / 10) == 0 || currentCount == amount - 1) {
                                    int progress = ((currentCount + 1) * 100) / amount;
                                    System.out.print("\rLoading[" + progress + "%]");
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Error writing to transaction file: " + e.getMessage());
                    }
                }));
            }

            // Wait for all threads to complete
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("\r" + amount + " Products created successfully.");
            long endTime = System.nanoTime(); // End time
            long resultTime = endTime - startTime;
            System.out.println("Writing " + amount + " products spent: " + (resultTime / 1_000_000_000.0) + " seconds.");
        }
    }


    // Helper method to get the last product number from the transaction file
    private int getLastProductNumber() {
        int lastProductNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("data/transaction.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String code = parts[0];
                    if (!code.isEmpty()) { // Check if the code is not empty
                        int number = Integer.parseInt(code.substring(code.lastIndexOf("-") + 1));
                        if (number > lastProductNumber) {
                            lastProductNumber = number;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
        return lastProductNumber;
    }

    public void randomRead(String filename) {
        // Reset the file pointer by closing and reopening the file in read mode
        products.clear();
        usedProductCodes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            long startTime = System.nanoTime();
            int i=0;
            while ((line = reader.readLine()) != null) {
                Product product = parseProductLine(line);
                if (product != null) {
                    products.add(product);
                    usedProductCodes.add(product.getCode());
                }
                i+=1;
                if ((amountProduct > 10000) && (i + 1) % (amountProduct / 10) == 0 || i == amountProduct - 1) {
                    int progress = ((i + 1) * 100) / amountProduct;
                    System.out.print("\rLoading[" + progress + "%]");
                }
            }
            long endTime = System.nanoTime();
            long resultTime = endTime - startTime;
            System.out.println("\rRead products spent: " + (resultTime /  1_000_000_000.0) + " seconds.");
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
        int lastProductNumberFromTransactionFile = getLastProductNumber();
        int lastProductNumberFromRandomWrite = getLastProductNumberFromRandomWrite();
        int productNumber = Math.max(lastProductNumberFromTransactionFile, lastProductNumberFromRandomWrite) + 1;
        return codePrefix + productNumber;
    }

    // Helper method to get the last product number generated by randomWrite method
    private int getLastProductNumberFromRandomWrite() {
        int lastProductNumberFromRandomWrite = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("data/transaction.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String code = parts[0];
                    if (code.startsWith("CSTAD-")) {
                        int number = Integer.parseInt(code.substring(code.lastIndexOf("-") + 1));
                        if (number > lastProductNumberFromRandomWrite) {
                            lastProductNumberFromRandomWrite = number;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
        return lastProductNumberFromRandomWrite;
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

    public Product getProductDetailByCode(String code) {
        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                return product;
            }
        }
        return null;
    }

    public boolean hasUncommittedTransactions() {
        File file = new File("data/transaction.dat");
        return file.exists() && file.length() > 0;
    }

    public void commitChanges() {
        if (!hasUncommittedTransactions()) {
            System.out.println("No uncommitted transactions to commit.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("data/transaction.dat"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("data/product.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Changes committed successfully!");
            this.changesCommitted = true; // Set the flag indicating changes have been committed
        } catch (IOException e) {
            System.err.println("Error committing changes: " + e.getMessage());
        }
    }
    public boolean areChangesCommitted() {
        return this.changesCommitted;
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

    // Method to restore product data from a backup file
    public void restoreData() {
        File backupFolder = new File("backup");
        File[] backupFiles = backupFolder.listFiles((dir, name) -> name.endsWith(".bak"));
        if (backupFiles == null || backupFiles.length == 0) {
            System.out.println("No backup files found in the backup_product folder.");
            return;
        }

        System.out.println("Available backup files:");
        for (int i = 0; i < backupFiles.length; i++) {
            System.out.println((i + 1) + ". " + backupFiles[i].getName());
        }

        System.out.print("Enter the number of the backup file to restore: ");
        Scanner scanner = new Scanner(System.in);
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Consume invalid input
            return;
        }
        if (choice < 1 || choice > backupFiles.length) {
            System.out.println("Invalid choice.");
            return;
        }

        String filePath = backupFiles[choice - 1].getPath();
        List<Product> restoredProducts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) { // Assuming the format is consistent
                    Product product = new Product();
                    product.setCode(parts[0]);
                    product.setName(parts[1]);
                    product.setPrice(Double.parseDouble(parts[2]));
                    product.setQty(Integer.parseInt(parts[3]));
                    product.setImported_at(parts[4]);
                    restoredProducts.add(product);
                } else {
                    System.out.println("Invalid data format in the file.");
                }
            }
            // Replace the existing product data with the restored data
            products.clear();
            products.addAll(restoredProducts);
            System.out.println("Product data restored successfully from " + filePath);

            // Write the restored data to "product.dat" file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/product.dat"))) {
                for (Product product : restoredProducts) {
                    writer.write(product.getCode() + "," + product.getName() + "," + product.getPrice() + ","
                            + product.getQty() + "," + product.getImported_at() + "\n");
                }
                System.out.println("Restored data written to product.dat");
            } catch (IOException e) {
                System.out.println("Error writing restored data to product.dat: " + e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found at path " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading product data: " + e.getMessage());
        }
    }

    // Method to backup product data to a file
    public void backupData() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("dd-MMM-yy (HH_mm_ss)"));
        String fileName = "backup/backup-" + formattedDateTime + ".bak";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Product product : products) {
                String line = String.format("%s,%s,%.2f,%d,%s%n",
                        product.getCode(), product.getName(), product.getPrice(), product.getQty(), product.getImported_at());
                writer.write(line);
            }
            System.out.println("Product data backed up successfully.");
        } catch (IOException e) {
            System.out.println("Error backing up product data: " + e.getMessage());
        }
    }

    public void handleBackupDecision() {
        //System.out.println("You have uncommitted transactions.");
        System.out.print("Do you want back up data?[Y/n]: ");
        String decision = input.nextLine().trim().toLowerCase();
        if (decision.equals("y")) {
            backupData();
        } else {
            System.out.println("You didn't backup data....!");
        }
    }

}
