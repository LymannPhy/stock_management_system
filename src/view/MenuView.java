package view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class MenuView {
    // Method to display the menu
    public static void displayMenu() {
        System.out.println("Application Menu");
        System.out.println("""
       ╔═════════════════════════════════════════════════════════════════════════════════════════════╗
       ║ Disp(l)ay  |  Rando(m)   |   W)rite    |   R)ead     |   (E)dit   |   (D)elete  | (S)earch  ║
       ║ Set R(o)ws |  (C)ommit   |   Res(t)ore |   Bac(k) up |   (H)elp   |   E(x)it                ║                                               ║
       ╚═════════════════════════════════════════════════════════════════════════════════════════════╝
        """);
    }
    public static void displayHelp(){
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        System.out.println("Help Instruction");
        table.addCell("1.       Press       L, l  :   Display products as table");
        table.addCell("2.       Press       W, w  :   Create a new product ");
        table.addCell("3.       Press       M, m  :   Random Product");
        table.addCell("4.       Press       R, r  :   View product detail by code");
        table.addCell("5.       Press       E, e  :   Edit an existing product by code");
        table.addCell("6.       Press       D, d  :   Delete an existing product by code");
        table.addCell("7.       Press       S, s  :   Search an existing product by name");
        table.addCell("8.       Press       C, c  :   Commit transactional data");
        table.addCell("9.       Press       K, k  :   Backup data");
        table.addCell("10.      Press       R, r  :   Restore data    ");
        table.addCell("11.      Press       F, f  :   Navigate pagination to the last page");
        table.addCell("12.      Press       P, p  :   Navigate pagination to the previous page");
        table.addCell("13.      Press       N, n  :   Navigate pagination to the Next page");
        table.addCell("14.      Press       H, h  :   Navigate pagination to the last page");
        table.addCell("15.      Press       B, b  :   Step back of the application");
        table.addCell("16.      Press       X, x  :   Exit the application");
        System.out.println(table.render());
    }
}
