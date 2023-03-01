package com.konstantion.view;

import com.google.common.collect.Lists;
import com.konstantion.controller.CliBucketController;
import com.konstantion.controller.CliOrderController;
import com.konstantion.controller.CliProductController;
import com.konstantion.controller.CliReviewController;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.review.dto.CreationReviewDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.UUID;

@Component
public class CliUI {
    private final CliProductController productController;
    private final CliBucketController bucketController;
    private final CliOrderController orderController;

    private final CliReviewController reviewController;
    private static final Scanner scanner = new Scanner(System.in);
    private final Logger logger = LoggerFactory.getLogger(CliUI.class);
    private static final String NO_SUCH_OPTION = "No such option provided";

    private static final String WRONG_FORMAT = "Wrong format of input";
    private String[] userInput;

    public CliUI(CliProductController productController, CliBucketController bucketController, CliOrderController orderController, CliReviewController reviewController) {
        this.productController = productController;
        this.bucketController = bucketController;
        this.orderController = orderController;
        this.reviewController = reviewController;
    }

    public void printMainDialog() {
        String dialog = """
                Commands:
                create-product [name] [price]
                delete-product [uuid]
                show-products-rating
                show-products [name/price]
                add-product-to-bucket [uuid] [quantity]
                remove-product-from-bucket [uuid]
                show-products-in-bucket
                create-order
                create-review [product-uuid] [message] [rating 1-5]
                """;
        logger.info(dialog);
        mainDialogHandler();
    }

    public void mainDialogHandler() {
        if (scanner.hasNextLine()) {
            userInput = scanner.nextLine().trim().split("\\s+");

            switch (userInput[0]) {
                case "create-product" -> addProduct();
                case "delete-product" -> deleteProducts();
                case "show-products" -> showProducts();
                case "show-products-rating" -> showProductsRating();
                case "add-product-to-bucket" -> addProductBucket();
                case "remove-product-from-bucket" -> removeProductBucket();
                case "show-products-in-bucket" -> showProductsBucket();
                case "create-order" -> createOrder();
                case "create-review" -> createReview();
                default -> logger.info(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            logger.info(NO_SUCH_OPTION);
        }
        printMainDialog();
    }

    private void showProductsRating() {
        try {
            Lists.reverse(productController.getProductsWithRating())
                    .forEach(System.out::println);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void createReview() {
        try {
            UUID productUuid = UUID.fromString(userInput[1]);
            String message = userInput[2];
            Integer rating = Integer.valueOf(userInput[3]);
            CreationReviewDto creationReviewDto = new CreationReviewDto(message, rating, productUuid);
            reviewController.createReview(creationReviewDto);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void createOrder() {
        try {
            orderController.createOrder();
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void showProductsBucket() {
        try {
            bucketController.getBucketProducts()
                    .forEach((key, value) -> System.out.println(key + ":" + value));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void removeProductBucket() {
        try {
            UUID uuid = UUID.fromString(userInput[1]);
            bucketController.removeProduct(uuid);

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void addProductBucket() {
        try {
            UUID uuid = UUID.fromString(userInput[1]);
            Integer quantity = Integer.valueOf(userInput[2]);
            bucketController.addProduct(uuid, quantity);

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void showProducts() {
        try {
            String parameter = userInput[1];
            productController.getProducts(Sort.Direction.ASC, parameter.toLowerCase())
                    .forEach(System.out::println);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void deleteProducts() {
        try {
            UUID uuid = UUID.fromString(userInput[1]);
            productController.deleteProduct(uuid);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void addProduct() {
        try {
            String name = userInput[1];
            Double price = Double.valueOf(userInput[2]);
            CreationProductDto creationProductDto = new CreationProductDto(name, price);
            productController.addProduct(creationProductDto);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            handleFormatException(e);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (Exception e) {
            handleException(e);
        } finally {
            printMainDialog();
        }
    }

    private void handleFormatException(RuntimeException e) {
        logger.error("{} {}", WRONG_FORMAT, e.getMessage());
        scanner.next();
    }

    private void handleValidationException(ValidationException e) {
        logger.error(e.getMessage());
        scanner.next();
    }

    private void handleException(Exception e) {
        logger.error("{} error:{}",NO_SUCH_OPTION,  e.getMessage());
        scanner.next();
    }

}
