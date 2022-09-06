import org.omg.CORBA.PUBLIC_MEMBER;

import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class connection {
    private Connection con;
    private PreparedStatement pS;
    private int errorCode;

    // Data for queries
    private String table;
    private String column;
    private String data;

    private static int num_CF;


    // Constructor
    public connection(){
        this.getConnection();
    }

    // Get connection to BDD
    private void getConnection(){
        try{ this.con = DriverManager.getConnection("jdbc:mysql://localhost/minimarket", "root"
                , "");
        } catch (SQLException e){ System.out.println("Exception: " + e); }
    }

    // ---------------------------- General Queries ----------------------------
    public ResultSet qryData(){
        try{
            String sqlQry = "SELECT * FROM " + this.getTable() + " WHERE " + this.getColumn() + " = ?";
            this.pS = con.prepareStatement(sqlQry);
            this.pS.setString(1, this.getData());
            return this.pS.executeQuery();
        } catch (Exception e){
            System.out.println("Exception: " + e);
            return null;
        }
    }
    public ResultSet qryLikeData(){
        try{
            String qryLData = "SELECT * FROM "+ this.getTable() + " WHERE " + this.getColumn() + " LIKE  '%"+ this.getData() +"%'";
            this.pS = this.con.prepareStatement(qryLData);
            return this.pS.executeQuery();
        } catch (SQLException erQryLD){
            return null;
        }
    }
    public void deleteData(){
        try{
            String sqlDelete = "DELETE FROM " + this.table + " WHERE " + this.column + " = '" + this.data + "'";
            this.pS = con.prepareStatement(sqlDelete);
            this.pS.executeUpdate();
        } catch (Exception e){
            System.out.println("Exception: " + e);
        }
    }

    public ResultSet qryAllInventory(){
        try{
            String sqlAllInventory = "SELECT P.cod_pro, P.det_Pro, P.preUni_Pro, P.preVen_Pro, P.sto_Pro, P.desc_Pro, Pr.nom_prov FROM producto P, proveedor Pr  WHERE P.FKident_prov = Pr.ident_prov";
            this.pS = this.con.prepareStatement(sqlAllInventory);
            return this.pS.executeQuery();
        } catch (SQLException erQryAL){
            return null;
        }
    }
    // -------------------------------------------------------------------------


    // ---------------------------- Create & Update USER ----------------------------
    public int createUser(String ident_Usu, String nom_Usu, String ape_Usu, String tel_Usu, String ema_Usu
            , String FKtipo_rol, String usuN_Usu, String pass_Usu){
        Calendar d = new GregorianCalendar();
        String ingUsu = String.valueOf(d.get(Calendar.YEAR)) + '-' + (d.get(Calendar.MONTH) + 1) + '-'
                + d.get(Calendar.DAY_OF_MONTH);
        this.errorCode = 0;
        try{
            String createUserSql = "INSERT INTO usuario(ident_Usu, nom_Usu, ape_Usu, ing_Usu, tel_Usu, " +
                    "ema_Usu, FKtipo_rol, usuN_Usu, pass_Usu) VALUES (?,?,?,?,?,?,?,?,?)";
            this.pS = con.prepareStatement(createUserSql);
            this.pS.setString(1, ident_Usu);
            this.pS.setString(2, nom_Usu);
            this.pS.setString(3, ape_Usu);
            this.pS.setString(4, ingUsu);
            this.pS.setString(5, tel_Usu);
            this.pS.setString(6, ema_Usu);
            this.pS.setString(7, FKtipo_rol);
            this.pS.setString(8, usuN_Usu);
            this.pS.setString(9, pass_Usu);
            this.pS.executeUpdate();
        } catch (SQLException e){
            this.errorCode = e.getErrorCode();
        }
        return this.errorCode;
    }
    public int updateUser(String ident_Usu, String nom_Usu, String ape_Usu, String ing_Usu, String tel_Usu
            , String ema_Usu, String FKtipo_rol, String usuN_Usu, String pass_Usu){
        this.errorCode = 0;
        try{
            String updateEmployeeSql = "UPDATE usuario SET ident_Usu = ?, nom_Usu = ?, ape_Usu = ?, ing_Usu = ?, " +
                    "tel_Usu = ?, ema_Usu = ?, FKtipo_rol = ?, usuN_Usu = ?, pass_Usu = ? WHERE ident_Usu = ?";
            this.pS = con.prepareStatement(updateEmployeeSql);
            this.pS.setString(1, ident_Usu);
            this.pS.setString(2, nom_Usu);
            this.pS.setString(3, ape_Usu);
            this.pS.setString(4, ing_Usu);
            this.pS.setString(5, tel_Usu);
            this.pS.setString(6, ema_Usu);
            this.pS.setString(7, FKtipo_rol);
            this.pS.setString(8, usuN_Usu);
            this.pS.setString(9, pass_Usu);
            this.pS.setString(10, this.getData());
            this.pS.executeUpdate();
        } catch (SQLException e){
            this.errorCode = e.getErrorCode();
        }
        return this.errorCode;
    }
    // -------------------------------------------------------------------------------


    // ---------------------------- Create & Update ROL ----------------------------
    public int updateRol(String tipo_rol, String desc_rol){
        this.errorCode = 0;
        try{
            String updateSqlRol = "UPDATE rol SET tipo_rol = ?, desc_rol = ? WHERE tipo_rol = ?";
            this.pS = con.prepareStatement(updateSqlRol);
            this.pS.setString(1, tipo_rol);
            this.pS.setString(2, desc_rol);
            this.pS.setString(3, this.getData());
            this.pS.executeUpdate();
        } catch (SQLException e){
            this.errorCode = e.getErrorCode();
        }
        return this.errorCode;
    }
    // -----------------------------------------------------------------------------


    // ---------------------------- Create & Update PROVIDER ----------------------------
    public int createProvider(String ident_prov, String nom_prov, String tel_prov, String ema_prov){
        this.errorCode = 0;
        try{
            String createSqlP = "INSERT INTO proveedor(ident_prov, nom_prov, tel_prov, ema_prov) VALUES (?,?,?,?)";
            this.pS = con.prepareStatement(createSqlP);
            this.pS.setString(1, ident_prov);
            this.pS.setString(2, nom_prov);
            this.pS.setString(3, tel_prov);
            this.pS.setString(4, ema_prov);
            this.pS.executeUpdate();

        } catch (SQLException e){
            this.errorCode = e.getErrorCode();
        }
        return this.errorCode;
    }
    public int updateProvider(String ident_prov, String nom_prov, String tel_prov, String ema_prov){
        this.errorCode = 0;
        try{
            String updateSqlP = "UPDATE proveedor SET ident_prov = ?, nom_prov = ?, tel_prov = ?, ema_prov = ? WHERE ident_prov = ?";
            this.pS = con.prepareStatement(updateSqlP);
            this.pS.setString(1, ident_prov);
            this.pS.setString(2, nom_prov);
            this.pS.setString(3, tel_prov);
            this.pS.setString(4, ema_prov);
            this.pS.setString(5, this.getData());
            this.pS.executeUpdate();
        } catch (SQLException e){
            this.errorCode = e.getErrorCode();
        }
        return this.errorCode;
    }
    // ----------------------------------------------------------------------------------


    // ---------------------------- Create & Update PRODUCT ----------------------------
    public int createProduct(String cod_Pro, String det_Pro, double preUni_Pro, double preVen_Pro, int sto_Pro
            , double desc_Pro, String FKident_Prov){
        this.errorCode = 0;
        try{
            String createSqlPr = "INSERT INTO producto VALUES(?,?,?,?,?,?,?) ";
            this.pS = con.prepareStatement(createSqlPr);
            this.pS.setString(1, cod_Pro);
            this.pS.setString(2, det_Pro);
            this.pS.setDouble(3, preUni_Pro);
            this.pS.setDouble(4,preVen_Pro);
            this.pS.setInt(5, sto_Pro);
            this.pS.setDouble(6, desc_Pro);
            this.pS.setString(7, FKident_Prov);
            this.pS.executeUpdate();
        } catch (SQLException e){
            this.errorCode = e.getErrorCode();
        }
        return this.errorCode;
    }
    public int updateProduct(String cod_Pro, String det_Pro, double preUni_Pro, double preVen_Pro, int sto_Pro
            , double desc_Pro, String FKident_Prov){
        this.errorCode = 0;
        try{
            String updateSqlPr = "UPDATE producto SET cod_Pro = ?, det_pro = ?, preUni_Pro = ?, preVen_Pro = ?," +
                    "sto_Pro = ?, desc_Pro  = ?, Fkident_Prov = ?  WHERE cod_Pro = ?";
            this.pS = con.prepareStatement(updateSqlPr);
            this.pS.setString(1, cod_Pro);
            this.pS.setString(2, det_Pro);
            this.pS.setDouble(3, preUni_Pro);
            this.pS.setDouble(4,preVen_Pro);
            this.pS.setInt(5, sto_Pro);
            this.pS.setDouble(6, desc_Pro);
            this.pS.setString(7, FKident_Prov);
            this.pS.setString(8, this.getData());
            this.pS.executeUpdate();
        } catch (SQLException e){
            this.errorCode = e.getErrorCode();
        }
        return this.errorCode;
    }

    public String rucProvider(String nomProv){
        try{
            String qryRuc = "SELECT ident_prov from proveedor WHERE nom_prov = ?";
            this.pS = this.con.prepareStatement(qryRuc);
            this.pS.setString(1, nomProv);
            ResultSet Fkident_prov = this.pS.executeQuery();
            if (Fkident_prov.next()){
                return Fkident_prov.getString(1);
            }
        } catch (SQLException ignored){}
        return nomProv;
    }
    // ----------------------------------------------------------------------------------


    // ---------------------------- Methods to Generate Sell ----------------------------
    public void createCab(){
        try{
            String createCabSql = "INSERT INTO cabfactura VALUES()";
            this.pS = this.con.prepareStatement(createCabSql);
            this.pS.executeUpdate();
            countC();
        } catch (SQLException eCc){
            System.out.println(eCc);
        }
    }
    private void countC(){
        try{
            String countCSql = "SELECT num_CF FROM cabfactura ORDER BY num_CF DESC LIMIT 1";
            this.pS = this.con.prepareStatement(countCSql);
            ResultSet num_CFr =  this.pS.executeQuery();
            if (num_CFr.next()){
                setNum_CF(num_CFr.getInt("num_CF"));
            }
        } catch (SQLException eCc){
            System.out.println(eCc);
        }
    }
    public void createDet(String Fkcod_pro, String detPro_Det, int cantPro_Det, double pUPro
            , double descPro,double valto_det){
        try {
            String createDetSql = "INSERT INTO detalle(FKnum_CF, FKcod_pro, codPro_Det, detPro_Det, cantPro_Det, pUPro, descPro,valto_det)" +
                    " VALUES (?,?,?,?,?,?,?,?)";
            this.pS = con.prepareStatement(createDetSql);
            this.pS.setInt(1, getNum_CF());
            this.pS.setString(2, Fkcod_pro);
            this.pS.setString(3, Fkcod_pro);
            this.pS.setString(4, detPro_Det);
            this.pS.setInt(5, cantPro_Det);
            this.pS.setDouble(6, pUPro);
            this.pS.setDouble(7, descPro);
            this.pS.setDouble(8, valto_det);
            this.pS.executeUpdate();
        } catch (SQLException eCt){
            System.out.println("FAILED :( CREATE DETAIL" + eCt);
        }
    }
    public ResultSet searchProduct(){
        try{
            String qrySearch = "SELECT det_Pro, preVen_Pro, desc_Pro FROM producto WHERE cod_pro = ?";
            this.pS = con.prepareStatement(qrySearch);
            this.pS.setString(1, this.data);
            return pS.executeQuery();
        } catch (SQLException eSp){
            System.out.println("FAILED");
            return null;
        }
    }
    public int stockProduct(String codeProduct){
        int stock = 0;
        try{
            String sqlStock = "SELECT sto_Pro FROM producto WHERE cod_Pro = ?";
            this.pS = this.con.prepareStatement(sqlStock);
            this.pS.setString(1,codeProduct);
            ResultSet stockPro = this.pS.executeQuery();
            if (stockPro.next()){
                stock = stockPro.getInt("sto_Pro");
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return stock;
    }
    public ResultSet readDet(){
        try{
            String qryReadDet = "SELECT * FROM detalle WHERE FKnum_CF = ?";
            this.pS = this.con.prepareStatement(qryReadDet);
            this.pS.setInt(1, getNum_CF());
            return this.pS.executeQuery();
        } catch (SQLException eRd){
            System.out.println("FAILED :( READ DET" + eRd);
            return null;
        }
    }
    public double currentPrice(){
        try{
            String sqlCurrentPrice = "SELECT subT_CF FROM cabfactura WHERE num_CF = " + getNum_CF();
            this.pS = this.con.prepareStatement(sqlCurrentPrice);
            ResultSet rS = this.pS.executeQuery();
            if (rS.next()){
                double currentP = rS.getDouble(1);
                if(rS.wasNull()){
                    return  0.0;
                } else{
                    return currentP;
                }
            } else {
                return 0.0;
            }
        } catch (SQLException eCp){
            return 0.0;
        }
    }

    public ResultSet searchInDetails(String codPro){
        try{
            String qryDetail = "SELECT * FROM detalle WHERE codPro_Det = ? AND FKnum_CF = ?";
            this.pS = this.con.prepareStatement(qryDetail);
            this.pS.setString(1, codPro);
            this.pS.setInt(2, connection.getNum_CF());
            return this.pS.executeQuery();
        } catch (SQLException eRd){
            System.out.println("FAILED :( READ DET" + eRd);
            return null;
        }
    }

    public void updateDetail(int cantPro, double pUPro, double descPro, String codPro){
        try{
            String updateDetail = "UPDATE detalle SET cantPro_DET = ?, pUPro = ?, descPro = ?, valto_det = ? WHERE FKnum_CF = ? AND codPro_Det = ?";
            this.pS = this.con.prepareStatement(updateDetail);
            this.pS.setInt(1, cantPro);
            this.pS.setDouble(2, pUPro);
            this.pS.setDouble(3, descPro);
            double subtTotal = pUPro * cantPro;
            double valtDetail = (descPro > 0.0) ? (double) Math.round((subtTotal- (subtTotal*(descPro/100)))*100)/100 : subtTotal;
            this.pS.setString(4, String.valueOf(String.format("%.2f", valtDetail)));
            this.pS.setInt(5, connection.getNum_CF());
            this.pS.setString(6, codPro);
            this.pS.executeUpdate();
        }catch (SQLException eRd){
            System.out.println("FAILED :( READ DET" + eRd);
        }
    }

    public void updateProductDetail(String codPro, int newStock){
        try{
            String qryProductDetail = "UPDATE producto SET sto_Pro = ? WHERE cod_Pro = ?";
            this.pS = this.con.prepareStatement(qryProductDetail);
            this.pS.setInt(1, newStock);
            this.pS.setString(2, codPro);
            this.pS.executeUpdate();
        }catch (SQLException eRd){
            System.out.println("FAILED :( READ DET" + eRd);
        }
    }

    public int deleteDetail(String codPro){
        this.errorCode = 0;
        try{
           String qryDeleteDetail = "DELETE FROM detalle WHERE codPro_Det = ? AND FKnum_CF = ?";
           this.pS = this.con.prepareStatement(qryDeleteDetail);
           this.pS.setString(1, codPro);
           this.pS.setInt(2, connection.getNum_CF());
           this.pS.executeUpdate();
        }catch (SQLException eRd){
            this.errorCode = eRd.getErrorCode();
        }
        return this.errorCode;
    }
    // ----------------------------------------------------------------------------------


    // ---------------------------- Methods of Make Sale ----------------------------
    public int createClient(String ident_Cli, String nom_Cli, String ape_Cli, String dir_Cli, String ema_Cli, String tel_Cli){
        this.errorCode = 0;
        try {
            String sqlCreateClient = "INSERT INTO cliente VALUES(?,?,?,?,?,?)";
            this.pS = this.con.prepareStatement(sqlCreateClient);
            this.pS.setString(1, ident_Cli);
            this.pS.setString(2, nom_Cli);
            this.pS.setString(3, ape_Cli);
            this.pS.setString(4, tel_Cli);
            this.pS.setString(5, ema_Cli);
            this.pS.setString(6, dir_Cli);
            this.pS.executeUpdate();
        } catch (SQLException eCt){
            this.errorCode = eCt.getErrorCode();
        }
        return this.errorCode;
    }
    public int updateClient(String ident_Cli, String nom_Cli, String ape_Cli, String dir_Cli, String ema_Cli, String tel_Cli){
        this.errorCode = 0;
        try{
            String sqlUpdateClient = "UPDATE cliente SET ident_Cli = ?, nom_Cli = ?, ape_Cli = ?, dir_Cli = ?, ema_Cli = ?, tel_Cli = ? " +
                    "WHERE ident_Cli = ?";
            this.pS = this.con.prepareStatement(sqlUpdateClient);
            this.pS.setString(1, ident_Cli);
            this.pS.setString(2, nom_Cli);
            this.pS.setString(3, ape_Cli);
            this.pS.setString(4, dir_Cli);
            this.pS.setString(5, ema_Cli);
            this.pS.setString(6, tel_Cli);
            this.pS.setString(7, this.data);
            this.pS.executeUpdate();
        } catch (SQLException eCt){
            this.errorCode = eCt.getErrorCode();
        }
        return this.errorCode;
    }
    // -------------------------------------------------------------------------------


    // ---------------------------- Methods of Sale ----------------------------
    public void updateFacture(String FKident_cli, String cnom_cli, String apeCli_CF, String dirCli_CF
            , String telCli_CF, Double iva_CF, Double valT_CF){
        try{
            Calendar d = new GregorianCalendar();
            String date = String.valueOf(d.get(Calendar.YEAR)) + '-' + (d.get(Calendar.MONTH) + 1) + '-'
                    + d.get(Calendar.DAY_OF_MONTH);

            String sqlUpdateFacture = "UPDATE cabfactura SET FKruc_Emp = ?, FKident_Cli = ?, cident_Cli = ?, FKident_Usu = ?, cident_Usu = ?" +
                    ",nom_Usu = ?, nomCli_CF = ?, apeCli_CF = ?, telCli_CF = ?, dirCli_CF = ?, fecha_CF = ?" +
                    ", iva_CF = ?, valT_CF = ? WHERE num_CF = ?";
            this.pS = this.con.prepareStatement(sqlUpdateFacture);
            this.pS.setString(1, credentialsToUse.Fkruc_Emp);
            this.pS.setString(2, FKident_cli);
            this.pS.setString(3, FKident_cli);
            this.pS.setString(4, credentialsToUse.getFKident_Usu());
            this.pS.setString(5, credentialsToUse.getFKident_Usu());
            this.pS.setString(6, credentialsToUse.nom_Usu);
            this.pS.setString(7, cnom_cli);
            this.pS.setString(8 , apeCli_CF);
            this.pS.setString(9, telCli_CF);
            this.pS.setString(10, dirCli_CF);
            this.pS.setString(11, date);
            this.pS.setDouble(12, iva_CF);
            this.pS.setDouble(13, valT_CF);
            this.pS.setInt(14, getNum_CF());
            this.pS.executeUpdate();
        } catch (SQLException eUf){
            System.out.println(eUf);
        }
    }
    // -------------------------------------------------------------------------------


    // ---------------------------- Methods of Configuration ----------------------------
    public int configurationPath(String path){
        this.errorCode = 0;
        try{
            String sqlConfigurationPath = "UPDATE pathpdf SET path_save = ? WHERE id = 1";
            this.pS = this.con.prepareStatement(sqlConfigurationPath);
            this.pS.setString(1, path);
            this.pS.executeUpdate();
        } catch (SQLException eCp){
            this.errorCode = eCp.getErrorCode();
        }
        return this.errorCode;
    }
    public ResultSet getPathSale(){
        try{
            String sqlGetPathSale = "SELECT path_save FROM pathpdf WHERE id = 1";
            this.pS = this.con.prepareStatement(sqlGetPathSale);
            return this.pS.executeQuery();
        } catch (SQLException eOp){
            System.out.println(eOp);
            return null;
        }
    }
    // -------------------------------------------------------------------------------------


    // ---------------------------- Login ----------------------------
    public ResultSet loginRol(String userNameLog, String passwordHash, String code_rol){
        try {
            String qryRol = "SELECT * FROM usuario WHERE usuN_Usu = '"+ userNameLog + "' AND pass_Usu = '" + passwordHash
                    + "' AND FKtipo_rol = '" + code_rol + "'";
            this.pS = this.con.prepareStatement(qryRol);
            return this.pS.executeQuery();
        } catch (SQLException loginE){
            System.out.println("Exception: " + loginE);
            return null;
        }
    }
    // -----------------------------------------------------------------------------------


    // ------------------------ Send Credentials of 'Empresa' ------------------------
    public void sendCredentials(){
        String sqlCredtendial = "SELECT * FROM empresa";
        try{
            this.pS = this.con.prepareStatement(sqlCredtendial);
            ResultSet credentialsEmp = this.pS.executeQuery();
            if(credentialsEmp.next()){
                credentialsToUse.setFkruc_Emp(credentialsEmp.getString("ruc_Emp"));
                credentialsToUse.setNom_Emp(credentialsEmp.getString("nom_Emp"));
                credentialsToUse.setTel_Emp(credentialsEmp.getString("tel_Emp"));
                credentialsToUse.setEma_Emp(credentialsEmp.getString("ema_Emp"));
                credentialsToUse.setDir_Emp(credentialsEmp.getString("dir_Emp"));
            }

        } catch (SQLException eSc){
            System.out.println(eSc);
        }
    }
    // -------------------------------------------------------------------------------


    // ------------------------ Save data of 'Empresa' ------------------------
    public void saveDataEmp(String ruc_Emp, String nom_Emp, String tel_Emp, String ema_Emp, String dir_Emp){
        try{
            this.getRucLast();
            String qrySaveData = "UPDATE empresa SET ruc_Emp = ?, nom_Emp = ?, tel_Emp = ?, ema_Emp = ?, dir_Emp =? WHERE ruc_Emp = ?";
            this.pS = this.con.prepareStatement(qrySaveData);
            this.pS.setString(1, ruc_Emp);
            this.pS.setString(2, nom_Emp);
            this.pS.setString(3, tel_Emp);
            this.pS.setString(4, ema_Emp);
            this.pS.setString(5, dir_Emp);
            this.pS.setString(6, this.getData());
            this.pS.executeUpdate();
            this.sendCredentials();
        } catch (SQLException eSd){
            System.out.println(eSd);
        }
    }
    private void getRucLast(){
        try{
            String  qryRucLast = "SELECT ruc_Emp FROM empresa";
            this.pS = this.con.prepareStatement(qryRucLast);
            ResultSet rucLast = this.pS.executeQuery();
            if (rucLast.next()){
                this.setData(rucLast.getString(1));
            }

        } catch (SQLException eSd){
            System.out.println(eSd);
        }
    }

    // ------------------------------------------------------------------------


    // ------------------------ Get Number of Details ------------------------
    public int numberDetails(){
        try{
            String qryNumberDetails = " SELECT COUNT(id_De) FROM detalle WHERE FKnum_CF = ?";
            this.pS = this.con.prepareStatement(qryNumberDetails);
            this.pS.setInt(1, getNum_CF());
            ResultSet numberDetails = this.pS.executeQuery();
            if (numberDetails.next()){
                return numberDetails.getInt(1);
            }
        } catch (SQLException eNd){
            System.out.println(eNd);
        }
        return 0;
    }
    // ------------------------------------------------------------------------


    // ------------------------ Get Max & Min Data Products ------------------------
    public ResultSet maxMinData(String fromDate, String toDate){
        try {
            String qryMaxMin = "SELECT D.codPro_Det, D.detPro_Det, SUM(Cb.valT_CF), SUM(D.cantPro_Det) FROM CabFactura Cb, detalle D, producto P" +
                    " WHERE cb.num_CF = D.Fknum_CF AND D.codPro_Det = P.cod_Pro AND Cb.fecha_CF BETWEEN ? AND ?" +
                    " GROUP BY D.codPro_Det" +
                    " ORDER BY SUM(D.cantPro_Det) DESC LIMIT 2";
            this.pS = this.con.prepareStatement(qryMaxMin);
            this.pS.setString(1, fromDate);
            this.pS.setString(2, toDate);
            return this.pS.executeQuery();
        } catch (SQLException loginE){
            System.out.println("Exception: " + loginE);
            return null;
        }
    }


    // ----------------------------- GETTERS AND SETTERS -----------------------------
    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }
    public void setColumn(String column) {
        this.column = column;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public static int getNum_CF() {
        return num_CF;
    }
    public static void setNum_CF(int num_CF) {
        connection.num_CF = num_CF;
    }
}