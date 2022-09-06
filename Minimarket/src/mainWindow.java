import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class mainWindow extends  JFrame{
    private JMenu subMenuUsers, subMenuProducts, subMenuProviders, subMenuRoles, subMenuInventory
            , subMenuReports, subMenuSell, subMenuOption;
    private JMenuItem itemRegisterU;
    private JMenuItem itemDeleteU;
    private JMenuItem itemRegisterP;
    private JMenuItem itemUpdateP;
    private JMenuItem itemDeletePr;
    private JMenuItem itemUpdatePr;
    private JMenuItem itemDeleteR;
    private JMenuItem itemUpdateR;
    private JMenuItem itemInventory;
    private JMenuItem itemSearchAll;
    private JMenuItem itemPath;
    private JMenuItem itemEmp;
    private JMenuItem itemUpdateU;
    private JMenuItem itemGenerateSell;
    private JMenuItem itemDeleteP;
    private JMenuItem itemRegisterPr;

    private ImageIcon image;


    public mainWindow(){
        // Create a main MenuBar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Variables to use
        String register = "Registrar";
        String delete = "Eliminar";
        String update = "Actualizar";

        // Create submenus for CRUD
        if(loginWindow.getRol().equals("adm")){
            subMenuSell = new JMenu("Factura");
            menuBar.add(subMenuSell);

            subMenuUsers = new JMenu("Usuarios");
            menuBar.add(subMenuUsers);

            subMenuProducts = new JMenu("Productos");
            menuBar.add(subMenuProducts);

            subMenuProviders = new JMenu("Proveedores");
            menuBar.add(subMenuProviders);

            subMenuRoles = new JMenu("Roles");
            menuBar.add(subMenuRoles);

            subMenuInventory = new JMenu("Consultar");
            menuBar.add(subMenuInventory);

            subMenuReports = new JMenu("Reportes");
            menuBar.add(subMenuReports);

            subMenuOption = new JMenu("Opciones");
            menuBar.add(subMenuOption);


            // Items of 'Sell'
            itemGenerateSell = new JMenuItem("Generar");
            image = new ImageIcon(".\\src\\assets\\sell_icon.png");
            itemGenerateSell.setIcon(image);
            subMenuSell.add(itemGenerateSell);


            // Items of 'Users'
            itemRegisterU = new JMenuItem(register);
            image = new ImageIcon(".\\src\\assets\\registerUser_icon.png");
            itemRegisterU.setIcon(image);
            subMenuUsers.add(itemRegisterU);

            itemDeleteU = new JMenuItem(delete);
            image = new ImageIcon(".\\src\\assets\\deleteUser_icon.png");
            itemDeleteU.setIcon(image);
            subMenuUsers.add(itemDeleteU);

            itemUpdateU = new JMenuItem(update);
            image = new ImageIcon(".\\src\\assets\\updateUser_icon.png");
            itemUpdateU.setIcon(image);
            subMenuUsers.add(itemUpdateU);


            // Items of 'Products'
            itemRegisterP = new JMenuItem(register);
            image = new ImageIcon(".\\src\\assets\\registerProduct_icon.png");
            itemRegisterP.setIcon(image);
            subMenuProducts.add(itemRegisterP);

            itemDeleteP = new JMenuItem(delete);
            image = new ImageIcon(".\\src\\assets\\deleteProduct_icon.png");
            itemDeleteP.setIcon(image);
            subMenuProducts.add(itemDeleteP);

            itemUpdateP = new JMenuItem(update);
            image = new ImageIcon(".\\src\\assets\\updateProduct_icon.png");
            itemUpdateP.setIcon(image);
            subMenuProducts.add(itemUpdateP);


            // Items of 'Providers'
            itemRegisterPr = new JMenuItem(register);
            image = new ImageIcon(".\\src\\assets\\crateProv_icon.png");
            itemRegisterPr.setIcon(image);
            subMenuProviders.add(itemRegisterPr);

            itemDeletePr = new JMenuItem(delete);
            image = new ImageIcon(".\\src\\assets\\deleteProv_icon.png");
            itemDeletePr.setIcon(image);
            subMenuProviders.add(itemDeletePr);

            itemUpdatePr = new JMenuItem(update);
            image = new ImageIcon(".\\src\\assets\\updateProv_icon.png");
            itemUpdatePr.setIcon(image);
            subMenuProviders.add(itemUpdatePr);


            // Items of 'Roles'
            itemDeleteR = new JMenuItem(delete);
            image = new ImageIcon(".\\src\\assets\\deleteRol.png");
            itemDeleteR.setIcon(image);
            subMenuRoles.add(itemDeleteR);

            itemUpdateR = new JMenuItem(update);
            image = new ImageIcon(".\\src\\assets\\updateRol.png");
            itemUpdateR.setIcon(image);
            subMenuRoles.add(itemUpdateR);


            // Items of 'Reports'
            itemInventory = new JMenuItem("Todo Inventario");
            image = new ImageIcon(".\\src\\assets\\inventory_icon.png");
            itemInventory.setIcon(image);
            subMenuReports.add(itemInventory);


            // Items of 'Query'
            itemSearchAll = new JMenuItem("Inventario");
            image = new ImageIcon(".\\src\\assets\\inventorySearch_icon.png");
            itemSearchAll.setIcon(image);
            subMenuInventory.add(itemSearchAll);


            // Items of 'Options'
            itemPath = new JMenuItem("Configurar ruta");
            image = new ImageIcon(".\\src\\assets\\path_icon.png");
            itemPath.setIcon(image);
            subMenuOption.add(itemPath);

            itemEmp = new JMenuItem("Datos Empresa");
            image = new ImageIcon(".\\src\\assets\\tienda_icon.png");
            itemEmp.setIcon(image);
            subMenuOption.add(itemEmp);

            /*
                    Methods of all options
            */
            // Users
            itemRegisterU.addActionListener(e -> new userWindow(0));
            itemUpdateU.addActionListener(e -> new userWindow(1));
            itemDeleteU.addActionListener(e -> new userWindow(2));

            // Roles
            itemUpdateR.addActionListener(e -> new rolWindow(1));
            itemDeleteR.addActionListener(e -> new rolWindow(2));

            // Providers
            itemRegisterPr.addActionListener(e -> new providerWindow(0));
            itemUpdatePr.addActionListener(e -> new providerWindow(1));
            itemDeletePr.addActionListener(e -> new providerWindow(2));

            // Products
            itemRegisterP.addActionListener(e -> new productWindow(0));
            itemUpdateP.addActionListener(e -> new productWindow(1));
            itemDeleteP.addActionListener(e -> new productWindow(2));

            // Generate Facture
            itemGenerateSell.addActionListener(e -> new sellWindow());

            // Options
            itemPath.addActionListener(e -> new pathWindow());
            itemEmp.addActionListener(e -> new companyWindow());

            // Reports
            itemInventory.addActionListener(e -> {
                connection co = new connection();
                String path = "";
                try{
                    ResultSet pathSave = co.getPathSale();
                    if(pathSave.next()){
                        path = pathSave.getString(1);
                    }
                } catch (SQLException eVp){
                    System.out.println(eVp);
                }
                if (new File(path).exists()){
                    new allReportWindow(path);
                }
            });

        } else{
            subMenuSell = new JMenu("Factura");
            menuBar.add(subMenuSell);

            subMenuUsers = new JMenu("Usuarios");
            menuBar.add(subMenuUsers);

            subMenuProducts = new JMenu("Productos");
            menuBar.add(subMenuProducts);

            subMenuInventory = new JMenu("Consultar");
            menuBar.add(subMenuInventory);

            subMenuReports = new JMenu("Reportes");
            menuBar.add(subMenuReports);

            subMenuOption = new JMenu("Opciones");
            menuBar.add(subMenuOption);

            // Items of 'Sell'
            itemGenerateSell = new JMenuItem("Generar");
            image = new ImageIcon(".\\src\\assets\\deleteProduct_icon.png");
            itemGenerateSell.setIcon(image);
            subMenuSell.add(itemGenerateSell);

            // Items of 'Users'
            itemRegisterU = new JMenuItem(register);
            image = new ImageIcon(".\\src\\assets\\registerUser_icon.png");
            itemRegisterU.setIcon(image);
            subMenuUsers.add(itemRegisterU);

            itemUpdateU = new JMenuItem(update);
            image = new ImageIcon(".\\src\\assets\\updateUser_icon.png");
            itemUpdateU.setIcon(image);
            subMenuUsers.add(itemUpdateU);


            // Items of 'Products'
            itemRegisterP = new JMenuItem(register);
            image = new ImageIcon(".\\src\\assets\\registerProduct_icon.png");
            itemRegisterP.setIcon(image);
            subMenuProducts.add(itemRegisterP);

            itemUpdateP = new JMenuItem(update);
            image = new ImageIcon(".\\src\\assets\\updateProduct_icon.png");
            itemUpdateP.setIcon(image);
            subMenuProducts.add(itemUpdateP);


            // Items of 'Reports'
            itemInventory = new JMenuItem("Todo Inventario");
            image = new ImageIcon(".\\src\\assets\\inventory_icon.png");
            itemInventory.setIcon(image);
            subMenuReports.add(itemInventory);

            // Items of 'Query'
            itemSearchAll = new JMenuItem("Inventario");
            image = new ImageIcon(".\\src\\assets\\inventorySearch_icon.png");
            itemSearchAll.setIcon(image);
            subMenuInventory.add(itemSearchAll);

            // Items of 'Options'
            itemPath = new JMenuItem("Configurar ruta");
            image = new ImageIcon(".\\src\\assets\\path_icon.png");
            itemPath.setIcon(image);
            subMenuOption.add(itemPath);

            // Users
            itemRegisterU.addActionListener(e -> new userWindow(0));
            itemUpdateU.addActionListener(e -> new userWindow(1));

            // Products
            itemRegisterP.addActionListener(e -> new productWindow(0));
            itemUpdateP.addActionListener(e -> new productWindow(1));

            // Generate Facture
            itemGenerateSell.addActionListener(e -> new sellWindow());

            // Options
            itemPath.addActionListener(e -> new pathWindow());
        }


        // Characteristics of Window
        setTitle("Veci's Market");
        setSize(617,650);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        image = new ImageIcon(".\\src\\assets\\menu_Window.png");
        setIconImage(image.getImage());

        JLabel label = new JLabel();
        add(label);
        ImageIcon imageIcon = new ImageIcon(".\\src\\assets\\principalImage.png");
        label.setIcon(imageIcon);




        // Query Inventory
        String tableAccess = "producto,proveedor,usuario,cliente";
        if (loginWindow.getRol().equals("ven")){
            tableAccess = "producto,cliente";
        }
        String finalTableAccess = tableAccess;
        itemSearchAll.addActionListener(e -> new searchWindow(finalTableAccess));
    }
}