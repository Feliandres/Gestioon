import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class allReportWindow {
    public allReportWindow(String pathToSave) {
        Document documentReportAllInventory = new Document(PageSize.A4, 5, 5, 10, 10);
        try{
            Image image = Image.getInstance(".\\src\\assets\\logo_large.png");
            image.setAlignment(1);
            Calendar d = new GregorianCalendar();
            String ingReport = String.valueOf(d.get(Calendar.YEAR)) + '-' + (d.get(Calendar.MONTH) + 1) + '-'
                    + d.get(Calendar.DAY_OF_MONTH);
            int second = d.get(Calendar.SECOND);

            connection con = new connection();
            String pdfName = credentialsToUse.getNom_Usu().substring(0,3) + ingReport + second + ".pdf";
            PdfWriter.getInstance(documentReportAllInventory, Files.newOutputStream(Paths.get(pathToSave + pdfName)));
            documentReportAllInventory.open();

            documentReportAllInventory.add(image);
            Paragraph titleReport = new Paragraph("\nINVENTARIO\n\n");
            titleReport.setAlignment(1);
            documentReportAllInventory.add(titleReport);

            Paragraph headerReport = new Paragraph("Usuario: " + credentialsToUse.getNom_Usu() + "      CI: "
                    + credentialsToUse.getFKident_Usu() + "      Fecha: " + ingReport + "\n\n\n");
            headerReport.setAlignment(1);
            documentReportAllInventory.add(headerReport);

            PdfPTable table = new PdfPTable(7);
            table.setTotalWidth(900);
            table.setWidths(new float[]{0.41f, 0.98f, 0.23f, 0.26f, 0.27f, 0.22f, 0.64f});

            Paragraph pUni = new Paragraph();
            pUni.setFont(FontFactory.getFont("Calibri", 11, Font.BOLD, new BaseColor(188,19,19)));
            pUni.add("P. Uni.");

            Paragraph pVen = new Paragraph();
            pVen.setFont(FontFactory.getFont("Calibri", 11, Font.BOLD, new BaseColor(78,162,8)));
            pVen.add("P. Ven.");

            Paragraph dto = new Paragraph();
            dto.setFont(FontFactory.getFont("Calibri", 11, Font.BOLD, new BaseColor(47,91,207)));
            dto.add("Dto.");

            Paragraph cod = new Paragraph();
            cod.setFont(FontFactory.getFont("Calibri", 11, Font.BOLD));
            cod.add("Cod.");
            table.addCell(cod);

            Paragraph det = new Paragraph();
            det.setFont(FontFactory.getFont("Calibri", 11, Font.BOLD));
            det.add("Detalle");
            table.addCell(det);

            Paragraph stock = new Paragraph();
            stock.setFont(FontFactory.getFont("Calibri", 11, Font.BOLD));
            stock.add("Stock");
            table.addCell(stock);

            table.addCell(pUni);
            table.addCell(pVen);
            table.addCell(dto);

            Paragraph prov = new Paragraph();
            prov.setFont(FontFactory.getFont("Calibri", 11, Font.BOLD));
            prov.add("Proveedor");
            table.addCell(prov);

            try {
                ResultSet allProducts = con.qryAllInventory();
                while (allProducts.next()) {
                    table.addCell(allProducts.getString(1));
                    table.addCell(allProducts.getString(2));
                    table.addCell(allProducts.getString(5));

                    Paragraph priceU = new Paragraph();
                    priceU.setFont(FontFactory.getFont("Calibri", 11, new BaseColor(188,19,19)));
                    priceU.add(allProducts.getString(3));
                    table.addCell(priceU);

                    Paragraph priceV = new Paragraph();
                    priceV.setFont(FontFactory.getFont("Calibri", 11,new BaseColor(78,162,8)));
                    priceV.add(allProducts.getString(4));
                    table.addCell(priceV);

                    Paragraph discount = new Paragraph();
                    discount.setFont(FontFactory.getFont("Calibri", 11, new BaseColor(47,91,207)));
                    discount.add(allProducts.getString(6));
                    table.addCell(discount);
                    table.addCell(allProducts.getString(7));
                }
                documentReportAllInventory.add(table);
            } catch (SQLException eAr) {
                System.out.println("ERROR TO GENERATE REPORT");
            }
        } catch (MalformedURLException eMu){
            System.out.println(eMu);
        } catch (DocumentException | IOException ex){
            throw new RuntimeException(ex);
        } finally {
            documentReportAllInventory.close();
        }
    }
}
