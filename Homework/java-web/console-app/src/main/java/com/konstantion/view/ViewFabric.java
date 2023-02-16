package com.konstantion.view;

import com.konstantion.controller.ProductController;
import com.konstantion.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import com.konstantion.product.dto.CreationProductDto;

import java.util.Locale;
import java.util.Scanner;

import static org.springframework.data.domain.Sort.Direction.ASC;

@SuppressWarnings("java:S106")
@Component
public class ViewFabric {
    private final ProductController productController;
    private static final Scanner scanner = new Scanner(System.in);
    private static final String NO_SUCH_OPTION = "No such option provided";

    public ViewFabric(ProductController productController) {
        this.productController = productController;
    }

    public void printMainDialog() {
        String dialog = """
                If you want to add product press 1
                If you want to delete product press 2
                If you want to see products press 3""";
        System.out.println(dialog);
        mainDialogHandler();
    }

    public void mainDialogHandler() {
        if (scanner.hasNextInt()) {
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> addProductDialog();
                case 2 -> deleteProductsDialog();
                case 3 -> seeProductsDialog();
                default -> System.out.println(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            System.out.println(NO_SUCH_OPTION);
        }
        printMainDialog();
    }

    public void addProductDialog() {
        String name = null;
        Integer price = null;

        try {
            System.out.println("Enter product name:");
            scanner.nextLine();
            if (scanner.hasNextLine()) {
                name = scanner.nextLine();
            }

            System.out.println("Enter product price (decimal value)");
            if (scanner.hasNextInt()) {
                price = scanner.nextInt();
            }

            productController.addProduct(new CreationProductDto(name, price));
        } catch (ValidationException e) {

        } catch (Exception e) {
            System.out.println(NO_SUCH_OPTION);
            scanner.next();
        } finally {
            printMainDialog();
        }
    }

    public void seeProductsDialog() {
        String parameter = "";
        System.out.println("Enter parameter by which you want to sort products, " +
                           "existing parameters are price/name default is id");

        try {
            scanner.nextLine();
            if (scanner.hasNextLine()) {
                parameter = scanner.nextLine();
            }

            productController.getProducts(ASC, parameter.toLowerCase(Locale.ROOT)).forEach(
                    System.out::println
            );
        } catch (Exception e) {
            scanner.next();
        } finally {
            printMainDialog();
        }
    }

    public void deleteProductsDialog() {
        Long id = null;
        System.out.println("Enter product id");

        try {
            scanner.nextLine();
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }

            productController.deleteProduct(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            printMainDialog();
        }
    }
}
