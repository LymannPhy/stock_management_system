package controller;

import model.Product;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import static model.Product.parseProductLine;

public class ProductController {
    private List<Product> products;
    private HashSet<String> usedProductCodes;
    private int nextProductNumber;

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

    private String serializeProduct(Product product) {
        return String.format("%s,%s,%.2f,%d,%s", product.getCode(), product.getName(), product.getPrice(), product.getQty(), product.getImported_at());
    }
}
