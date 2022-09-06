import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class pathWindow extends  JFrame{
    private JButton SAVEButton;
    private JTextField pathJT;
    private JPanel configurationW;

    public pathWindow(){
        setContentPane(configurationW);
        setSize(550,150);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuracion de Path");
        ImageIcon image = new ImageIcon(".\\src\\assets\\icon_path.png");
        setIconImage(image.getImage());
        setResizable(false);
        connection co = new connection();
        this.showLastPath(co);

        SAVEButton.addActionListener(e -> {
            String pathRoute = pathJT.getText();
            if (pathRoute.length() != 0){
                int error = co.configurationPath(pathRoute);
                if (error == 0){
                    JOptionPane.showMessageDialog(null,"Path agregado", "EXITO",JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null,"Codigo Error: " + error, "ERROR",JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Campo Vacio", "ERROR",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showLastPath(connection co){
        try{
            ResultSet rS = co.getPathSale();
            if (rS.next()){
                pathJT.setText(rS.getString("path_save"));
            }
        } catch (SQLException ec){
            System.out.println(ec);
        }
    }
}