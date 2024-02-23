package model;

import java.util.Date;
import java.util.Objects;

public class Product {
    private String code;
    private String name;
    private Double price;
    private Integer qty;
    private String imported_at;

    public Product(){}
    public Product(String code, String name, Double price, Integer qty, String imported_at){
        this.code = code;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.imported_at = imported_at;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getImported_at() {
        return imported_at;
    }

    public void setImported_at(String imported_at) {
        this.imported_at = imported_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(code, product.code) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(qty, product.qty) && Objects.equals(imported_at, product.imported_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, price, qty, imported_at);
    }
    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", imported_at=" + imported_at +
                '}';
    }
    public static Product parseProductLine(String line) {
        String[] fields = line.split(",");
        if (fields.length != 5) {
            // Handle invalid line
            return null;
        }
        try {
            String code = fields[0];
            String name = fields[1];
            double price = Double.parseDouble(fields[2]);
            int qty = Integer.parseInt(fields[3]);
            String imported_at = fields[4];

            return new Product(code, name, price, qty, imported_at);
        } catch (NumberFormatException e) {
            // Handle parsing errors
            return null;
        }
    }
}
