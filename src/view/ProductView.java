package view;

import model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.*;

public class ProductView implements Color {
    Scanner scanner = new Scanner(System.in);
    static Integer currentPage = 1;
    static Integer pageSize = 1;
    static Integer rowPerPage = 4;

    Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);

    public void displayProducts(List<Product> products) {
        while (true) {
            try {
                display(products);
                System.out.print("(B).Back or to Navigate page: ");
                String page = scanner.nextLine();
                if (Objects.equals(page, "b")) return;
                switch (page.toLowerCase()) {
                    case "f" -> {
                        currentPage = 1;
                    }
                    case "p" -> {
                        if(currentPage == 1)
                            currentPage = 1;
                        else
                            currentPage -= 1;
                    }
                    case "g" -> {
                        System.out.print("Enter numberPage to navigate = ");
                        Integer pageInput = scanner.nextInt();
                        currentPage = pageInput;
                        if(pageInput > pageSize){
                            System.out.println("The maximum pages is "+pageSize);
                            currentPage = pageSize;
                        }
                        else if(currentPage < 1){
                            System.out.println("The minimum page is "+1);
                            currentPage = 1;
                        }
                    }
                    case "n" -> {
                        if( currentPage == pageSize)
                            currentPage = pageSize;
                        else
                            currentPage += 1;
                    }
                    case "l" -> {
                        currentPage = pageSize;
                    }
                    default -> {
                        System.out.println("Invalidate option...!");
                    }
                }
            } catch (Exception e) {
                System.out.println("[!] Invalid Input...");
            }
        }
    }

    // pagination for display products
    public static void display(List<Product> product) {
        pageSize = (int) Math.ceil((double) product.size() / rowPerPage);
        int startIndex = (currentPage * rowPerPage) - rowPerPage; // 5
        int endIndex = Math.min(startIndex + rowPerPage, product.size());
        System.out.println("Total Page : " + pageSize);

        System.out.println("#"+"=".repeat(32)+" Products List as Table "+"=".repeat(32)+"#");
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        table.setColumnWidth(0,15,20);
        table.setColumnWidth(1,15,20);
        table.setColumnWidth(2,15,20);
        table.setColumnWidth(3,15,20);
        table.setColumnWidth(4,15,20);
        table.addCell(yellow+"Code",cellStyle);
        table.addCell(yellow+"Name",cellStyle);
        table.addCell(yellow+"Price",cellStyle);
        table.addCell(yellow+"QTY",cellStyle);
        table.addCell(yellow+"Imported At"+reset,cellStyle);
        try {
            for (int i = startIndex; i < endIndex; i++) {
                table.addCell(blue+product.get(i).getCode(), cellStyle);
                table.addCell(blue+product.get(i).getName(), cellStyle);
                table.addCell(blue+product.get(i).getPrice().toString(), cellStyle);
                table.addCell(blue+product.get(i).getQty().toString(), cellStyle);
                table.addCell(blue+product.get(i).getImported_at()+reset, cellStyle);
            }
            System.out.println(table.render());
            System.out.println("+" + "~".repeat(89) + "+");
            System.out.println(" Page " + currentPage + " of " + pageSize + " ".repeat(50) + "Total Record: " + product.size());
            System.out.println(" Page Navigation" + " ".repeat(25) + "(F).First (P).Previous (G).Goto (N).Next (L).Last");
            System.out.println("+" + "~".repeat(89) + "+");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No records to display.");
        }
    }

    public void setRow() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("#".repeat(15));
        System.out.println("# Set row to display in table");
        int rowInput = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("> Enter row: ");
                rowInput = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // consume the invalid input
            }
        }

        scanner.nextLine(); // consume newline character after nextInt()

        System.out.print("> Are you sure to set row display? [y/n]: ");
        String save = scanner.nextLine();
        if (save.equalsIgnoreCase("y")) {
            rowPerPage = rowInput;
            System.out.println("# Set row successfully...!");
        }
        System.out.println("#".repeat(15));
    }

    /*public void randomDisplay(List<Product> product){
        System.out.println("Products List as Table");
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);
        table.setColumnWidth(0,15,20);
        table.setColumnWidth(1,15,20);
        table.setColumnWidth(2,15,20);
        table.setColumnWidth(3,15,20);
        table.setColumnWidth(4,15,20);
        table.addCell(yellow+"Code");
        table.addCell(yellow+"Name");
        table.addCell("Price");
        table.addCell("QTY");
        table.addCell("Imported At");

        pageSize = (int) Math.ceil((double) product.size() / rowPerPage);
        int startIndex = (currentPage * rowPerPage) - rowPerPage; // 5
        int endIndex = Math.min(startIndex + rowPerPage, product.size());
        System.out.println("Total Page : " + pageSize);

        try {
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
        } catch (IndexOutOfBoundsException e) {
            // Handle the case when the index is out of bounds (i.e., when total page count is 0)
            System.out.println("No records to display.");
        }


    }*/


        // Method to display product details
    public void displayProductDetails(Product product) {
        if (product != null) {
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
            System.out.println("Product Details:");
            table.addCell(yellow+"ID" + " ".repeat(20) + ": " + product.getCode());
            table.addCell(yellow+"Name" + " ".repeat(18) + ": " + product.getName());
            table.addCell(yellow+"Unit Price" + " ".repeat(12) + ": " + product.getPrice());
            table.addCell(yellow+"Qty" + " ".repeat(19) + ": " + product.getQty());
            table.addCell(yellow+"Imported Date" + " ".repeat(9) + ": " + product.getImported_at()+reset);
            System.out.println(table.render());
        } else {
            System.out.println("Product not found.");
        }
    }
    public void previewUpdate(String code,String name,double price,int qty,String date) {
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        System.out.println("Product's All Details Preview After Update:");
        table.addCell(yellow+"ID" + " ".repeat(20) + ": " + code);
        table.addCell(yellow+"Name" + " ".repeat(18) + ": " + name);
        table.addCell(yellow+"Unit Price" + " ".repeat(12) + ": " + price);
        table.addCell(yellow+"Qty" + " ".repeat(19) + ": " + qty);
        table.addCell(yellow+"Imported Date" + " ".repeat(9) + ": " + date+reset);
        System.out.println(table.render());
    }

}
