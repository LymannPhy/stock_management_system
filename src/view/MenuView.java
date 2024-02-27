package view;

public class MenuView {
    public static void displayMenu() {
        System.out.println("Application Menu");
        System.out.println("""
       ╔═════════════════════════════════════════════════════════════════════════════════════════════╗
       ║ Disp(l)ay  |  Rando(m)   |   W)rite    |   R)ead     |   (E)dit   |   (D)elete  | (S)earch  ║
       ║ Set R(o)ws |  (C)ommit   |   Res(t)ore |   Bac(k) up |   (H)elp   |   E(x)it                ║                                              
       ╚═════════════════════════════════════════════════════════════════════════════════════════════╝
        """);
    }
    // Help User
    public static void helpUser() {
        System.out.println("""
        ╔═════════════════════════════════════════════════════════════════════════════════════════════╗
        ║ 1.       Press       L, l  :   Display products as table                                    ║
        ║ 2.       Press       W, w  :   Create a new product                                         ║
        ║ 3.       Press       M, m  :                                                                ║
        ║ 4.       Press       R, r  :   View product detail by code                                  ║
        ║ 5.       Press       E, e  :   Edit an existing product by code                             ║
        ║ 6.       Press       D, d  :   Delete an existing product by code                           ║
        ║ 7.       Press       S, s  :   Search an existing product by name                           ║
        ║ 8.       Press       C, c  :   Commit transactional data                                    ║
        ║ 9.       Press       K, k  :   Backup data                                                  ║
        ║ 10.      Press       R, r  :   Restore data                                                 ║
        ║ 11.      Press       F, f  :   Navigate pagination to the last page                         ║
        ║ 12.      Press       P, p  :   Navigate pagination to the previous page                     ║
        ║ 13.      Press       N, n  :   Navigate pagination to the Next page                         ║
        ║ 14.      Press       H, h  :   Navigate pagination to the last page                         ║
        ║ 15.      Press       B, b  :   Step back of the application                                 ║
        ║ 16.      Press       X, x  :   Exit the application                                         ║
        ╚═════════════════════════════════════════════════════════════════════════════════════════════╝
        """);
    }
}
