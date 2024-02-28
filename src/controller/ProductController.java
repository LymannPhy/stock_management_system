package controller;

import model.Product;
import view.ProductView;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import static model.Product.parseProductLine;

public class ProductController {
    private List<Product> products = new ArrayList<>();
    private String backupDirectory = "backup/";
    private HashSet<String> usedProductCodes;
    private ProductView view = new ProductView();
    private int nextProductNumber;
    Scanner input = new Scanner(System.in);

    public ProductController(List<Product> products) {
        this.products = products != null ? products : new ArrayList<>();
        this.usedProductCodes = new HashSet<>();
        this.nextProductNumber = loadNextProductNumber();
    }

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
             BufferedWriter writer = new BufferedWriter(new FileWriter("data/product.dat", true))) {
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
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
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
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found at path " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading product data: " + e.getMessage());
        }
    }




    // Method to backup product data to a file
    public void backupData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("backup/backup_product.bak"))) {
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
    public boolean backupDataTransactions() {
        File file = new File("backup/backup_product.bak");
        return file.exists() && file.length() > 0;
    }
    public void handleBackupDecision() {
        if (backupDataTransactions()) {
            System.out.println("You have uncommitted transactions.");
            System.out.print("Do you want to save or lose data?[Y/n]: ");
            String decision = input.nextLine().trim().toLowerCase();
            if (decision.equals("y")) {
                backupData();
            } else {
                System.out.println("Exiting the program without saving data.");
            }
        }
    }

}
