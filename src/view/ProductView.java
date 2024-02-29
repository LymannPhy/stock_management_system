package view;

import model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ProductView implements Color {
    Scanner scanner = new Scanner(System.in);
    static Integer currentPage = 1;
    static Integer pageSize = 1;
    static Integer rowPerPage = 4;

    Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);

    public void displayProducts(List<Product> products) {
        while (true) {
            display(products);
            System.out.print("\uD83C\uDD71️.Back or to Navigate page: ");
            String page = scanner.nextLine();
            if(Objects.equals(page, "b")) return;
            switch (page.toLowerCase()){
                case "f" -> {
                    currentPage=1;
                }
                case "p" -> {
                    if(currentPage < 1){
                        System.out.println("The minimum page is 1 only.");
                        currentPage = 1;
                    }
                    else currentPage-=1;
                }
                case "g" -> {
                    System.out.print("➡️ Enter numberPage to navigate = ");
                    int pageInput = scanner.nextInt();
                    if(pageInput > pageSize) {
                        System.out.println("The maximum page is "+page+" only.");
                        currentPage = pageSize;
                    } else if (pageInput < 1) {
                        System.out.println("The minimum page is 1 only.");
                        currentPage = 1;
                    }
                    else currentPage = pageInput;
                }
                case "n" -> {
                    if(currentPage > pageSize){
                        System.out.println("The maximum page is "+page+" only.");
                        currentPage = pageSize;
                    }
                    else currentPage+=1;
                }
                case "l" -> {
                    currentPage = pageSize;
                }
                default -> {
                    System.out.println("⚠️ Invalidate option...!");
                }
            }
        }
    }

    // pagination for display products
    public static void display(List<Product> product) {
        System.out.println("Products List as Table");
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        table.setColumnWidth(0,15,20);
        table.setColumnWidth(1,15,20);
        table.setColumnWidth(2,15,20);
        table.setColumnWidth(3,15,20);
        table.setColumnWidth(4,15,20);
        table.addCell("Code",cellStyle);
        table.addCell("Name",cellStyle);
        table.addCell("Price",cellStyle);
        table.addCell("QTY",cellStyle);
        table.addCell("Imported At");

        pageSize = (int) Math.ceil((double) product.size() / rowPerPage);
        int startIndex = (currentPage * rowPerPage) - rowPerPage; // 5
        int endIndex = Math.min(startIndex + rowPerPage, product.size());
        System.out.println("Total Page : " + pageSize);

        for (int i = startIndex; i < endIndex; i++) {
            table.addCell(product.get(i).getCode(),cellStyle);
            table.addCell(product.get(i).getName(),cellStyle);
            table.addCell(product.get(i).getPrice().toString(),cellStyle);
            table.addCell(product.get(i).getQty().toString(),cellStyle);
            table.addCell(product.get(i).getImported_at(),cellStyle);
        }
        System.out.println(table.render());
        System.out.println("+" + "~".repeat(89) + "+");
        System.out.println(" Page " + currentPage + " of " + pageSize + " ".repeat(58) + "Total Record: " + product.size());
        System.out.println(" Page Navigation" + " ".repeat(25) + "(F).First (P).Previous (G).Goto (N).Next (L).Last");
        System.out.println("+" + "~".repeat(89) + "+");
    }

    public void setRow(){
        System.out.println("#️⃣".repeat(15));
        System.out.println("#️⃣ Set row to display in table");
        System.out.print("➡️ Enter row: ");
        int rowInput = scanner.nextInt();
        scanner.nextLine();
        System.out.print("\uD83E\uDD14 Are you sure to set row display? [y/n]: ");
        String save = scanner.nextLine();
        if(save.equalsIgnoreCase("y")){
            rowPerPage = rowInput;
            System.out.println("✔️  Set row successfully...!");
        }
        System.out.println("#️⃣".repeat(15));
    }

    /*public void randomDisplay(List<Product> product){
        System.out.println("Products List as Table");
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);
        table.setColumnWidth(0,15,20);
        table.setColumnWidth(1,15,20);
        table.setColumnWidth(2,15,20);
        table.setColumnWidth(3,15,20);
        table.setColumnWidth(4,15,20);
        table.addCell("Code");
        table.addCell("Name");
        table.addCell("Price");
        table.addCell("QTY");
        table.addCell("Imported At");

        pageSize = (int) Math.ceil((double) product.size() / rowPerPage);
        int startIndex = (currentPage * rowPerPage) - rowPerPage; // 5
        int endIndex = Math.min(startIndex + rowPerPage, product.size());
        System.out.println("Total Page : " + pageSize);

        for (int i = startIndex; i < endIndex; i++) {
            table.addCell(product.get(i).getCode());
            table.addCell(product.get(i).getName());
            table.addCell(product.get(i).getPrice().toString());
            table.addCell(product.get(i).getQty().toString());
            table.addCell(product.get(i).getImported_at());
        }
        System.out.println(table.render());
        System.out.println("+" + "~".repeat(89) + "+");
        System.out.println(" Page " + currentPage + " of " + pageSize + " ".repeat(58) + "Total Record: " + product.size());
        System.out.println(" Page Navigation" + " ".repeat(25) + "(F).First (P).Previous (G).Goto (N).Next (L).Last");
        System.out.println("+" + "~".repeat(89) + "+");

    }*/


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
            System.out.println("\uD83D\uDEAB Product not found.");
        }
    }
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
