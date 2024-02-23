import controller.ProductController;
import model.Product;
import view.ProductView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductController controller = new ProductController();
        //Create new product
//        controller.createProduct();

        //Read data from tramsaction source file
        controller.readDataFromFile("data/transaction.dat");
        // Get the products from the controller
        List<Product> products = controller.getProducts();
        // Instantiate the view and display the products
        ProductView view = new ProductView();
        view.displayProducts(products);
    }
}