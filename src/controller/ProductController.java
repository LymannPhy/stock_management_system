package controller;

import model.Product;
import view.Color;
import view.ProductView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static model.Product.parseProductLine;

public class ProductController implements Color {
    private static List<Product> products;
    private static HashSet<String> usedProductCodes;
    static Scanner scanner = new Scanner(System.in);
    private ProductView view = new ProductView();
    private int nextProductNumber;
    private static volatile boolean isLoading = true;
    static int amountProduct;
    Scanner input = new Scanner(System.in);
    private boolean changesCommitted;
    private boolean delete;


    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    public ProductController(List<Product> products) {
        this.products = products;
        this.usedProductCodes = new HashSet<>();
        this.nextProductNumber = loadNextProductNumber();
        this.changesCommitted = false;
    }

    public void index(){
       // not file
        if(Files.exists(Paths.get("data/product.dat"))){
            products.clear();
            usedProductCodes.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader("data/product.dat"))) {
                String line;
                int i=0;
                int count = 1;
                long startTime = System.nanoTime();
                while ((line = reader.readLine()) != null) {
                    Product product = parseProductLine(line);
                    if (product != null) {
                        products.add(product);
                        usedProductCodes.add(product.getCode());
                        if (count == 100) System.out.print(green+"\r♨️ Data is Loading ...../");
                        if (count == 200) {
                            System.out.print("\r♨️ Data is Loading .....\\ ");
                            count=1;
                        }
                        count++;
                        i++;
                    }
                }
                long endTime = System.nanoTime(); // End time
                long resultTime = endTime - startTime;
                if(resultTime > 100000)
                    System.out.println(reset+"\r♨️ loading spent times: " + (resultTime /  1_000_000_000.0) + " seconds." + reset);
                else
                    System.out.println("\r♨️ loading spent times: 0.00002 seconds." + reset);
            } catch (IOException e) {
                System.err.println("Error reading data from file: " + e.getMessage());
            }
            System.out.println();
        }
        else System.out.println("⚠️ no file...!");
    }

    public void writeToList(){
        products.clear();
        usedProductCodes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/product.dat"))) {
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


    public void randomWrite() {
        System.out.print("➡️ Enter random amount = ");
        int amount = scanner.nextInt();
        amountProduct = amount;
        scanner.nextLine();
        System.out.println("\uD83E\uDD14 Do you want to append or override ?");
        System.out.println("(a). Append");
        System.out.println("(o). Override");
        System.out.println("(b). Back to main menu");
        System.out.print("➡️ Enter choice: ");
        String save = scanner.nextLine();
        boolean status = false;
        if(save.equalsIgnoreCase("a")) status = true;
        else if(save.equalsIgnoreCase("o")) status = false;
        long startTime = System.nanoTime(); // Start time
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/product.dat",status))) {
            int i = 0;
            int count = 0;
            if (Objects.equals(save, "a")) {
                int lastProductNumber = getLastProductNumber(); // Get the last product number from the transaction file
                while (i < amount) {
                    String productCode = "CSTAD-" + (lastProductNumber + i + 1); // Generate product code
                    // Create new product only if the code is not already used
                    if (!usedProductCodes.contains(productCode)) {
                        Product newProduct = new Product(productCode, "P" + (lastProductNumber + i + 1), 10.00d, 10, LocalDate.now().toString());
                        String serializedProduct = serializeProduct(newProduct);
                        writer.write(serializedProduct);
                        writer.newLine();
                        usedProductCodes.add(productCode);
                    }
                    if (count == 100) System.out.print("\r♨️ Data is Loading ...../");
                    if (count == 200) {
                        System.out.print("\r♨️ Data is Loading .....\\ ");
                        count=1;
                    }
                    count++;
                    i++;
                }
            }
            else if (Objects.equals(save, "o")) {
                while (i < amount) {
                    Product newProduct = new Product("CSTAD-"+(i+1), "P" + (i + 1), 10.00d, 10, LocalDate.now().toString());
                    String serializedProduct = serializeProduct(newProduct);
                    writer.write(serializedProduct);
                    writer.newLine();
                    if (count == 100) System.out.print("\r♨️ Data is Loading ...../");
                    if (count == 200) {
                        System.out.print("\r♨️ Data is Loading .....\\ ");
                        count=1;
                    }
                    count++;
                    i++;
                }
            }
            else return;
        } catch (IOException e) {
            System.err.println("Error writing to transaction file: " + e.getMessage());
        }
        System.out.println("\r"+amount + " ✅ Products created successfully.");
        long endTime = System.nanoTime(); // End time
        long resultTime = endTime - startTime;
        if(resultTime > 100000)
            System.out.println(reset+"♨️ write "+amount+" products spent spent: " + (resultTime /  1_000_000_000.0) + " seconds.");
        else
            System.out.println(reset+"♨️ write "+amount+" products spent spent: " + (resultTime / 1_000_000.0) + " milliseconds.");
    }

    // Helper method to get the last product number from the transaction file
    private int getLastProductNumber() {
        int lastProductNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("data/product.dat"))) {
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
            int count=0;
            while ((line = reader.readLine()) != null) {
                Product product = parseProductLine(line);
                if (product != null) {
                    products.add(product);
                    usedProductCodes.add(product.getCode());
                }
                if (count == 100) System.out.print("\r♨️ Data is Loading ...../");
                if (count == 200) {
                    System.out.print("\r♨️ Data is Loading .....\\ ");
                    count=1;
                }
                count++;
                i+=1;
            }
            long endTime = System.nanoTime();
            long resultTime = endTime - startTime;
            if(resultTime > 100000)
                System.out.println(reset+"\r♨️ read "+products.size()+" products spent spent: " + (resultTime /  1_000_000_000.0) + " seconds.");
            else
                System.out.println(reset+"\r♨️ read "+products.size()+" products spent spent: " + (resultTime / 1_000_000.0) + " milliseconds.");
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
        try {
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
        }catch (Exception e){
            System.out.println("[!] Incorrect Input....");
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
        System.out.print("❔ Are you sure you want to commit? [Y/N]: ");
        String answer = scanner.nextLine();
        if(answer.equalsIgnoreCase("y")){

            if (delete){
                try (BufferedReader reader = new BufferedReader(new FileReader("data/transaction.dat"));
                     BufferedWriter writer = new BufferedWriter(new FileWriter("data/product.dat"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                    System.out.println(blue+"✅ Changes committed successfully!"+reset);
                    this.changesCommitted = true; // Set the flag indicating changes have been committed
                } catch (IOException e) {
                    System.err.println("Error committing changes: " + e.getMessage());
                }
                delete=false;
                return;
            }
            if (products.isEmpty()){
                System.out.println(yellow+"⚠️ No data, can't commit...!");
            }
            else{
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
                    System.out.println(blue+"✅ Changes committed successfully!"+reset);
                    this.changesCommitted = true; // Set the flag indicating changes have been committed
                } catch (IOException e) {
                    System.err.println("Error committing changes: " + e.getMessage());
                }
            }
        }
        else if(answer.equalsIgnoreCase("n")){
            System.out.println("❗You didn't commit anything...!");
        }
        else System.out.println("Invalid input...!");
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

    public void deleteProductByCode(String code){
        System.out.println("[#] Are you sure you want to delete this product's ??? [Y/N]: ");
        String answer = new Scanner(System.in).next();
        if (answer.equalsIgnoreCase("y")) {
            for (int i = 0; i < products.size(); i++) {
                // Get the product at the current index
                Product product = products.get(i);

                // Check if the code of the current product matches the given code
                if (product.getCode().equals(code)) {
                    // Remove the product from the list
                    products.remove(i);
                    // Decrement the index as the size of the list has decreased
                    i--;
                    writeProductsToFile();
                    delete=true;
                    break;
                }
            }
        }else if (answer.equalsIgnoreCase("n")){
            System.out.println("[#] You didn't delete anything.....");
            delete=false;
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
    public Boolean clearFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("");
        } catch (IOException e) {
            return false;
        }
        products.clear();
        return true;
    }

}
