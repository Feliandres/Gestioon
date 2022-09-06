import javax.swing.*;
import java.sql.ResultSet;

public class providerWindow extends JFrame{
    private JPanel providerWindow;
    private JTextField identJT;
    private JTextField nomJT;
    private JTextField telJT;
    private JButton saveButton;
    private JButton cleanButton;
    private JLabel title;
    private JButton queryButton;
    private JTextField emailJT;

    JTextField[] jTexts = {nomJT, telJT, emailJT};

    public providerWindow(int option){
        // Connection to core of SQL
        connection co = new connection();
        co.setTable("proveedor");
        co.setColumn("ident_prov");

        // Characteristics of Window
        setContentPane(providerWindow);
        setVisible(true);
        setSize(350,300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon(".\\src\\assets\\icons\\provider\\iconProviderWindow.png");
        setIconImage(image.getImage());

        // Default Title
        String titleWindow = "CREAR PROVEEDOR";
        switch (option){
            case 1:
                titleWindow = "ACTUALIZAR PROVEEDOR";
                saveButton.setText("ACTUALIZAR");
                break;
            case 2:
                titleWindow = "ELIMINAR PROVEEDOR";
                saveButton.setText("ELIMINAR");
                break;
        }
        setTitle(titleWindow);

        // No visible 'queryButton' default
        queryButton.setVisible(false);

        // Disabled some objects
        if (option == 1 | option == 2){
            queryButton.setVisible(true);
            this.disabledAll();
        }

        // Query and load data in fields
        queryButton.addActionListener(e -> {
            String ident = identJT.getText();
            if (ident.length() != 0){
                co.setData(ident.trim());
                ResultSet rS = co.qryData();
                try {
                    if (rS.next()){
                        if (option == 1){
                            for(JTextField jT : jTexts){
                                jT.setEnabled(true);
                            }
                        }
                        saveButton.setEnabled(true);
                        cleanButton.setEnabled(true);
                        identJT.setText(rS.getString("ident_prov"));
                        nomJT.setText(rS.getString("nom_prov"));
                        telJT.setText(rS.getString("tel_prov"));
                        emailJT.setText(rS.getString("ema_prov"));
                    } else {
                        JOptionPane.showMessageDialog(null,"Proveedor no encontrado", "ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception eq) {
                    System.out.println("Exception" + eq);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR",JOptionPane.ERROR_MESSAGE);
            }

        });

        // CRUD Provider
        saveButton.addActionListener(e -> {
            validations val = new validations();

            // Check if some JTextField is empty
            boolean conditionEmpty = val.validateEmptyFields(jTexts);
            if (conditionEmpty){
                String rucPRV = identJT.getText().trim();
                String namePRV = nomJT.getText().trim();
                String phonePRV = telJT.getText().trim();
                String emailPRV = emailJT.getText().trim();
                Integer code;

                // Boolean of validate fields
                boolean conditionRegex = true;

                // Validate RUC, phone, email & name
                if (! val.validateNumbers(namePRV) | val.minLengthField(namePRV, 3)){
                    JOptionPane.showMessageDialog(null, "Nombre Incorrecto", "ERROR"
                            , JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }
                if (val.validateNumbers(rucPRV) | val.lengthField(rucPRV, 13)){
                    JOptionPane.showMessageDialog(null,"RUC Incorrecto", "ERROR"
                            ,JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }
                if (val.validateNumbers(phonePRV) | val.lengthField(phonePRV, 10)){
                    JOptionPane.showMessageDialog(null,"Telefono Incorrecto"
                            , "ERROR",JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }
                if (val.validateEmail(emailPRV)){
                    JOptionPane.showMessageDialog(null, "Correo Incorrecto", "ERROR"
                            , JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }

                if (conditionRegex){
                    switch (option){
                        case 0:
                            code = co.createProvider(rucPRV, namePRV, phonePRV, emailPRV);
                            if (code.equals(0)){
                                JOptionPane.showMessageDialog(null,"Proveedor Creado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                                this.cleanAll();
                            } else{
                                JOptionPane.showMessageDialog(null,"Codigo Error: " + code, "ERROR",JOptionPane.ERROR_MESSAGE);
                                JOptionPane.showMessageDialog(null,"Proveedor no creado", "ERROR",JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 1:
                            code = co.updateProvider(rucPRV, namePRV, phonePRV, emailPRV);
                            if (code.equals(0)){
                                JOptionPane.showMessageDialog(null,"Proveedor Actualizado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                                this.cleanAll();
                                this.disabledAll();
                            } else {
                                JOptionPane.showMessageDialog(null,"Codigo Error: " + code, "ERROR",JOptionPane.ERROR_MESSAGE);
                                JOptionPane.showMessageDialog(null,"Proveedor no actualizado", "ERROR",JOptionPane.ERROR_MESSAGE);
                            }

                            break;
                        case 2:
                            co.deleteData();
                            JOptionPane.showMessageDialog(null,"Proveedor Eliminado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                            this.cleanAll();
                            this.disabledAll();
                            break;
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null, "Campos Vacios", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Clean/Disabled
        cleanButton.addActionListener(e -> {
            if (option == 1 | option == 2) {
                this.cleanAll();
                this.disabledAll();
            }
        });
    }

    // Clean all JTextFields
    private void cleanAll(){
        for(JTextField jT : jTexts){ jT.setText(""); }
        identJT.setText("");
    }

    // Disabled JTextFields and Buttons
    private void disabledAll(){
        for(JTextField jT : jTexts){ jT.setEnabled(false); }
        saveButton.setEnabled(false);
        cleanButton.setEnabled(false);
    }
}