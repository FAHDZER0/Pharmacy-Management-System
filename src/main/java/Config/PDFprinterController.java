package Config;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFprinterController {

    public static class InvoiceItem {
        String ItemName;
        String price;
        String amount;
        String PricePerItem;

        public InvoiceItem(String ItemName, String price, String amount, String PricePerItem) {
            this.ItemName = ItemName;
            this.price = price;
            this.amount = amount;
            this.PricePerItem = PricePerItem;
        }
    }

    public void PrintSalesIntoPDF(String orderType, String cashierName, String invoiceNumber,
                                  String customerNumber, String customerName, String customerAddress, String notes, List<InvoiceItem> items,
                                  String totalPrice, String discountPercentage, String deliveryPrice, String finalTotalPrice) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(612, 792)); // Standard invoice size
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add header image
            PDImageXObject pdImage = PDImageXObject.createFromFile("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\resources\\Images\\Login_pic_2.png", document);
            contentStream.drawImage(pdImage, 450, 730, 50, 50); // Adjust as per your image size

            // Add pharmacy information
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, 730);
            contentStream.showText("Pharmacy Name");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("123 Pharmacy St., Any City, ST 12345");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Tel: 0123 456 7890, 0111 222 3333");
            contentStream.endText();

            // Add order and cashier details
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, 680);
            contentStream.showText("Order Type:");
            contentStream.newLineAtOffset(70, 0);
            contentStream.showText(orderType);
            contentStream.newLineAtOffset(-70, -15);
            contentStream.showText("Cashier:");
            contentStream.newLineAtOffset(50, 0);
            contentStream.showText(cashierName);
            contentStream.endText();

            // Add invoice details
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, 650);
            contentStream.showText("Invoice No:");
            contentStream.newLineAtOffset(70, 0);
            contentStream.showText(invoiceNumber);
            contentStream.newLineAtOffset(-70, -15);
            contentStream.showText("Customer No:");
            contentStream.newLineAtOffset(80, 0);
            contentStream.showText(customerNumber);
            contentStream.endText();

            // Add bill to information
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, 620);
            contentStream.showText("Bill to:");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(70, 605);
            contentStream.showText(customerName);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(customerAddress);
            contentStream.endText();

            // Add table headers
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, 570);
            contentStream.showText("Item");
            contentStream.newLineAtOffset(50, 0);
            contentStream.showText("Item Name");
            contentStream.newLineAtOffset(160, 0);
            contentStream.showText("Amount");
            contentStream.newLineAtOffset(80, 0);
            contentStream.showText("Price per Item");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Total Price");
            contentStream.endText();

            // Add table content
            float yPosition = 550;
            int itemIndex = 1;
            for (InvoiceItem item : items) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(70, yPosition);
                contentStream.showText(String.valueOf(itemIndex));
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText(item.ItemName);
                contentStream.newLineAtOffset(180, 0);
                contentStream.showText(item.amount);
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(item.price);
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText(item.PricePerItem);
                contentStream.endText();
                yPosition -= 20;
                itemIndex++;
            }


            // Add current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy HH:mm");
            String formattedDateTime = now.format(formatter);

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(250, yPosition -= 20);
            contentStream.showText(formattedDateTime);
            contentStream.endText();


            // Adjust position for the notes section
            yPosition -= 20;

            // Add notes if available
            if (notes != null && !notes.isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(70, yPosition);
                contentStream.showText("Notes: " + notes);
                contentStream.endText();
                yPosition -= 20;
            }

            // Add total, discount, and delivery details dynamically based on the last position
            yPosition -= 20;

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, yPosition);
            contentStream.showText("Total Price:");
            contentStream.newLineAtOffset(150, 0);
            contentStream.showText(totalPrice);
            contentStream.endText();
            yPosition -= 20;

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, yPosition);
            contentStream.showText("Discount Percentage:");
            contentStream.newLineAtOffset(150, 0);
            contentStream.showText(discountPercentage);
            contentStream.endText();
            yPosition -= 20;

            if (deliveryPrice != null && !deliveryPrice.isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.newLineAtOffset(70, yPosition);
                contentStream.showText("Delivery Price:");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText(deliveryPrice);
                contentStream.endText();
                yPosition -= 20;
            }

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(70, yPosition);
            contentStream.showText("Total Price of the Invoice:");
            contentStream.newLineAtOffset(150, 0);
            contentStream.showText(finalTotalPrice);
            contentStream.endText();
            yPosition -= 20;

            // Add footer
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.newLineAtOffset(70, yPosition - 20);
            contentStream.showText("If you have any questions, please contact: Pharmacy@gmail.com");
            contentStream.endText();

            contentStream.close();

            //saving the document in directory named "Sales Invoices" in the project directory
            document.save("Pharmacy-Management-System/Sales Invoices/" + invoiceNumber + ".pdf");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method to test the class
//    public static void main(String[] args) {
//        PDFprinterController pdfprinterController = new PDFprinterController();
//
//        List<InvoiceItem> items = List.of(
//                new InvoiceItem("Penadol", "$200", "1", "$200"),
//                new InvoiceItem("Cough Medicine", "$500", "1", "$500"),
//                new InvoiceItem("Seyrum", "$500", "2", "$1000"),
//                new InvoiceItem("Plasters", "$200", "1", "$200"),
//                new InvoiceItem("Hair Brush", "$500", "1", "$500")
//        );
//
//        pdfprinterController.PrintSalesIntoPDF(
//                "Delivery",
//                "Fahd Ahmed",
//                "00000001",
//                "1",
//                "Flora Osama",
//                "District 30 Karor, Bedaya Tower 2, 3rd Floor, Flat 6, Cairo, Egypt",
//                "Please deliver the items as soon as possible. Thank you!",
//                items,
//                "$2400",
//                "0%",
//                "$50",
//                "$2400"
//        );
//    }
}
