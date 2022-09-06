
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;


public class makeSaleWindow extends JFrame{
    private JTextField dniJT;
    private JButton queryButton;
    private JTextField nameJT;
    private JTextField lastNJT;
    private JTextField addressJt;
    private JTextField emailJT;
    private JTextField phoneJT;
    private JButton printButton;
    private JPanel makeSale;
    private JLabel subTotalJL;
    private JLabel cambioJL;
    private JTextField moneyUserJT;
    private JTextField ivaJT;
    private JTextField totalJT;
    private ResultSet userData;
    private boolean conditionChange = false;
    private boolean validIVA = true;
    private boolean existClient = false;
    private final JTextField[] jTexts = {nameJT, lastNJT, addressJt, emailJT, phoneJT, moneyUserJT};


    public makeSaleWindow(double subT) {
        // Characteristics of Window
        setContentPane(makeSale);
        setTitle("Generar Venta");
        setSize(600, 350);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon(".\\src\\assets\\make_Window.png");
        setIconImage(image.getImage());
        this.changeAll(false);
        connection con = new connection();
        validations val = new validations();
        totalJT.setEnabled(false);

        // Visible data before
        subTotalJL.setText(String.valueOf(subT));
        ivaJT.setText("12.00");
        double valT = subT * (Double.parseDouble(ivaJT.getText())/100) + subT;
        totalJT.setText(String.valueOf(String.format("%.2f", valT)));

        // Buttons
        queryButton.addActionListener(e -> {
            this.cleanAll();
            String ident_user = dniJT.getText();
            if (ident_user.length() != 0){
                if(!val.lengthField(ident_user, 10) | !val.lengthField(ident_user, 13)){
                    if (!credentialsToUse.getEma_Emp().equals("DEFAULT")){
                        con.setTable("cliente");
                        con.setColumn("ident_Cli");
                        con.setData(ident_user);
                        this.userData = con.qryData();
                        try{
                            if (this.userData.next()){
                                this.existClient = true;
                                con.setData(dniJT.getText());
                                this.changeAll(true);
                                nameJT.setText(userData.getString("nom_Cli"));
                                lastNJT.setText(userData.getString("ape_Cli"));
                                addressJt.setText(userData.getString("dir_Cli"));
                                emailJT.setText(userData.getString("ema_Cli"));
                                phoneJT.setText(userData.getString("tel_Cli"));
                            } else {
                                JOptionPane.showMessageDialog(null,"Usuario no encontrado!\n Registre Ahora"
                                        , "ALERTA", JOptionPane.INFORMATION_MESSAGE);
                                this.changeAll(true);
                                emailJT.setText(credentialsToUse.getEma_Emp());
                                phoneJT.setText(credentialsToUse.getTel_Emp());
                                addressJt.setText(credentialsToUse.getDir_Emp());
                            }
                        } catch (SQLException eRD){
                            System.out.println(eRD);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,"Configure los datos de la Empresa"
                                , "ALERTA", JOptionPane.INFORMATION_MESSAGE);
                        new companyWindow();
                    }

                } else {
                    JOptionPane.showMessageDialog(null,"DNI Incorrecto", "ERROR"
                            , JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
            }
        });


        printButton.addActionListener( e -> {
            boolean conditionEmpty = val.validateEmptyFields(jTexts);

            if (conditionEmpty & dniJT.getText().length() != 0){
                String dni = dniJT.getText();
                String name = nameJT.getText();
                String lastN = lastNJT.getText();
                String address = addressJt.getText();
                String email = emailJT.getText();
                String phone = phoneJT.getText();
                int errorCode;

                boolean conditionRegex = true;
                if(val.validateNumbers(dni)){
                    if (!val.lengthField(dni, 10)){
                        if (!val.lengthField(dni,13)){
                            JOptionPane.showMessageDialog(null,"Dni Incorrecto", "ERROR",JOptionPane.ERROR_MESSAGE);
                            conditionRegex = false;
                        }
                    }

                }
                if(val.validateNumbers(phone) | val.lengthField(phone, 10)){
                    JOptionPane.showMessageDialog(null,"Telefono Incorrecto", "ERROR",JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }
                if(val.validateEmail(email)){
                    JOptionPane.showMessageDialog(null, "Correo Incorrecto", "ERROR", JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }
                if(!val.validateNumbers(name) | !val.validateNumbers(lastN) | val.minLengthField(name, 3) | val.minLengthField(lastN, 3)){
                    JOptionPane.showMessageDialog(null, "Nombres Incorrectos", "ERROR", JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }
                if (val.minLengthField(address, 5)){
                    JOptionPane.showMessageDialog(null, "Direccion invalida", "ERROR", JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }

                if (conditionRegex){
                    if (this.validIVA){
                        if (this.conditionChange){
                            if (this.existClient){errorCode = con.updateClient(dni, name, lastN, address, email, phone); }
                            else {
                                errorCode = con.createClient(dni, name, lastN, address, email, phone);
                            }
                            double iva = Double.parseDouble(ivaJT.getText());
                            double valTotal = Double.parseDouble(totalJT.getText());
                            con.updateFacture(dni, name, lastN, address, phone, iva, valTotal);

                            try{
                                ResultSet pathResult = con.getPathSale();
                                pathResult.next();
                                String pathToSave = pathResult.getString(1);
                                if (pathToSave.equals("Nothing")){
                                    JOptionPane.showMessageDialog(null,"Configurar path", "WARNING"
                                            , JOptionPane.INFORMATION_MESSAGE);
                                    new pathWindow();
                                } else{
                                    if (new File(pathToSave).exists()){
                                        if(errorCode == 0){
                                            JOptionPane.showMessageDialog(null,"Generando...", "SUCCESSFULLY"
                                                    , JOptionPane.INFORMATION_MESSAGE);
                                            dispose();
                                        } else {
                                            JOptionPane.showMessageDialog(null,"Codigo Error: " + errorCode, "ERROR"
                                                    ,JOptionPane.ERROR_MESSAGE);
                                        }
                                        new generateFac(pathToSave);
                                    } else {
                                        JOptionPane.showMessageDialog(null,"Path no valido", "ERROR"
                                                ,JOptionPane.ERROR_MESSAGE);
                                    }

                                }
                            } catch (SQLException eOp){
                                JOptionPane.showMessageDialog(null,"EASTER EGG", "EASTER EGG"
                                        , JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"Cambio no puede ser negativo", "ERROR"
                                    , JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,"IVA invalido", "ERROR"
                                , JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null,"Campos Vacios", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
            }
        });

        // Key Listeners
        moneyUserJT.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double moneyUser = 0;
                if (moneyUserJT.getText().length() != 0){
                    try{
                        moneyUser = (Double.parseDouble(moneyUserJT.getText()));
                    } catch (Exception ec){
                        moneyUser = 0;
                    }
                }
                double total = Double.parseDouble(totalJT.getText());
                double change = moneyUser - total;

                cambioJL.setText(String.valueOf(String.format("%.2f",change)));
                conditionChange = change >= 0;
                if (conditionChange){
                    cambioJL.setForeground(Color.GREEN);
                } else {
                    cambioJL.setForeground(Color.RED);
                }
            }
        });


        ivaJT.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double iva = 0;
                if(ivaJT.getText().length() != 0){
                    double subTotal = Double.parseDouble(subTotalJL.getText());
                    if (val.validateNumbers(String.valueOf(iva))){
                        iva = Double.parseDouble(ivaJT.getText()) / 100;
                        if (iva < 0 | iva >= 100){
                            validIVA = false;
                        }
                    } else {
                        validIVA = false;
                    }
                    totalJT.setText(String.valueOf(String.format("%.2f", (subTotal * iva) +subTotal)));
                }
            }
        });
    }

    // Change the accessibility of JTextsFields and 'printButton'
    private void changeAll(boolean condition){
        for (JTextField jT: jTexts){ jT.setEnabled(condition); }
        this.printButton.setEnabled(condition);
    }

    private void cleanAll(){
        for (JTextField jT: jTexts){
            jT.setText("");
        }
    }
}
