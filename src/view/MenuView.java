package view;

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
}
