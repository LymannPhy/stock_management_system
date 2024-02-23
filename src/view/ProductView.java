package view;

import model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class ProductView {
    public void displayProducts(List<Product> products) {

        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE);
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
}
