import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginWindow extends JFrame {
    private JPanel JPanelLogin;
    private JComboBox rolComboBox;
    private JTextField nameUserJT;
    private JPasswordField passwordJP;
    private JButton loginButton;

    private static String rol;

    public loginWindow(){
        // Characteristics Window Login
        setContentPane(JPanelLogin);
        setVisible(true);
        setTitle("Veci's Market");
        setSize(380, 400);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon(".\\src\\assets\\icons\\login\\iconLogoWindow.png");
        setIconImage(image.getImage());
        connection con = new connection();
        con.sendCredentials();

        try{
            con.setTable("rol");
            con.setColumn("tipo_rol");
            con.setData("");
            String rolCB;
            ResultSet rS = con.qryLikeData();
            while(rS.next()){
                rolCB = rS.getString("desc_rol");
                rolComboBox.addItem(rolCB.toUpperCase());
            }
        } catch (SQLException eRr){
            System.out.println(eRr);
        }
        // Validation credentials
        loginButton.addActionListener(e -> {
            try{
                loginWindow.setRol(String.valueOf(rolComboBox.getSelectedItem()).substring(0,3).toLowerCase());
                String nameUser = nameUserJT.getText();
                String passwordHash =  String.valueOf(passwordJP.getPassword());

                if (nameUser.length() == 0 & passwordHash.length() == 0){
                    JOptionPane.showMessageDialog(null, "Campos Vacios", "ERROR"
                            , JOptionPane.ERROR_MESSAGE);
                } else{
                    ResultSet result = con.loginRol(nameUser, passwordHash,loginWindow.getRol());
                    if (result.next()){
                        credentialsToUse.setFKident_Usu(result.getString("ident_Usu"));
                        credentialsToUse.setNom_Usu(result.getString("nom_Usu"));
                        dispose();
                        new mainWindow();

                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciales Incorrectas", "ERROR"
                                , JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception loginB){
                System.out.println("Error Login" + loginB);
            }
        });
    }

    public static String getRol() {
        return rol;
    }
    public static void setRol(String rol) {
        loginWindow.rol = rol;
    }
}
