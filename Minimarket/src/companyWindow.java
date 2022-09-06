import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class companyWindow extends JFrame{
    private JTextField rucEmpJT;
    private JTextField nomEmpJT;
    private JButton saveButton;
    private JButton cleanButton;
    private JPanel emprJP;
    private JTextField telEmpJT;
    private JTextField emaEmpJT;
    private JTextField dirEmpJT;

    private JTextField[] jTexts = {rucEmpJT, nomEmpJT, emaEmpJT, telEmpJT, dirEmpJT};

    public  companyWindow(){
        setContentPane(emprJP);
        setTitle("Empresa");
        setSize(380, 250);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon(".\\src\\assets\\icons\\option\\iconMinimarket.png");
        setIconImage(image.getImage());
        connection con = new connection();
        validations val = new validations();

        con.setTable("empresa");
        con.setColumn("ruc_Emp");
        con.setData("");
        ResultSet datEmp = con.qryLikeData();

        try{
            if (datEmp.next()){
                rucEmpJT.setText(datEmp.getString(1));
                nomEmpJT.setText(datEmp.getString(2));
                telEmpJT.setText(datEmp.getString(3));
                emaEmpJT.setText(datEmp.getString(4));
                dirEmpJT.setText(datEmp.getString(5));
            }
        } catch (SQLException eLd){
            System.out.println(eLd);
        }

        saveButton.addActionListener(e -> {
            boolean emptyFields = val.validateEmptyFields(jTexts);
            if (emptyFields){
                String rucEmp = rucEmpJT.getText();
                String nomEmp = nomEmpJT.getText();
                String emaEmp = emaEmpJT.getText();
                String telEmp = telEmpJT.getText();
                String dirEmp = dirEmpJT.getText();
                boolean validations = true;

                if(val.validateNumbers(rucEmp) | val.lengthField(rucEmp, 13)){
                    JOptionPane.showMessageDialog(null, "RUC Incorrecto", "ERROR", JOptionPane.ERROR_MESSAGE);
                    validations = false;
                }
                if (val.validateNumbers(telEmp) | val.lengthField(telEmp, 10)){
                    JOptionPane.showMessageDialog(null, "Telefono Incorrecto", "ERROR", JOptionPane.ERROR_MESSAGE);
                    validations = false;
                }
                if (val.validateEmail(emaEmp)){
                    JOptionPane.showMessageDialog(null, "Email Incorrecto", "ERROR", JOptionPane.ERROR_MESSAGE);
                    validations = false;
                }
                if (val.minLengthField(dirEmp, 10)){
                    JOptionPane.showMessageDialog(null, "Direccion Incorrecta", "ERROR", JOptionPane.ERROR_MESSAGE);
                    validations = false;
                }

                if (validations){
                    con.saveDataEmp(rucEmp, nomEmp, telEmp, emaEmp , dirEmp);
                    JOptionPane.showMessageDialog(null, "Datos Guardados", "EXITO", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Campos Vacios", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        cleanButton.addActionListener(e -> cleanAll());
    }

    private void cleanAll(){
        for(JTextField jT: jTexts){
            jT.setText("");
        }
    }
}
