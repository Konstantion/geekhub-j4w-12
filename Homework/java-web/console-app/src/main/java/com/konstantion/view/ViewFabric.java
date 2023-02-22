package com.konstantion.view;

import com.konstantion.controller.CliProductController;
import com.konstantion.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.konstantion.product.dto.CreationProductDto;

import java.util.Locale;
import java.util.Scanner;

import static org.springframework.data.domain.Sort.Direction.ASC;


@Component
public class ViewFabric {
    private final CliProductController productController;
    private static final Scanner scanner = new Scanner(System.in);

    private final Logger logger = LoggerFactory.getLogger(ViewFabric.class);
    private static final String NO_SUCH_OPTION = "No such option provided";

    public ViewFabric(CliProductController productController) {
        this.productController = productController;
    }

    public void printMainDialog() {
        String dialog = """
                                
                If you want to add product press 1
                If you want to delete product press 2
                If you want to see products press 3""";
        logger.info(dialog);
        mainDialogHandler();
    }

    public void mainDialogHandler() {
        if (scanner.hasNextInt()) {
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> addProductDialog();
                case 2 -> deleteProductsDialog();
                case 3 -> seeProductsDialog();
                default -> logger.info(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            logger.info(NO_SUCH_OPTION);
        }
        printMainDialog();
    }

    public void addProductDialog() {
        String name = null;
        Integer price = null;

        try {
            logger.info("Enter product name:");
            scanner.nextLine();
            if (scanner.hasNextLine()) {
                name = scanner.nextLine();
            }

            logger.info("Enter product price (decimal value)");
            if (scanner.hasNextInt()) {
                price = scanner.nextInt();
            }

            productController.addProduct(new CreationProductDto(name, price));
        } catch (ValidationException e) {
            logger.error(e.getMessage());
            scanner.next();
        } catch (Exception e) {
            logger.error(NO_SUCH_OPTION);
            scanner.next();
        } finally {
            printMainDialog();
        }
    }

    public void seeProductsDialog() {
        String parameter = "";
        logger.info("Enter parameter by which you want to sort products, " +
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
            logger.error(NO_SUCH_OPTION);
            scanner.next();
        } finally {
            printMainDialog();
        }
    }

    public void deleteProductsDialog() {
        Long id = null;
        logger.info("Enter product id");

        try {
            scanner.nextLine();
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }

            productController.deleteProduct(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            printMainDialog();
        }
    }
}
