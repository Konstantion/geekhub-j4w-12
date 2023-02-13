import controller.ProductController;
import product.ProductRepository;
import product.ProductService;
import product.validator.ProductValidations;
import product.validator.ProductValidator;
import view.ViewFabric;

public class ApplicationStarter {
    public static void main(String[] args) {
        ProductValidations productValidations = new ProductValidations();
        ProductValidator productValidator = new ProductValidator(productValidations);
        ProductRepository productRepository = new ProductRepository();
        ProductService productService = new ProductService(productValidator, productRepository);
        ProductController productController = new ProductController(productService);
        ViewFabric viewFabric = new ViewFabric(productController);

        boolean isRunning = true;
        while (isRunning) {
            viewFabric.printMainDialog();
            isRunning = false;
        }
    }
}
