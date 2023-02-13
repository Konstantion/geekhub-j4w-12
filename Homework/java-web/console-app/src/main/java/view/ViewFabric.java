package view;

import controller.ProductController;

import java.util.Scanner;

public class ViewFabric {
    private final ProductController productController;
    private static final Scanner scanner = new Scanner(System.in);
    private static final String NO_SUCH_OPTION = "No such option provided";

    public ViewFabric(ProductController productController) {
        this.productController = productController;
    }

    public static void printMainDialog() {
        String dialog = """
                If you want to add product press 1
                If you want to delete product press 2
                If you want to see products press 3
                """;
        System.out.println(dialog);
        mainDialogHandler();
    }

    public static void mainDialogHandler() {
        if (scanner.hasNextInt()) {
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> addProductDialog();
                default -> System.out.println(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            System.out.println(NO_SUCH_OPTION);
        }
        printMainDialog();
    }

    public static void addProductDialog() {
        System.out.println("Enter product name:");

    }
}
