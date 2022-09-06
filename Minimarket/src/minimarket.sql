CREATE DATABASE minimarket;
USE minimarket;

CREATE TABLE rol(
	tipo_rol VARCHAR(3) PRIMARY KEY NOT NULL,
    desc_rol VARCHAR(15) NOT NULL
);

INSERT INTO rol VALUES ('adm', 'administrador'), ('ven','vendedor');

CREATE TABLE proveedor(
	ident_prov VARCHAR(13) PRIMARY KEY NOT NULL,
    nom_prov VARCHAR(20) NOT NULL,
    tel_prov VARCHAR(10) NULL,
    ema_prov VARCHAR(30) NULL 
);
INSERT INTO proveedor VALUES ('1752774305001', 'PROCAN S.A', '0961731484', 'procan@gmail.com');

CREATE TABLE producto(
	cod_Pro VARCHAR(7) PRIMARY KEY NOT NULL,
    det_Pro VARCHAR(50) NOT NULL,
    preUni_Pro DOUBLE NOT NULL,
    preVen_Pro DOUBLE NOT NULL,
    sto_Pro INT4 NOT NULL,
    desc_Pro DECIMAL(4,2) NOT NULL,
    FKident_Prov VARCHAR(13) NULL,
    FOREIGN KEY (FKident_Prov) REFERENCES proveedor(ident_prov) ON DELETE SET NULL ON UPDATE CASCADE
); 
INSERT INTO producto VALUES('BORRAR', 'Borrar este producto',1.5, 2.5, 12, 0.0, '1752774305001');

CREATE TABLE cliente(
	ident_Cli VARCHAR(13) NOT NULL PRIMARY KEY,
    nom_Cli VARCHAR(10) NOT NULL,
    ape_Cli VARCHAR(15) NOT NULL,
    tel_Cli VARCHAR(10) NULL,
    ema_Cli VARCHAR(30) NULL,
    dir_Cli VARCHAR(60) NULL
);

INSERT INTO cliente VALUES ('1752774305', 'Estela', 'Chipantasi', '0961723454', 'estela@gmail.com', 'Machachi');

INSERT INTO cliente(ident_Cli, nom_Cli, ape_Cli) 
VALUES ('9999999999999', 'CONSUMIDOR', 'FINAL');

CREATE TABLE Usuario(
	ident_Usu VARCHAR(10) PRIMARY KEY NOT NULL,
    nom_Usu VARCHAR(10) NOT NULL,
    ape_Usu VARCHAR(15) NOT NULL,
    ing_Usu DATE NOT NULL,
    tel_Usu VARCHAR(10) NOT NULL,
    ema_Usu VARCHAR(30) NOT NULL,
    FKtipo_rol VARCHAR(3) NULL,
    usuN_Usu VARCHAR(10) NOT NULL,
    pass_Usu VARCHAR(255) NOT NULL,
    FOREIGN KEY (FKtipo_rol) REFERENCES rol(tipo_rol) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO usuario VALUES('1752774305', 'Carlos', 'Mefisto', '2022-06-09', '0987878987', 'carlos@gmail.com', 'adm', 'admin', 'admin');

INSERT INTO usuario VALUES ('1752664471', 'Juan', 'Carlos', '2022-09-13', '0961731484'
, 'juan@gmail.com', 'adm', 'carlos', '123');

CREATE TABLE empresa(
	ruc_Emp VARCHAR(13) PRIMARY KEY,
    nom_Emp VARCHAR(30) NOT NULL,
    tel_Emp VARCHAR(10) NOT NULL,
    ema_Emp VARCHAR(30) NOT NULL,
    dir_Emp VARCHAR(60) NOT NULL
);

INSERT INTO empresa VALUES('DEFAULT', 'DEFAULT', 'DEFAULT', 'DEFAULT', 'DEFAULT');

CREATE TABLE CabFactura(
	num_CF INT8 PRIMARY KEY NOT NULL AUTO_INCREMENT,
    FKruc_Emp VARCHAR(13) NULL,
    FKident_Cli VARCHAR(13) NULL,
    cident_Cli VARCHAR(13) NULL,
    FKident_Usu VARCHAR(10) NULL,
    cident_Usu VARCHAR(10) NULL,
    nom_Usu VARCHAR(10) NULL,
    nomCli_CF VARCHAR(10) NULL,
    apeCli_CF VARCHAR(15) NULL,
    telCli_CF VARCHAR(10) NULL,
    dirCli_CF VARCHAR(60) NULL,
    fecha_CF DATE NULL,
    subT_CF DOUBLE NULL DEFAULT 0.0,
    iva_CF DOUBLE NULL,
    valT_CF DOUBLE NULL DEFAULT 0.0, 
    FOREIGN KEY (FKident_Cli) REFERENCES cliente(ident_Cli) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (FKident_Usu) REFERENCES Usuario(ident_Usu) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (FKruc_Emp) REFERENCES empresa(ruc_Emp) ON DELETE SET NULL ON UPDATE CASCADE 
);

CREATE TABLE detalle(
	id_De INT8 AUTO_INCREMENT PRIMARY KEY,
    FKnum_CF INT8 NOT NULL,
    FKcod_pro VARCHAR(7) NULL,
    codPro_Det VARCHAR(7) NOT NULL,
    detPro_Det VARCHAR(50) NOT NULL,
    cantPro_Det INT2 NOT NULL,
    pUPro DOUBLE NOT NULL,
    descPro DOUBLE NOT NULL,
    valto_det DOUBLE NOT NULL,
    FOREIGN KEY (FKnum_CF) REFERENCES CabFactura(num_CF) ON DELETE CASCADE ,
    FOREIGN KEY (FKcod_pro) REFERENCES Producto(cod_Pro) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE pathPdf(
	id INT2 PRIMARY KEY,
    path_save VARCHAR(255) NULL DEFAULT 'Nothing'
);
INSERT INTO pathPdf(id) VALUES (1);


CREATE TRIGGER trg_reducir_stock
AFTER insert on detalle
FOR EACH ROW
    UPDATE producto SET sto_pro = producto.sto_pro - new.cantPro_Det
    WHERE new.codPro_Det = producto.cod_Pro; 

CREATE TRIGGER trg_update_subT_CF
AFTER insert on detalle
FOR EACH ROW
    UPDATE CabFactura SET subT_CF = (SELECT sum(valto_det) FROM detalle
                                        WHERE FKnum_CF = CabFactura.num_CF
                                        group by (new.FKnum_CF))
    WHERE new.FKnum_CF = CabFactura.num_CF;
    
CREATE TRIGGER trigger_update_subT_CF
AFTER UPDATE on detalle
FOR EACH ROW
    UPDATE CabFactura SET subT_CF = (SELECT sum(valto_det) FROM detalle
                                        WHERE FKnum_CF = CabFactura.num_CF
                                        group by (new.FKnum_CF))
    WHERE new.FKnum_CF = CabFactura.num_CF;
    

CREATE TRIGGER trg_update_default
AFTER UPDATE ON empresa
FOR EACH ROW 
	UPDATE cliente SET tel_Cli = (SELECT tel_Emp from empresa),
    ema_Cli = (SELECT ema_Emp from empresa),
    dir_Cli = (SELECT dir_Emp from empresa)
    WHERE ident_Cli = '9999999999999';
    
CREATE TRIGGER trg_delete_detail
AFTER DELETE ON detalle
FOR EACH ROW 
	UPDATE CabFactura SET subT_CF = (SELECT SUM(valto_det) FROM detalle WHERE FKnum_CF = CabFactura.num_CF GROUP BY (OLD.FKnum_CF));
    

