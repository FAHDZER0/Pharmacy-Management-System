package Config;

import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
            PDImageXObject pdImage = PDImageXObject.createFromFile("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\resources\\Images\\loginright2.png", document);
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
            contentStream.showText("Discount Amount:");
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
            document.save("Pharmacy-Management-System/PDFs/Sales Invoices/" + invoiceNumber + ".pdf");
            tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
            AnimationType type = AnimationType.POPUP;
            tray.setAnimationType(type);
            tray.setTitle("Success");
            tray.setMessage("PDF created successfully");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printTableIntoPDF(ObservableList<Map<String, Object>> dataList , Boolean Landascape) {
        try (PDDocument document = new PDDocument()) {

            PDPage page ;

            if (Landascape){
                page = new PDPage(new PDRectangle(1200, 612)); // Standard A4 size
            }else{
                page = new PDPage(PDRectangle.A4);
            }

            document.addPage(page);



            // Load the font
            PDType0Font font = PDType0Font.load(document, new File("F:\\Pharmacy Backup\\Pharmacy-Management-System\\Lib\\alfont_com_arial-1.ttf"));

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add header image
            PDImageXObject pdImage = PDImageXObject.createFromFile("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\resources\\Images\\loginright2.png", document);
            contentStream.drawImage(pdImage, 470, 770, 50, 50); // Adjust as per your image size

            // Define the table structure
            float margin = 50;
            float imageheight = 50;
            float yStart = page.getMediaBox().getHeight() - margin - imageheight;
            float yPosition = yStart;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

            // Define the space between rows
            float rowSpacing = 20;
            // Define the number of columns (assuming all maps have the same keys)
            int numberOfColumns = !dataList.isEmpty() ? dataList.get(0).keySet().size() : 0;
            float columnWidth = tableWidth / numberOfColumns;

            // Write table column headers
            contentStream.setFont(font, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);

            // Assuming that all maps in the dataList have the same keySet
            if (!dataList.isEmpty()) {
                for (String column : dataList.get(0).keySet()) {
                    contentStream.showText(String.format("%-20s", column));
                }
            }
            contentStream.endText();

            // Write table rows
            contentStream.setFont(font, 12);
            for (Map<String, Object> data : dataList) {
                yPosition -= rowSpacing;
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                for (Object value : data.values()) {
                    String text = value != null ? value.toString() : "";
                    text = text.replace("\n", " "); // Replace newline characters with spaces
                    contentStream.showText(text + "    ");
                }
                contentStream.endText();
            }

            contentStream.close();

            // Generate a unique filename by appending a timestamp
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            String fileName = "table_" + dtf.format(now);

            document.save("Pharmacy-Management-System/PDFs/tables/" + fileName + ".pdf");
            System.out.println("PDF created successfully");

            TrayNotification tray = new TrayNotification();
            tray.setAnimationType(AnimationType.POPUP);
            tray.setTitle("Success");
            tray.setMessage("PDF created successfully");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processText(String text) {
        try {
            ArabicShaping arabicShaping = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
            text = arabicShaping.shape(text);
            Bidi bidi = new Bidi(text, Bidi.LTR);
            text = bidi.writeReordered(Bidi.DO_MIRRORING);
        } catch (ArabicShapingException e) {
            e.printStackTrace();
        }
        return text;
    }

    public void printSupplierInfo(ObservableList<Map<String, Object>> dataList) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Load the font
            PDType0Font font = PDType0Font.load(document, new File("F:\\Pharmacy Backup\\Pharmacy-Management-System\\Lib\\alfont_com_arial-1.ttf"));

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add header image
            PDImageXObject pdImage = PDImageXObject.createFromFile("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\resources\\Images\\loginright2.png", document);
            contentStream.drawImage(pdImage, 470, 770, 50, 50); // Adjust as per your image size

            // Define the table structure
            float margin = 50;
            float imageheight = 50;
            float yStart = page.getMediaBox().getHeight() - margin - imageheight;
            float yPosition = yStart;

            // Define the space between rows
            float rowSpacing = 20;

            // Write data vertically
            contentStream.setFont(font, 12);
            for (Map<String, Object> data : dataList) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    String text = entry.getKey() + ": " + (entry.getValue() != null ? entry.getValue().toString() : "");
                    text = processText(text);
                    text = text.replace("\n", " "); // Replace newline characters with spaces
                    contentStream.showText(text);
                    contentStream.endText();
                    yPosition -= rowSpacing;
                }
                yPosition -= rowSpacing; // Extra space between different data entries
            }

            contentStream.close();

            // Generate a unique filename by appending a timestamp
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            String fileName = "supplier_info_" + dtf.format(now);

            document.save("Pharmacy-Management-System/PDFs/Supplier Info/" + fileName + ".pdf");
            System.out.println("PDF created successfully");

            TrayNotification tray = new TrayNotification();
            tray.setAnimationType(AnimationType.POPUP);
            tray.setTitle("Success");
            tray.setMessage("PDF created successfully");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
