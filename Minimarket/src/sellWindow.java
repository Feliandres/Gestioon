import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class sellWindow extends JFrame{
    private JPanel sellW;
    private JTable sellsJT;
    private JButton addButton;
    private JButton sellButton;
    private JLabel currentPriceJL;
    private JTabbedPane addDetail;
    private JTextField codeJT;
    private JTextArea descJT;
    private JTextField priceJT;
    private JTextField discJT;
    private JSpinner quantityS;
    private JButton queryButton;
    private JButton cleanButton;
    private JTextField codeDe;
    private JTextArea descDe;
    private JTextField priceDe;
    private JTextField discDe;
    private JSpinner quantityDe;
    private JButton deleteButton;
    private JButton cleanButtonDe;
    private JButton searchButtonDe;
    private boolean isExists = false;

    public sellWindow(){
        // Characteristics of Window
        connection co = new connection();
        setContentPane(sellW);
        setSize(530, 550);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Factura");
        setResizable(false);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon(".\\src\\assets\\icons\\sell\\iconWindowSell.png");
        setIconImage(image.getImage());


        // Connect to core SQL
        connection con = new connection();

        // Create 'cab_notevent'
        con.createCab();

        // Get 'valT' of current 'cabfactura'
        AtomicReference<Double> subT_CF = new AtomicReference<>(0.0);

        this.disabledAll();
        this.disabledAllDelete();
        AtomicInteger stockLast = new AtomicInteger();
        AtomicInteger stockToRollback = new AtomicInteger();

        queryButton.addActionListener(e -> {
            if (codeJT.getText().length() != 0){
                co.setData(codeJT.getText());
                try {
                    ResultSet existDetail = co.searchInDetails(codeJT.getText());
                    if (existDetail.next()){
                        quantityS.setEnabled(true);
                        addButton.setEnabled(true);
                        cleanButton.setEnabled(true);
                        descJT.setText(existDetail.getString(5));
                        priceJT.setText(existDetail.getString(7));
                        discJT.setText(existDetail.getString(8));
                        quantityS.setValue(existDetail.getInt(6));
                        stockLast.set((Integer) quantityS.getValue());
                        discJT.setEnabled(true);
                        JOptionPane.showMessageDialog(null,"Actualize el producto", "ACTUALIZAR"
                                , JOptionPane.INFORMATION_MESSAGE);
                        this.isExists = true;
                    } else {
                        ResultSet rS = co.searchProduct();
                        try{
                            if (rS.next()){
                                quantityS.setEnabled(true);
                                addButton.setEnabled(true);
                                cleanButton.setEnabled(true);
                                descJT.setText(rS.getString(1));
                                priceJT.setText(rS.getString(2));
                                discJT.setText("0.0");
                                discJT.setEnabled(true);
                            } else {
                                JOptionPane.showMessageDialog(null,"Producto no encontrado", "ERROR"
                                        , JOptionPane.ERROR_MESSAGE);
                            }
                        }catch (Exception er){
                            System.out.println(er);
                        }
                        this.isExists = false;
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
            }
        });

        addButton.addActionListener(e -> {
            String[] columnsFact = {"Codigo", "Detalle", "Cantidad", "PvP", "Descuento", "SubTotal"};
            DefaultTableModel t = new DefaultTableModel();
            for (String cN: columnsFact){
                t.addColumn(cN);
            }

            this.sellsJT.setModel(t);
            t.addRow(columnsFact);
            this.sellsJT.setModel(t);


            String cod_pro = codeJT.getText();
            String descri_pro = descJT.getText();
            double discountD = 0.0;

            int quantityToBuy = (int) quantityS.getValue();
            int currentStock = co.stockProduct(cod_pro);

            boolean conditionCreate = true;
            String discount = discJT.getText();
            double price = Double.parseDouble(priceJT.getText());

            if (discount.length() == 0 | codeJT.getText().length() == 0){
                JOptionPane.showMessageDialog(null,"Campos Vacios", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
                conditionCreate = false;
            }

            try{
                discountD = Double.parseDouble(discount);
            } catch (Exception er){
                JOptionPane.showMessageDialog(null,"Descuento no valido", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
                conditionCreate = false;

            }

            int stockToUpdate = (int) quantityS.getValue();

            if (this.isExists){
                int difference = (stockToUpdate - stockLast.get());
                if (difference > 0){
                    if (difference <= currentStock){
                        con.updateDetail(stockToUpdate, price, discountD, cod_pro);
                        int toSaveStock = currentStock - difference;
                        con.updateProductDetail(cod_pro, toSaveStock);
                        JOptionPane.showMessageDialog(null,"Actualizado con exito", "Exito"
                                , JOptionPane.INFORMATION_MESSAGE);
                        this.cleanAll();
                    } else {
                        JOptionPane.showMessageDialog(null,"Fuera de Stock", "ERROR"
                                , JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    con.updateDetail(stockToUpdate, price, discountD, cod_pro);
                    int toSaveStock = currentStock + Math.abs(difference);
                    con.updateProductDetail(cod_pro, toSaveStock);
                    JOptionPane.showMessageDialog(null,"Actualizado con exito", "Exito"
                            , JOptionPane.INFORMATION_MESSAGE);
                    this.cleanAll();
                }
            } else {
                try {
                    discountD = Double.parseDouble(discount)/100;
                    if (discountD < 0.0 | quantityToBuy <= 0){
                        conditionCreate = false;
                        JOptionPane.showMessageDialog(null,"Cantidad o Descuento no validos", "ERROR"
                                , JOptionPane.ERROR_MESSAGE);
                    }
                    if ((currentStock - quantityToBuy) < 0){
                        JOptionPane.showMessageDialog(null,"Fuera de Stock", "ERROR"
                                , JOptionPane.ERROR_MESSAGE);
                        conditionCreate = false;
                    }
                } catch (Exception ePd){
                    JOptionPane.showMessageDialog(null,"Descuento no Valido", "ERROR"
                            , JOptionPane.ERROR_MESSAGE);
                    conditionCreate = false;
                }
                if (conditionCreate){
                    double subPrice = (double) Math.round(price * quantityToBuy * 100)/ 100;
                    double totalPrice = (discountD > 0.0) ? (double) Math.round((subPrice - (subPrice*discountD))*100)/100 : subPrice;
                    co.createDet(cod_pro, descri_pro, quantityToBuy, price, discountD, totalPrice);
                    JOptionPane.showMessageDialog(null,"Agregado con Exito", "EXITO"
                            , JOptionPane.INFORMATION_MESSAGE);
                    this.cleanAll();
                }
            }
            ResultSet dataDet =  co.readDet();
            try{
                String[] dataToTable = new String[columnsFact.length];
                while (dataDet.next()){
                    int index = 0;
                    dataToTable[index] = dataDet.getString("FKcod_pro");
                    index ++;
                    dataToTable[index] = dataDet.getString("detPro_Det");
                    index ++;
                    dataToTable[index] = dataDet.getString("cantPro_Det");
                    index ++;
                    dataToTable[index] = dataDet.getString("pUPro");
                    index ++;
                    dataToTable[index] = dataDet.getString("descPro");
                    index ++;
                    dataToTable[index] = dataDet.getString("valto_det");
                    t.addRow(dataToTable);
                    this.sellsJT.setModel(t);
                    subT_CF.set(co.currentPrice());
                    currentPriceJL.setText(String.valueOf(String.format("%.2f",subT_CF.get())));
                }
            } catch (SQLException eRD){
                System.out.println("CANT READ DATA");
            }
        });

        cleanButton.addActionListener(e -> this.cleanAll());

        searchButtonDe.addActionListener(e -> {
            String codeDed = codeDe.getText();
            if (codeDed.length() != 0){
                try{
                    ResultSet existDetail = co.searchInDetails(codeDed);
                    if (existDetail.next()){
                        deleteButton.setEnabled(true);
                        cleanButtonDe.setEnabled(true);
                        descDe.setText(existDetail.getString(5));
                        priceDe.setText(existDetail.getString(7));
                        discDe.setText("0.0");
                        quantityDe.setValue(existDetail.getInt(6));
                        stockToRollback.set((Integer) quantityDe.getValue());
                    } else {
                        JOptionPane.showMessageDialog(null,"Producto no encontrado", "ERROR"
                                , JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String codeDeToDel = codeDe.getText();
            if (codeDeToDel.length() != 0){
                int errorCode = con.deleteDetail(codeDeToDel);
                int currentStock = con.stockProduct(codeDeToDel);
                if (errorCode == 0){
                    con.updateProductDetail(codeDeToDel, currentStock + stockToRollback.get());
                    JOptionPane.showMessageDialog(null,"Detalle Eliminado", "EXITO"
                            , JOptionPane.INFORMATION_MESSAGE);

                    subT_CF.set(co.currentPrice());
                    currentPriceJL.setText(String.valueOf(String.format("%.2f",subT_CF.get())));
                    this.cleanAllDelete();
                } else {
                    JOptionPane.showMessageDialog(null,"Codigo Error: " + errorCode, "ERROR"
                            , JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
            }

            ResultSet dataDet =  co.readDet();
            try{
                String[] columnsFact = {"Codigo", "Detalle", "Cantidad", "PvP", "Descuento", "SubTotal"};
                DefaultTableModel t = new DefaultTableModel();
                for (String cN: columnsFact){
                    t.addColumn(cN);
                }

                this.sellsJT.setModel(t);
                t.addRow(columnsFact);
                this.sellsJT.setModel(t);

                String[] dataToTable = new String[columnsFact.length];
                while (dataDet.next()){
                    int index = 0;
                    dataToTable[index] = dataDet.getString("FKcod_pro");
                    index ++;
                    dataToTable[index] = dataDet.getString("detPro_Det");
                    index ++;
                    dataToTable[index] = dataDet.getString("cantPro_Det");
                    index ++;
                    dataToTable[index] = dataDet.getString("pUPro");
                    index ++;
                    dataToTable[index] = dataDet.getString("descPro");
                    index ++;
                    dataToTable[index] = dataDet.getString("valto_det");
                    t.addRow(dataToTable);
                    this.sellsJT.setModel(t);
                }
            } catch (SQLException eRD){
                System.out.println("CANT READ DATA");
            }

        });

        cleanButtonDe.addActionListener(e -> this.cleanAllDelete());
        // Button to make sell
        sellButton.addActionListener(e -> {
            if (co.numberDetails() != 0){
                subT_CF.set(co.currentPrice());
                new makeSaleWindow(subT_CF.get());
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "No existen detalles", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // First Tab
    public void disabledAll(){
        priceJT.setEnabled(false);
        descJT.setEnabled(false);
        quantityS.setEnabled(false);
        addButton.setEnabled(false);
        cleanButton.setEnabled(false);
        discJT.setEnabled(false);
    }
    public void cleanAll(){
        codeJT.setText("");
        this.disabledAll();
        priceJT.setText("");
        descJT.setText("");
        quantityS.setValue(0);
        discJT.setText("");
    }


    // Second Tab
    public void disabledAllDelete(){
        discDe.setEnabled(false);
        quantityDe.setEnabled(false);
        deleteButton.setEnabled(false);
        cleanButtonDe.setEnabled(false);
        descDe.setEnabled(false);
        priceDe.setEnabled(false);
    }
    public void cleanAllDelete(){
        codeDe.setText("");
        discDe.setText("");
        quantityDe.setValue(0);
        priceDe.setText("");
        descDe.setText("");
        this.disabledAllDelete();
    }

}
