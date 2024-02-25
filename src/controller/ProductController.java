package controller;

import model.Product;

import java.io.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import view.ProductView;

import static model.Product.parseProductLine;

public class ProductController {
    private List<Product> products;
    private HashSet<String> usedProductCodes;
    private int nextProductNumber;

    public ProductController(List<Product> products) {
        this.products = products;
        this.usedProductCodes = new HashSet<>();
        this.nextProductNumber = loadNextProductNumber(); // Load from file or database
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

    // Generate product code
    private String generateProductCode() {
        String codePrefix = "CSTAD-";
        String code = codePrefix + nextProductNumber;
        nextProductNumber++; // Increment the product number for the next product
        return code;
    }

    // Method to read data from the file and parse it into Product objects
    public void readDataFromFile(String filename) {
        // Clear the products list and used product codes set before reading new data
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

    private String serializeProduct(Product product) {
        return String.format("%s,%s,%.2f,%d,%s", product.getCode(), product.getName(), product.getPrice(), product.getQty(), product.getImported_at());
    }
    //Update Product By Code
    public void updateProduct(String code) {
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
                System.out.print("> Product's Name :");
                String productName = scanner.nextLine();
                System.out.print("> Product's Price:");
                double productPrice = Double.parseDouble(scanner.nextLine());
                System.out.print("> Product's QTY :");
                int productQty = Integer.parseInt(scanner.nextLine());
                System.out.print("[!] Are you sure you want to Update?? [Y/N] :");
                String want = sure.next();
                if (want.equalsIgnoreCase("y")) {
                    for (Product product : products) {
                        if (product.getCode().equals(code)) {
                            product.setName(productName);
                            product.setPrice(productPrice);
                            product.setQty(productQty);
                            writeProductsToFile();
                            System.out.println("Update Product Successfully!!");
                            return;
                        }
                    }
                } else if (want.equalsIgnoreCase("n")){
                    System.out.println("# You didn't Update Anything");
                }
            }
            case "2" -> updateAttribute(code, scanner, "NAME");
            case "3" -> updateAttribute(code, scanner, "UNIT PRICE");
            case "4" -> updateAttribute(code, scanner, "QTY");
            case "5" -> System.out.println("# Back to menu");
            default-> System.out.println("# Invalid Input Choose! Please try again....");
        }
    }
    //Update single
    private void updateAttribute(String code, Scanner scanner, String attributeName) {
        Scanner sure = new Scanner(System.in);
        System.out.print("> Product's " + attributeName + " :");
        String attributeValue = scanner.nextLine();
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
                    writeProductsToFile();
                    System.out.println("Update Product's " + attributeName + " Successfully!!");
                    return;
                }
            }
        }
    }
    // Write the data to file
    private void writeProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat"))) {
            for (Product product : products) {
                String serializedProduct = serializeProduct(product);
                writer.write(serializedProduct + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Error writing to transaction file: " + e.getMessage());
        }
    }


}
