package com.konstantion.file;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.konstantion.bill.Bill;
import com.konstantion.guest.Guest;
import com.konstantion.order.Order;
import com.konstantion.product.Product;
import com.konstantion.table.Table;
import com.konstantion.user.User;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.itextpdf.text.BaseColor.WHITE;

public class PdfUtils {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font CELL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    private static final BaseColor LIGHT_GRAY = new BaseColor(0xC0, 0xC0, 0xC0);

    private PdfUtils() {
    }

    public static void fillBillDocumentPdf(
            Bill bill,
            Order order,
            Map<Product, Long> products,
            Table table,
            User waiter,
            Guest guest,
            Document document
    ) throws DocumentException {
        // Add title
        Paragraph title = new Paragraph("Bill", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add subtitle
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Paragraph subtitle = new Paragraph(String.format("Date: %s", formatter.format(order.getCreatedAt())), SUBTITLE_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        // Add table number, waiter name, and guest name
        String waiterName = waiter != null ? String.join(" ", waiter.getFirstName(), waiter.getLastName()) : "";
        String tableName = table != null ? table.getName() : "";
        String guestName = guest != null ? guest.getName() : "";
        Paragraph tableInfo = new Paragraph(String.format("Table: %s%n Waiter: %s%n Guest: %s%n",
                tableName,
                waiterName,
                guestName), HEADER_FONT);
        tableInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(tableInfo);

        // Add products table
        PdfPTable productsTable = new PdfPTable(new float[]{4, 4, 2, 2});
        productsTable.setWidthPercentage(100);
        productsTable.setHeaderRows(1);

        // Add table headers
        productsTable.addCell(createCell("Product", Element.ALIGN_CENTER, LIGHT_GRAY));
        productsTable.addCell(createCell("Quantity", Element.ALIGN_CENTER, LIGHT_GRAY));
        productsTable.addCell(createCell("Price", Element.ALIGN_CENTER, LIGHT_GRAY));
        productsTable.addCell(createCell("Total", Element.ALIGN_CENTER, LIGHT_GRAY));

        products.forEach((product, quantity) -> {
            productsTable.addCell(createCell(product.getName()));
            productsTable.addCell(createCell(String.valueOf(quantity), Element.ALIGN_CENTER));
            productsTable.addCell(createCell(String.format("%.2f", product.getPrice()), Element.ALIGN_RIGHT));
            productsTable.addCell(createCell(String.format("%.2f", product.getPrice() * quantity), Element.ALIGN_RIGHT));
        });

        // Add products total
        productsTable.addCell(createCell("Total", 3, Element.ALIGN_RIGHT, WHITE));
        productsTable.addCell(createCell(String.format("%.2f", bill.getPrice()), Element.ALIGN_RIGHT, WHITE));

        // Add discount
        productsTable.addCell(createCell("Discount", 3, Element.ALIGN_RIGHT, WHITE));
        String discount = guest != null ? guest.getDiscountPercent() + "%" : "";
        productsTable.addCell(createCell(String.format("%s", discount), Element.ALIGN_RIGHT, WHITE));

        // Add total with discount
        productsTable.addCell(createCell("Total with Discount", 3, Element.ALIGN_RIGHT, WHITE));
        productsTable.addCell(createCell(String.format("%.2f", bill.getPriceWithDiscount()), Element.ALIGN_RIGHT, WHITE));

        document.add(productsTable);

        // Add bill closed time
        Paragraph closedAtParagraph = new Paragraph(String.format("Bill closed at: %s", formatter.format(bill.getCreatedAt())), HEADER_FONT);
        closedAtParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(closedAtParagraph);
    }

    private static PdfPCell createCell(String text, int colspan, int alignment, BaseColor background) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, HEADER_FONT));
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(background);
        cell.setPadding(5);
        return cell;
    }

    private static PdfPCell createCell(String text, int alignment, BaseColor background) {
        PdfPCell cell = new PdfPCell(new Phrase(text, CELL_FONT));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(background);
        cell.setPadding(5);
        return cell;
    }

    private static PdfPCell createCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, CELL_FONT));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        return cell;
    }

    private static PdfPCell createCell(String text) {
        return createCell(text, Element.ALIGN_LEFT, CELL_FONT);
    }

    private static PdfPCell createCell(String text, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        return cell;
    }
}
