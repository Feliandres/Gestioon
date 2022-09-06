import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class searchWindow extends JFrame{
    private JComboBox optionsCB;
    private JTextField strSearchJT;
    private JButton queryButton;
    private JTable allTable;
    private JPanel readW;
    private JComboBox tableCB;
    private int len;

    public searchWindow(String tables){
        // Characteristics of Window
        setContentPane(readW);
        setSize(650,500);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("Inventario");
        setResizable(false);
        ImageIcon image = new ImageIcon(".\\src\\assets\\search_Window.png");
        setIconImage(image.getImage());

        // Fill comboBox with tables names
        for(String n: tables.split(",")){ tableCB.addItem(n); }

        // Fix layout
        JScrollPane scrollPane = new JScrollPane(this.allTable);
        getContentPane().setLayout(new FlowLayout());
        this.strSearchJT.setColumns(7);
        getContentPane().add(scrollPane);

        // Choose table to search
        queryButton.addActionListener(e -> {
            String table = String.valueOf(tableCB.getSelectedItem()).toLowerCase();
            showData(table);
        });
    }
    private void showData(String table){
        // Connection to SQL core
        connection co = new connection();

        // Heads of Tables
        String[] columnsProduct = {"Codigo", "Detalle", "P. Compra", "P. Venta", "Stock", "Descuento"};
        String[] columnsProvider = {"RUC", "Nombre", "Telefono", "Email"};
        String[] columnsEmployee = {"DNI", "Nombre", "Apellido", "Fecha Ing.", "Telefono", "Email", "Rol", "Usuario"};
        String[] columnsClient = {"DNI", "Nombre", "Apellido", "Telefono", "Email", "Direccion"};

        // Send table name and string to search
        String likeSearch = strSearchJT.getText();
        co.setTable(table);
        co.setData(likeSearch);

        // Initial variables to use
        String column = "";
        DefaultTableModel t = new DefaultTableModel();
        int oS =  optionsCB.getSelectedIndex();

        // Decide for what column search
        switch (table){
            case "producto":
                column = (oS == 0) ? "cod_Pro" : "det_Pro";
                for(String n: columnsProduct){
                    t.addColumn(n);
                }
                this.len = columnsProduct.length;
                break;
            case "usuario":
                column = (oS == 0) ? "ident_Usu" : "nom_Usu";
                for(String n: columnsEmployee){
                    t.addColumn(n);
                }
                this.len = columnsEmployee.length;
                break;
            case "proveedor":
                column = (oS == 0) ? "ident_prov" : "nom_prov";
                for(String n: columnsProvider){
                    t.addColumn(n);
                }
                this.len = columnsProvider.length;
                break;
            case "cliente":
                column = (oS == 0) ? "ident_Cli" : "nom_Cli";
                for(String n: columnsClient){
                    t.addColumn(n);
                }
                this.len = columnsClient.length;
                break;
        }

        // Send column to search
        co.setColumn(column);

        // Set head of table
        this.allTable.setModel(t);
        String[] data = new String[this.len];

        // Load data in table
        try{
            // Query data with regular expressions
            ResultSet rS = co.qryLikeData();
            while (rS.next()){
                for (int i = 0; i < this.len; i++){
                    data[i] = String.valueOf(rS.getString(i+1));
                }
                t.addRow(data);
            }
            this.allTable.setModel(t);
        } catch (Exception er){
            JOptionPane.showMessageDialog(null, "THIS NOT HAPPENED", "EASTER EGG", JOptionPane.QUESTION_MESSAGE);
        }
    }
}