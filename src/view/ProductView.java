package view;

import model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class ProductView {
    Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);
    public void displayProducts(List<Product> products) {
        System.out.println("Product List as Table");
        table.addCell("Code");
        table.addCell("Name");
        table.addCell("Price");
        table.addCell("QTY");
        table.addCell("Imported At");
        for (Product product : products) {
            table.addCell(product.getCode());
            table.addCell(product.getName());
            table.addCell(product.getPrice().toString());
            table.addCell(product.getQty().toString());
            table.addCell(product.getImported_at());
        }
        System.out.println(table.render());
    }
    // Method to display product details
    // Method to display product details
    public void displayProductDetails(Product product) {
        if (product != null) {
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
            System.out.println("Product Details:");
            table.addCell("ID" + " ".repeat(20) + ": " + product.getCode());
            table.addCell("Name" + " ".repeat(18) + ": " + product.getName());
            table.addCell("Unit Price" + " ".repeat(12) + ": " + product.getPrice());
            table.addCell("Qty" + " ".repeat(19) + ": " + product.getQty());
            table.addCell("Imported Date" + " ".repeat(9) + ": " + product.getImported_at());
            System.out.println(table.render());
        } else {
            System.out.println("Product not found.");
        }
    }
    // preview All Update
    public void previewUpdate(String code,String name,double price,int qty,String date) {
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        System.out.println("Product's All Details Preview After Update:");
        table.addCell("ID" + " ".repeat(20) + ": " + code);
        table.addCell("Name" + " ".repeat(18) + ": " + name);
        table.addCell("Unit Price" + " ".repeat(12) + ": " + price);
        table.addCell("Qty" + " ".repeat(19) + ": " + qty);
        table.addCell("Imported Date" + " ".repeat(9) + ": " + date);
        System.out.println(table.render());
    }
}
