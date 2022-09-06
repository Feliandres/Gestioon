import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class productWindow extends JFrame{
    private JTextField codePrJT;
    private JTextField stockJT;
    private JTextField priceCJT;
    private JButton createButton;
    private JButton cleanProduct;
    private JPanel createPPanel;
    private JTextArea detPrJt;
    private JButton queryButton;
    private JTextField priceVJT;
    private JTextField descJT;
    private JComboBox providersCB;
    private double priceV = 0.0;

    JTextField[] jTexts = {stockJT, priceCJT, priceVJT, descJT};


    public productWindow(int option){
        // Connection to core of SQL
        connection co = new connection();

        // Characteristics of Window
        setContentPane(createPPanel);
        setVisible(true);
        setSize(550,300);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon image = new ImageIcon(".\\src\\assets\\icons\\product\\iconProductWindow.png");
        setIconImage(image.getImage());

        this.fillComboBox(co);
        validations val = new validations();

        co.setTable("producto");
        co.setColumn("cod_Pro");

        // Default Title
        String titleWindow = "CREAR PRODUCTO";
        // Show more characteristics of Window
        switch (option){
            case 1:
                titleWindow = "ACTUALIZAR PRODUCTO";
                createButton.setText("ACTUALIZAR");
                break;
            case 2:
                titleWindow = "ELIMINAR PRODUCTO";
                createButton.setText("ELIMINAR");
                break;
        }
        setTitle(titleWindow);

        // Occult buttons with option choose
        queryButton.setVisible(false);
        if (option == 1 | option == 2){
            queryButton.setVisible(true);
            this.disabledAll();
        } else {
            descJT.setText("0.0");
        }

        // Query button and load data into JTexts
        queryButton.addActionListener(e -> {
            String code = codePrJT.getText();
            if (code.length() != 0){
                co.setData(code);
                ResultSet rs = co.qryData();
                try{
                    if(rs.next()){
                        if (option == 1){
                            for (JTextField jT : jTexts){
                                jT.setEnabled(true);
                                detPrJt.setEnabled(true);
                                providersCB.setEnabled(true);
                            }
                        }
                        createButton.setEnabled(true);
                        cleanProduct.setEnabled(true);
                        detPrJt.setText(rs.getString("det_Pro"));
                        priceCJT.setText(rs.getString("preUni_Pro"));
                        priceVJT.setText(rs.getString("preVen_Pro"));
                        descJT.setText(rs.getString("desc_Pro"));
                        stockJT.setText(String.valueOf(rs.getInt("sto_Pro")));
                    } else {
                        JOptionPane.showMessageDialog(null,"Producto no encontrado", "ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception eqry){
                    JOptionPane.showMessageDialog(null,"Fallo: Cargar Datos", "ERROR",JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR",JOptionPane.ERROR_MESSAGE);
            }
        });
        // Get data of all JTextField to prepare any Sql Statement
        createButton.addActionListener(e -> {
            int stock = 0;
            double priceC = 0.0;
            double priceV = 0.0;
            double desc = 0.0;
            boolean conditionEmpty = val.validateEmptyFields(jTexts);
            if (detPrJt.getText().length() == 0){
                conditionEmpty = false;
            }

            if (conditionEmpty){
                boolean validateNumbers = false;
                try{
                    stock = Integer.parseInt(stockJT.getText());
                    priceC = Double.parseDouble(priceCJT.getText());
                    priceV = Double.parseDouble(priceVJT.getText());
                    desc = Double.parseDouble(descJT.getText());
                    if (stock > 0 & priceC > 0 & priceV > 0 & (desc >= 0 | desc <100) & (priceC <= priceV)){
                        validateNumbers = true;
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Datos incorrectos", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                if(validateNumbers){
                    String codeP = codePrJT.getText().trim();
                    String descriptionP = detPrJt.getText().trim();
                    String FKprov = co.rucProvider((String) providersCB.getSelectedItem());
                    Integer code;
                    switch (option){
                        case 0:
                            code = co.createProduct(codeP, descriptionP, priceC, priceV, stock, desc, FKprov);
                            if(code.equals(0)){
                                JOptionPane.showMessageDialog(null,"Producto Creado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                                this.cleanAll();
                            } else {
                                JOptionPane.showMessageDialog(null,"Codigo error: " + code, "ERROR",JOptionPane.ERROR_MESSAGE);
                                JOptionPane.showMessageDialog(null,"Producto no creado", "ERROR",JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 1:
                            code = co.updateProduct(codeP, descriptionP, priceC, priceV, stock, desc, FKprov);
                            if (code.equals(0)) {
                                JOptionPane.showMessageDialog(null,"Producto Actualizado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                                this.cleanAll();
                                this.disabledAll();
                            } else {
                                JOptionPane.showMessageDialog(null,"Codigo error: " + code, "ERROR",JOptionPane.ERROR_MESSAGE);
                                JOptionPane.showMessageDialog(null,"Producto no actualizado", "ERROR",JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 2:
                            co.deleteData();
                            JOptionPane.showMessageDialog(null,"Producto Eliminado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                            this.cleanAll();
                            this.disabledAll();
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Datos incorrectos", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Campos Vacios", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Clean/Disabled
        cleanProduct.addActionListener(e -> {
            if (option == 1 | option == 2) { this.disabledAll(); }
            this.cleanAll();
        });

        descJT.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try{
                    double discount = Double.parseDouble(descJT.getText());
                    double priceC = Double.parseDouble(priceCJT.getText());
                    if (discount != 0 & priceV != 0){
                        double priceVD = (priceV * (100 - discount)/100);
                        priceVJT.setText(String.valueOf(String.format("%.2f",priceVD)));
                        if (priceVD > priceC){
                            priceVJT.setForeground(Color.GREEN);
                        } else {
                            priceVJT.setForeground(Color.RED);
                        }
                    }
                }  catch (Exception erT){
                    priceVJT.setText(String.valueOf(priceV));
                }
            }
        });

        priceVJT.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                priceVJT.setForeground(Color.BLACK);
                descJT.setText("0.0");
                try{
                    priceV = Double.parseDouble(priceVJT.getText());
                } catch (Exception ePd){
                    priceV = 0.0;
                }
            }
        });
    }

    // Clean JTextFields
    public void cleanAll(){
        for (JTextField jT : jTexts){
            jT.setText("");
        }
        detPrJt.setText("");
        codePrJT.setText("");
    }

    // Disables JTextFields & Buttons
    public void disabledAll(){
        cleanProduct.setEnabled(false);
        createButton.setEnabled(false);
        for (JTextField jT : jTexts){
            jT.setEnabled(false);
        }
        detPrJt.setEnabled(false);
        providersCB.setEnabled(false);
    }

    // Load ComboBox
    public void fillComboBox(connection co){
        try{
            co.setTable("proveedor");
            co.setColumn("nom_prov");
            co.setData("");
            String nomProv;
            ResultSet rS = co.qryLikeData();
            while(rS.next()){
                nomProv = rS.getString(2);
                providersCB.addItem(nomProv);
            }
        } catch (SQLException eRr){
            JOptionPane.showMessageDialog(null,"No existen proveedores", "WARNING",JOptionPane.INFORMATION_MESSAGE);
        }

    }
}