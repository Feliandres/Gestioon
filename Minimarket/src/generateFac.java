import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class generateFac {
    public generateFac(String path){
        Document document = new Document();

        try {
            // Connection to SQL Core
            connection co = new connection();
            co.setTable("cabfactura");
            co.setColumn("num_CF");
            co.setData(String.valueOf(connection.getNum_CF()));

            // Build the header of data client
            double valT = 0.0;
            double iva = 0.0;
            double subT = 0.0;
            String pdfName = "";
            StringBuilder dataUser = new StringBuilder("DNI: ");
            try{
                ResultSet dataClient = co.qryData();
                if(dataClient.next()){
                    String dniClient = dataClient.getString("cident_Cli");
                    String nomClient = dataClient.getString("nomCli_CF");
                    String apeClient = dataClient.getString("apeCli_CF");
                    String addrClient = dataClient.getString("dirCli_CF");
                    String telClient = dataClient.getString("telCli_CF");
                    String date = dataClient.getString("fecha_CF");
                    dataUser.append(dniClient).append("\nNombre: ").append(nomClient).append(" ").append(apeClient);
                    dataUser.append("\nDireccion: ").append(addrClient).append("\nTelefono: ").append(telClient)
                            .append("\nFecha: ").append(date).append("\n\n");

                    pdfName = nomClient.substring(0,2) + apeClient.substring(0,2) + "_" + date + "-" + connection.getNum_CF() +".pdf";
                }
                valT = dataClient.getDouble("valT_CF");
                iva = dataClient.getDouble("iva_CF");
                subT = dataClient.getDouble("subT_CF");
            } catch (SQLException eDu){
                System.out.println(eDu);
            }


            // References to image
            Image imagen = Image.getInstance(".\\src\\assets\\logos\\logoMinimarket.png");

            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(path + pdfName)));
            document.open();

            // Add: Logo
            document.add(imagen);

            // Header: Characteristics about Pharmacy
            Paragraph header = new Paragraph("FACTURA\n"  + "RUC: "+ credentialsToUse.getFkruc_Emp() +  "\n"
                    + "DIRECCION: " + credentialsToUse.getDir_Emp() + "\n" + "CORREO: " + credentialsToUse.getEma_Emp()
                    + "\nTELEFONO: " + credentialsToUse.getTel_Emp() + "\n\n");

            header.setAlignment(1);
            document.add(header);

            // Header: Data of Client
            Paragraph dataClientH = new Paragraph(String.valueOf(dataUser));
            document.add(dataClientH);

            // Add: Column names of table 'details'
            PdfPTable table = new PdfPTable(7);

            // Widths of cells
            table.setWidths( new float[]{0.15f, 0.35f, 0.99f, 0.37f, 0.30f,0.30f, 0.45f});

            table.addCell("N#");
            table.addCell("Codigo");
            table.addCell("Detalle");
            table.addCell("Cantidad");
            table.addCell("PvP");
            table.addCell("Dto.");
            table.addCell("SubTotal");

            // Send data to prepare query about all details
            co.setTable("detalle");
            co.setColumn("FKnum_CF");
            co.setData(String.valueOf(connection.getNum_CF()));


            ResultSet rS = co.qryData();
            try{
                int index = 1;
                while(rS.next()){
                    table.addCell(String.valueOf(index));
                    table.addCell(rS.getString("FKcod_pro"));
                    table.addCell(rS.getString("detPro_Det"));
                    table.addCell(rS.getString("cantPro_Det"));
                    table.addCell(rS.getString("pUPro"));
                    table.addCell(rS.getString("descPro"));
                    table.addCell(rS.getString("valto_det"));
                    index++;
                }
                document.add(table);
                Paragraph footer = new Paragraph("\nSubTotal: " + String.format("%.2f",subT));
                Paragraph footer2 = new Paragraph("\nIVA 12%: " + String.format("%.2f", (iva/100) * subT));
                Paragraph footer3 = new Paragraph("\nTotal: " + String.format("%.2f", valT));
                footer.setAlignment(2);
                footer2.setAlignment(2);
                footer3.setAlignment(2);
                document.add(footer);
                document.add(footer2);
                document.add(footer3);
            } catch (SQLException e){
                System.out.println(e);
            }
        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (DocumentException | IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            document.close();
        }
    }
}