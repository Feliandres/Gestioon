import javax.swing.*;
import java.sql.ResultSet;

public class rolWindow extends JFrame{
    private JPanel rolWindow;
    private JTextField nameRolJT;
    private JTextField codeRolJT;
    private JButton queryRButton;
    private JButton createButton;
    private JButton cleanButton;
    public rolWindow(int option){
        // Connection Core SQL
        connection co = new connection();
        co.setTable("rol");
        co.setColumn("tipo_rol");

        // Characteristics of Window
        setContentPane(rolWindow);
        setSize(400, 200);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        ImageIcon image = new ImageIcon(".\\src\\assets\\roles_Window.png");
        setIconImage(image.getImage());

        // Default title
        String titleWindow = "";
        switch (option){
            case 1:
                titleWindow = "ACTUALIZAR ROL";
                createButton.setText("ACTUALIZAR");
                break;
            case 2:
                titleWindow = "ELIMINAR ROL";
                createButton.setText("ELIMINAR");
                break;
        }
        setTitle(titleWindow);

        // Occult buttons with option choose
        queryRButton.setVisible(true);
        this.disabledAll();



        // Query and load data in fields
        queryRButton.addActionListener(e -> {
            String codeRol = codeRolJT.getText();
            if (codeRol.length() != 0){
                co.setData(codeRol.trim());
                ResultSet rs = co.qryData();

                try {
                    if (rs.next()) {
                        if (option == 1) {
                            nameRolJT.setEnabled(true);
                        }
                        createButton.setEnabled(true);
                        cleanButton.setEnabled(true);
                        nameRolJT.setText(rs.getString("desc_rol").trim());
                    } else {
                        JOptionPane.showMessageDialog(null, "Rol no encontrado!", "ERROR"
                                , JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception eP) {
                    System.out.println("[-] ERROR, load data into JTextFields Failed !");
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR",JOptionPane.ERROR_MESSAGE);
            }
        });
        // CRUD of Rol
        createButton.addActionListener(e -> {
            validations val = new validations();

            if (nameRolJT.getText().length() == 0 | codeRolJT.getText().length() == 0){
                JOptionPane.showMessageDialog(null,"Campos Vacios", "ERROR"
                        , JOptionPane.ERROR_MESSAGE);
            } else {
                if (val.lengthField(codeRolJT.getText(), 4)){
                    Integer code;
                    String codeN = codeRolJT.getText();
                    String nameM = nameRolJT.getText();

                    switch (option){
                        case 1:
                            code = co.updateRol(codeN, nameM);
                            if (code.equals(0)){
                                JOptionPane.showMessageDialog(null,"Rol Actualizado", "Exito"
                                        ,JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null,"Codigo Error: " + code, "ERROR"
                                        ,JOptionPane.ERROR_MESSAGE);
                                JOptionPane.showMessageDialog(null,"Rol no creado", "ERROR"
                                        ,JOptionPane.ERROR_MESSAGE);
                            }
                            this.disabledAll();
                            this.cleanAll();
                            break;
                        case 2:
                            co.deleteData();
                            JOptionPane.showMessageDialog(null,"Rol Eliminado", "Exito"
                                    ,JOptionPane.INFORMATION_MESSAGE);
                            this.disabledAll();
                            this.cleanAll();
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Codigo Invalido", "ERROR",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        // Clean/Disabled
        cleanButton.addActionListener(e -> {
            this.disabledAll();
            this.cleanAll();
        });
    }

    // Clean JTextFields
    private void cleanAll(){
        this.nameRolJT.setText("");
        this.codeRolJT.setText("");
    }

    // Disabled JTextField and Buttons
    private void disabledAll(){
        cleanButton.setEnabled(false);
        createButton.setEnabled(false);
        this.nameRolJT.setEnabled(false);
    }
}
