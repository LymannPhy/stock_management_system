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
        System.out.println(blue+"☆".repeat(71) );
        System.out.print("""
       ╔═════════════════════════════════════════════════════════════════════════════════════════════════╗
       ║                                        Application Menu                                         ║
       ║─────────────────────────────────────────────────────────────────────────────────────────────────║
       ║   Disp(l)ay  |  Rando(m)   |   W)rite    |   R)ead     |   (E)dit   |   (D)elete  | (S)earch    ║
       ║   Set R(o)ws |  (C)ommit   |   Res(t)ore |   Bac(k) up |   (H)elp   |   E(x)it    | (*)Clear    ║\s
       ║                                                                                                 ║\s
       ╚═════════════════════════════════════════════════════════════════════════════════════════════════╝
        """ );
        System.out.println("☆".repeat(71) + reset);
    }
    public static void displayHelp(){
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        System.out.println("#" + "=".repeat(27)+" Help Instruction "+"=".repeat(28)+"#");
        table.addCell(blue+"1.       Press       L, l  :   Display products as table");
        table.addCell(blue+"2.       Press       W, w  :   Create a new product ");
        table.addCell(blue+"3.       Press       M, m  :   Random Product");
        table.addCell(blue+"4.       Press       A, a  :   Append Data");
        table.addCell(blue+"5.       Press       O, o  :   Override Data");
        table.addCell(blue+"6.       Press       R, r  :   View product detail by code");
        table.addCell(blue+"7.       Press       E, e  :   Edit an existing product by code");
        table.addCell(blue+"8.       Press       D, d  :   Delete an existing product by code");
        table.addCell(blue+"9.       Press       S, s  :   Search an existing product by name");
        table.addCell(blue+"10.      Press       C, c  :   Commit transactional data");
        table.addCell(blue+"11.      Press       K, k  :   Backup data");
        table.addCell(blue+"12.      Press       R, r  :   Restore data");
        table.addCell(blue+"13.      Press       F, f  :   Navigate pagination to the first page");
        table.addCell(blue+"14.      Press       P, p  :   Navigate pagination to the previous page");
        table.addCell(blue+"15.      Press       N, n  :   Navigate pagination to the Next page");
        table.addCell(blue+"16.      Press       L, l  :   Navigate pagination to the last page");
        table.addCell(blue+"17.      Press       H, h  :   Help");
        table.addCell(blue+"18.      Press       B, b  :   Step back of the application");
        table.addCell(blue+"19.      Press       *     :   Clear data from all files");
        table.addCell(blue+"20.      Press       X, x  :   Exit the application"+reset);
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
