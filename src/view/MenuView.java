package view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class MenuView implements Color {

    public void logo(){
        String logo = """
                \t\t\t\t\t ╔══════════════════════════════════════════════════════╗
                \t\t\t\t\t ║ \t\t ██████╗███████╗████████╗ █████╗ ██████╗        ║
                \t\t\t\t\t ║ \t\t██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗       ║
                \t\t\t\t\t ║ \t\t██║     ███████╗   ██║   ███████║██║  ██║       ║
                \t\t\t\t\t ║ \t\t██║     ╚════██║   ██║   ██╔══██║██║  ██║       ║
                \t\t\t\t\t ║ \t\t╚██████╗███████║   ██║   ██║  ██║██████╔╝       ║
                \t\t\t\t\t ║ \t\t ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝        ║
                \t\t\t\t\t ║ \t\t\t Center of Science Technology and           ║
                \t\t\t\t\t ║ \t\t\t\t Advanced Development                   ║
                \t\t\t\t\t ║ \t\t☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆      ║
                \t\t\t\t\t ║ \t\t\t\t\t Stock Manager                      ║
                \t\t\t\t\t ║ \t\t☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆      ║
                \t\t\t\t\t ╚══════════════════════════════════════════════════════╝
                """;
        System.out.println("\n"+ yellow +logo + reset);
    }

    // Method to display the menu
    public static void displayMenu() {
        //System.out.println(blue+ " ".repeat(35) +"➡️ Application Menu ⬅️" + reset);
        System.out.println(magenta+"☆".repeat(71) );
        System.out.print("""
       ╔═════════════════════════════════════════════════════════════════════════════════════════════════╗
       ║                                        Application Menu                                         ║
       ║─────────────────────────────────────────────────────────────────────────────────────────────────║
       ║   Disp(l)ay  |  Rando(m)   |   W)rite    |   R)ead     |   (E)dit   |   (D)elete  | (S)earch    ║
       ║   Set R(o)ws |  (C)ommit   |   Res(t)ore |   Bac(k) up |   (H)elp   |   E(x)it    | (Cl)ear     ║\s
       ║                                                                                                 ║\s
       ╚═════════════════════════════════════════════════════════════════════════════════════════════════╝
        """ );
        System.out.println("☆".repeat(71) + reset);
    }
    public static void displayHelp(){
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        System.out.println("#" + "=".repeat(27)+" Help Instruction "+"=".repeat(28)+"#");
        table.addCell(darkBlue+"1.       Press       L, l  :   Display products as table");
        table.addCell(darkBlue+"2.       Press       W, w  :   Create a new product ");
        table.addCell(darkBlue+"3.       Press       M, m  :   Random Product");
        table.addCell(darkBlue+"4.       Press       R, r  :   View product detail by code");
        table.addCell(darkBlue+"5.       Press       E, e  :   Edit an existing product by code");
        table.addCell(darkBlue+"6.       Press       D, d  :   Delete an existing product by code");
        table.addCell(darkBlue+"7.       Press       S, s  :   Search an existing product by name");
        table.addCell(darkBlue+"8.       Press       C, c  :   Commit transactional data");
        table.addCell(darkBlue+"9.       Press       K, k  :   Backup data");
        table.addCell(darkBlue+"10.      Press       R, r  :   Restore data    ");
        table.addCell(darkBlue+"11.      Press       F, f  :   Navigate pagination to the last page");
        table.addCell(darkBlue+"12.      Press       P, p  :   Navigate pagination to the previous page");
        table.addCell(darkBlue+"13.      Press       N, n  :   Navigate pagination to the Next page");
        table.addCell(darkBlue+"14.      Press       H, h  :   Navigate pagination to the last page");
        table.addCell(darkBlue+"15.      Press       B, b  :   Step back of the application");
        table.addCell(darkBlue+"16.      Press       X, x  :   Exit the application"+reset);
        System.out.println(table.render());
    }

    public void starting(){
        for (int i = 0; i <= 100; i+=2) {
            int totalBlocks = 30;
            int blocksToShow = (i * totalBlocks) / 100;
            System.out.print(" ".repeat(10) + " Starting [ " + i + "% ]");
            System.out.print(" ".repeat(10) + getProgressBar(blocksToShow, totalBlocks) + "\r");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(" "+reset);
    }

    private static String getProgressBar(int blocksToShow, int totalBlocks) {
        String progressBar = "█".repeat(Math.max(0, blocksToShow)) +
                " ".repeat(Math.max(0, totalBlocks - blocksToShow));
        return green + progressBar;
    }
}
