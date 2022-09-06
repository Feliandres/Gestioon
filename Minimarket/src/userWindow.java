import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class userWindow extends JFrame{
    private JPanel employeWindow;
    private JTextField dniJT;
    private JTextField nameJT;
    private JTextField lastNameJT;
    private JTextField phoneJT;
    private JTextField emailJT;
    private JTextField userJT;
    private JButton cleanButton;
    private JButton createButton;
    private JButton queryButton;
    private JTextField dateIngJT;
    private JPasswordField passwordJP;
    private final connection co = new connection();
    private JComboBox rolesCB;

    private final int optionC;
    JTextField[] jTexts = {dniJT, nameJT, lastNameJT, phoneJT, emailJT, userJT, passwordJP};

    public userWindow(int option){
        this.optionC = option;

        // Characteristics of Window
        setContentPane(employeWindow);
        setVisible(true);
        setSize(550,400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon image = new ImageIcon(".\\src\\assets\\employee_Window.png");
        setIconImage(image.getImage());

        // Fill ComboBox with all roles
        this.fillRolesCB();

        // Set default data to UPDATE & DELETE
        co.setTable("usuario");
        co.setColumn("ident_Usu");

        // Default Title
        String titleWindow = "CREAR USUARIO";

        // Show more characteristics of Window
        switch (option){
            case 1:
                titleWindow = "ACTUALIZAR USUARIO";
                createButton.setText("ACTUALIZAR");
                break;
            case 2:
                titleWindow = "ELIMINAR USUARIO";
                createButton.setText("ELIMINAR");
                break;
        }

        setTitle(titleWindow);

        // Occult buttons depend on the option choose
        queryButton.setVisible(false);
        dateIngJT.setEnabled(false);

        if (option == 1 | option == 2){
            queryButton.setVisible(true);
            dateIngJT.setEnabled(true);
            this.disabledAll();
        } else {
            // Date Default
            Calendar d = new GregorianCalendar();
            String date = String.valueOf(d.get(Calendar.YEAR)) + '-' + (d.get(Calendar.MONTH) + 1) + '-'
                    + d.get(Calendar.DAY_OF_MONTH);
            dateIngJT.setText(date);
        }

        // Complement query of CRUD
        queryButton.addActionListener(e ->{
            String dni = dniJT.getText().trim();
            if (dni.length() != 0){
                co.setData(dni);
                ResultSet rs = co.qryData();
                try{
                    if(rs.next()){
                        // Enabled JTextsFields to Update User
                        if (option == 1){
                            for (JTextField jT : jTexts){
                                jT.setEnabled(true);
                            }
                            rolesCB.setEnabled(true);
                        }

                        // Enabled buttons if ser is found
                        createButton.setEnabled(true);
                        cleanButton.setEnabled(true);

                        // Load data of BDD in JTextFields
                        nameJT.setText(rs.getString("nom_Usu"));
                        lastNameJT.setText(rs.getString("ape_Usu"));
                        phoneJT.setText(rs.getString("tel_Usu"));
                        dateIngJT.setText(rs.getString("ing_Usu"));
                        emailJT.setText(rs.getString("ema_Usu"));
                        userJT.setText(rs.getString("usuN_Usu"));
                        passwordJP.setText(rs.getString("pass_Usu"));
                    } else {
                        JOptionPane.showMessageDialog(null,"Usuario no encontrado", "ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception eqry){
                    System.out.println("[-] ERROR, load data into JTextFields Failed !");
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR",JOptionPane.ERROR_MESSAGE);
            }

        });
        // CRUD of User
        createButton.addActionListener(e ->{
            // Class to validate fields
            validations val = new validations();

            // Check if some JTextField is empty
            boolean conditionEmpty = val.validateEmptyFields(jTexts);

            if (conditionEmpty){
                String dni_Usu = dniJT.getText().trim();
                String name_Usu = nameJT.getText().trim();
                String lastN_Usu = lastNameJT.getText().trim();
                String phone_Usu = phoneJT.getText().trim();
                String ema_Usu = emailJT.getText().trim();
                String user_Usu = userJT.getText().trim();
                String pass_Usu = String.valueOf(passwordJP.getPassword()).trim();
                String Fkrol = (String) rolesCB.getSelectedItem();
                Integer code;

                // Boolean of validate fields
                boolean conditionRegex = true;

                // Validate DNI, phone, email, name & last name
                if(val.validateNumbers(dni_Usu) | val.lengthField(dni_Usu, 10)){
                        JOptionPane.showMessageDialog(null,"Dni Incorrecto", "ERROR",JOptionPane.ERROR_MESSAGE);
                        conditionRegex = false;
                }
                if(val.validateNumbers(phone_Usu) | val.lengthField(phone_Usu, 10)){
                        JOptionPane.showMessageDialog(null,"Telefono Incorrecto", "ERROR",JOptionPane.ERROR_MESSAGE);
                        conditionRegex = false;
                }
                if(val.validateEmail(ema_Usu)){
                        JOptionPane.showMessageDialog(null, "Correo Incorrecto", "ERROR", JOptionPane.ERROR_MESSAGE);
                        conditionRegex = false;
                }
                if(!val.validateNumbers(name_Usu) | !val.validateNumbers(lastN_Usu) | val.minLengthField(name_Usu, 3) | val.minLengthField(lastN_Usu, 3)){
                    JOptionPane.showMessageDialog(null, "Nombres Incorrectos", "ERROR", JOptionPane.ERROR_MESSAGE);
                    conditionRegex = false;
                }

                if(conditionRegex){
                    switch (option){
                        case 0:
                            code = co.createUser(dni_Usu, name_Usu, lastN_Usu, phone_Usu, ema_Usu, Fkrol, user_Usu, pass_Usu);
                            if(code.equals(0)){
                                JOptionPane.showMessageDialog(null,"Usuario Creado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                                this.cleanAll();
                            } else {
                                JOptionPane.showMessageDialog(null,"Codigo Error: " + code, "ERROR",JOptionPane.ERROR_MESSAGE);
                                JOptionPane.showMessageDialog(null,"Usuario no creado", "ERROR",JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 1:
                            String dateEmp = dateIngJT.getText();
                            code = co.updateUser(dni_Usu, name_Usu, lastN_Usu, dateEmp, phone_Usu, ema_Usu, Fkrol, user_Usu, pass_Usu);
                            if (code.equals(0)) {
                                JOptionPane.showMessageDialog(null,"Usuario Actualizado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                                this.cleanAll();
                                this.disabledAll();
                            } else {
                                JOptionPane.showMessageDialog(null,"Codigo Error: " + code, "ERROR",JOptionPane.ERROR_MESSAGE);
                                JOptionPane.showMessageDialog(null,"Usuario no actualizado", "ERROR",JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 2:
                            co.deleteData();
                            JOptionPane.showMessageDialog(null,"Usuario Eliminado", "Exito",JOptionPane.INFORMATION_MESSAGE);
                            this.cleanAll();
                            this.disabledAll();
                            break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Campos Vacios", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Clean JTextsFields and disabled in case of update and delete
        cleanButton.addActionListener(e -> {
            if (option == 1 | option == 2){
                this.disabledAll();
                dateIngJT.setText("");
            }
            this.cleanAll();
        });
    }
    private void fillRolesCB(){
        try{
            co.setTable("rol");
            co.setColumn("tipo_rol");
            co.setData("");
            String rol;
            ResultSet rS = co.qryLikeData();
            while(rS.next()){
                rol = rS.getString("tipo_rol");
                rolesCB.addItem(rol);
            }
        } catch (SQLException eRr){
            JOptionPane.showMessageDialog(null,"No existen roles", "WARNING",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cleanAll(){
        for (JTextField jT : jTexts){ jT.setText(""); }
        dniJT.setText("");
    }
    private void disabledAll(){
        cleanButton.setEnabled(false);
        createButton.setEnabled(false);
        for (JTextField jT : jTexts){ jT.setEnabled(false); }
        dateIngJT.setEnabled(false);
        rolesCB.setEnabled(false);
        dniJT.setEnabled(true);
    }
}