package com.example.agroathos.BD_SQLITE.UTILIDADES;

public class Utilidades {

    public static final int VERSION_APP = 7;

    //DATOS NIVEL1
    public static final String TABLA_NIVEL1 = "RRHH_NIVEL1";
    public static final String CAMPO_ID_NIVEL1 = "id";
    public static final String CAMPO_IDNIVEL1_NIVEL1 = "id_nivel_uno";
    public static final String CAMPO_ZONA_NIVEL1 = "zona";
    public static final String CAMPO_FUNDO_NIVEL1 = "fundo";
    public static final String CAMPO_CULTIVO_NIVEL1 = "cultivo";
    public static final String CAMPO_DNI_NIVEL1 = "dni";
    public static final String CAMPO_FECHA_NIVEL1 = "fecha";
    public static final String CAMPO_HORA_NIVEL1 = "hora";
    public static final String CAMPO_SINCRONIZADO_NIVEL1 = "sincronizado";
    public static final String CREAR_TABLA_NIVEL1 = "CREATE TABLE "+TABLA_NIVEL1+" " +
            "("+CAMPO_ID_NIVEL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_IDNIVEL1_NIVEL1+" TEXT, "
            +CAMPO_ZONA_NIVEL1+" TEXT, " + ""+CAMPO_FUNDO_NIVEL1+" TEXT, "+CAMPO_CULTIVO_NIVEL1+" TEXT, "
            +CAMPO_DNI_NIVEL1+" TEXT, " +CAMPO_FECHA_NIVEL1+" TEXT, "+CAMPO_HORA_NIVEL1+" TEXT, "+CAMPO_SINCRONIZADO_NIVEL1+" TEXT)";

    //DATOS NIVEL 1.5
    public static final String TABLA_NIVEL1_5 = "RRHH_NIVEL1_5";
    public static final String CAMPO_ID_NIVEL1_5 = "id";
    public static final String CAMPO_ANEXONIVEL1_NIVEL1_5 = "anexo_nivel1";
    public static final String CAMPO_ID_GRUPO_NIVEL1_5 = "id_grupo";
    public static final String CAMPO_CONTADOR_GRUPO_NIVEL1_5 = "contador";
    public static final String CAMPO_DNI_NIVEL1_5 = "anexo_supervisor";
    public static final String CAMPO_ESTADO_NIVEL1_5 = "estado";
    public static final String CAMPO_SINCRONIZADO_NIVEL1_5 = "sincronizado";
    public static final String CREAR_TABLA_NIVEL1_5 = "CREATE TABLE "+TABLA_NIVEL1_5+" " +
            "("+CAMPO_ID_NIVEL1_5+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_ANEXONIVEL1_NIVEL1_5+
            " TEXT, " +CAMPO_ID_GRUPO_NIVEL1_5+" TEXT, " + ""+CAMPO_CONTADOR_GRUPO_NIVEL1_5+
            " TEXT, "+CAMPO_DNI_NIVEL1_5+" TEXT, "+CAMPO_ESTADO_NIVEL1_5+" TEXT, "+ CAMPO_SINCRONIZADO_NIVEL1_5+" TEXT)";

    //DATOS NIVEL 2
    public static final String TABLA_NIVEL2 = "RRHH_NIVEL2";
    public static final String CAMPO_ID_NIVEL2 = "id";
    public static final String CAMPO_ANEXO_GRUPO_NIVEL2 = "id_grupo";
    public static final String CAMPO_FUNDO_NIVEL2 = "fundo";
    public static final String CAMPO_MODULO_NIVEL2 = "modulo";
    public static final String CAMPO_LOTE_NIVEL2 = "lote";
    public static final String CAMPO_LABOR_NIVEL2 = "labor";
    public static final String CAMPO_PERSONAL_NIVEL2 = "personal";
    public static final String CAMPO_DNI_NIVEL2 = "dni";
    public static final String CAMPO_FECHA_NIVEL2 = "fecha";
    public static final String CAMPO_HORA_INICIO_NIVEL2 = "hora_inicio";
    public static final String CAMPO_HORA_FIN_NIVEL2 = "hora_fin";
    public static final String CAMPO_ESTADO_NIVEL2 = "estado";
    public static final String CAMPO_SINCRONIZADO_NIVEL2 = "sincronizado";
    public static final String CREAR_TABLA_NIVEL2 = "CREATE TABLE "+TABLA_NIVEL2+" " +
            "("+CAMPO_ID_NIVEL2+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_ANEXO_GRUPO_NIVEL2+" TEXT,"
            +CAMPO_FUNDO_NIVEL2+" TEXT, "+CAMPO_MODULO_NIVEL2 +" TEXT, "+CAMPO_LOTE_NIVEL2+" TEXT, "
            +CAMPO_LABOR_NIVEL2+" TEXT, "+CAMPO_PERSONAL_NIVEL2+" TEXT, "+CAMPO_DNI_NIVEL2+" TEXT, "
            +CAMPO_FECHA_NIVEL2+" TEXT, "+CAMPO_HORA_INICIO_NIVEL2+" TEXT, "+CAMPO_HORA_FIN_NIVEL2+" TEXT, "
            +CAMPO_ESTADO_NIVEL2+" TEXT, " +CAMPO_SINCRONIZADO_NIVEL2+" TEXT)";

    //DATOS DE DESTAJO
    public static final String TABLA_DESTAJO_NIVEL1 = "RRHH_DESTAJO";
    public static final String CAMPO_DESTAJO_ID_NIVEL1 = "id";
    public static final String CAMPO_DESTAJO_FECHA_NIVEL1 = "fecha";
    public static final String CAMPO_DESTAJO_HORA_NIVEL1 = "hora";
    public static final String CAMPO_DESTAJO_JARRA_NIVEL1 = "Jarra";
    public static final String CAMPO_DESTAJO_DNI_NIVEL1 = "Dni";
    public static final String CAMPO_DESTAJO_SINCRONIZADO_NIVEL1 = "sincronizado";
    public static final String CREAR_DESTAJO_TABLA_NIVEL1 = "CREATE TABLE "+TABLA_DESTAJO_NIVEL1+"" +
            " ("+CAMPO_DESTAJO_ID_NIVEL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_DESTAJO_FECHA_NIVEL1+" " +
            "TEXT, "+CAMPO_DESTAJO_HORA_NIVEL1+" TEXT, "+CAMPO_DESTAJO_JARRA_NIVEL1+" TEXT, "+CAMPO_DESTAJO_DNI_NIVEL1+" TEXT, "+
            CAMPO_DESTAJO_SINCRONIZADO_NIVEL1+" TEXT)";

    //DATOS DE GARITA BUS
    public static final String TABLA_GARITA_NIVEL1 = "TRANSPORTE_GARITA_BUS";
    public static final String CAMPO_GARITA_ID_NIVEL1 = "id";
    public static final String CAMPO_GARITA_ANEXO_PLACA_NIVEL1 = "anexo_placa";
    public static final String CAMPO_GARITA_ZONA_NIVEL1 = "zona";
    public static final String CAMPO_GARITA_FUNDO_NIVEL1 = "fundo";
    public static final String CAMPO_GARITA_PERSONAL_NIVEL1 = "personal";
    public static final String CAMPO_GARITA_FECHA_NIVEL1 = "fecha";
    public static final String CAMPO_GARITA_HORA_NIVEL1 = "hora";
    public static final String CAMPO_GARITA_SINCRONIZADO_NIVEL1 = "sincronizado";
    public static final String CREAR_GARITA_TABLA_NIVEL1 = "CREATE TABLE "+TABLA_GARITA_NIVEL1+"" +
            " ("+CAMPO_GARITA_ID_NIVEL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_GARITA_ANEXO_PLACA_NIVEL1+" " +
            "TEXT, "+CAMPO_GARITA_ZONA_NIVEL1+" TEXT, "+CAMPO_GARITA_FUNDO_NIVEL1+" TEXT, "+CAMPO_GARITA_PERSONAL_NIVEL1+
            " TEXT, "+CAMPO_GARITA_FECHA_NIVEL1+" TEXT, "+CAMPO_GARITA_HORA_NIVEL1+" TEXT, "+CAMPO_GARITA_SINCRONIZADO_NIVEL1+" TEXT)";

    //DATOS DE GARITA BUS INTERMEDIO
    public static final String TABLA_GARITA_NIVEL1_5 = "TRANSPORTE_GARITA_BUS_PLACA";
    public static final String CAMPO_GARITA_ID_NIVEL1_5 = "id";
    public static final String CAMPO_GARITA_ANEXO_PERSONAL_NIVEL1_5 = "anexo_personal";
    public static final String CAMPO_GARITA_CONTADOR_NIVEL1_5 = "contador";
    public static final String CAMPO_GARITA_PLACA_NIVEL1_5 = "placa";
    public static final String CAMPO_GARITA_SINCRONIZADO_NIVEL1_5 = "sincronizado";
    public static final String CREAR_GARITA_TABLA_NIVEL1_5 = "CREATE TABLE "+TABLA_GARITA_NIVEL1_5+"" +
            " ("+CAMPO_GARITA_ID_NIVEL1_5+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_GARITA_ANEXO_PERSONAL_NIVEL1_5+" " +
            "TEXT, "+CAMPO_GARITA_CONTADOR_NIVEL1_5+" TEXT, "+CAMPO_GARITA_PLACA_NIVEL1_5+" TEXT, "+CAMPO_GARITA_SINCRONIZADO_NIVEL1_5+" TEXT)";

    //DATOS DE GARITA PERSONAL
    public static final String TABLA_GARITA_NIVEL2 = "TRANSPORTE_GARITA_PERSONAL";
    public static final String CAMPO_GARITA_ID_NIVEL2 = "id";
    public static final String CAMPO_GARITA_ZONA_NIVEL2 = "zona";
    public static final String CAMPO_GARITA_FUNDO_NIVEL2 = "fundo";
    public static final String CAMPO_GARITA_PERSONAL_NIVEL2 = "personal";
    public static final String CAMPO_GARITA_TIPO_HORA_NIVEL2 = "tipo_hora";
    public static final String CAMPO_GARITA_FECHA_NIVEL2 = "fecha";
    public static final String CAMPO_GARITA_HORA_NIVEL2 = "hora";
    public static final String CAMPO_GARITA_SINCRONIZADO_NIVEL2 = "sincronizado";
    public static final String CREAR_GARITA_TABLA_NIVEL2 = "CREATE TABLE "+TABLA_GARITA_NIVEL2+"" +
            " ("+CAMPO_GARITA_ID_NIVEL2+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_GARITA_ZONA_NIVEL2+" " +
            "TEXT, "+CAMPO_GARITA_FUNDO_NIVEL2+" TEXT, "+CAMPO_GARITA_PERSONAL_NIVEL2+" TEXT, "+CAMPO_GARITA_TIPO_HORA_NIVEL2+
            " TEXT, "+CAMPO_GARITA_FECHA_NIVEL2+" TEXT, "+CAMPO_GARITA_HORA_NIVEL2+" TEXT, "+CAMPO_GARITA_SINCRONIZADO_NIVEL2+" TEXT)";

    //DATOS DE GARITA UNIDAD
    public static final String TABLA_GARITA_NIVEL3 = "TRANSPORTE_GARITA_UNIDAD";
    public static final String CAMPO_GARITA_ID_NIVEL3 = "id";
    public static final String CAMPO_GARITA_ZONA_NIVEL3 = "zona";
    public static final String CAMPO_GARITA_FUNDO_NIVEL3 = "fundo";
    public static final String CAMPO_GARITA_PERSONAL_NIVEL3 = "personal";
    public static final String CAMPO_GARITA_TIPO_HORA_NIVEL3 = "tipo_hora";
    public static final String CAMPO_GARITA_FECHA_NIVEL3 = "fecha";
    public static final String CAMPO_GARITA_HORA_NIVEL3 = "hora";
    public static final String CAMPO_GARITA_SINCRONIZADO_NIVEL3 = "sincronizado";
    public static final String CREAR_GARITA_TABLA_NIVEL3 = "CREATE TABLE "+TABLA_GARITA_NIVEL3+"" +
            " ("+CAMPO_GARITA_ID_NIVEL3+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_GARITA_ZONA_NIVEL3+" " +
            "TEXT, "+CAMPO_GARITA_FUNDO_NIVEL3+" TEXT, "+CAMPO_GARITA_PERSONAL_NIVEL3+" TEXT, "+CAMPO_GARITA_TIPO_HORA_NIVEL3+
            " TEXT,"+CAMPO_GARITA_FECHA_NIVEL3+" TEXT, "+CAMPO_GARITA_HORA_NIVEL3+" TEXT, "+CAMPO_GARITA_SINCRONIZADO_NIVEL3+" TEXT)";

    //DATOS TAREO PLANTA
    public static final String TABLA_TAREO_PLANTA_NIVEL1 = "RRHH_TAREO_PLANTA_NIVEL1";
    public static final String CAMPO_ID_TAREO_PLANTA_NIVEL1 = "id";
    public static final String CAMPO_IDNIVEL1_TAREO_PLANTA_NIVEL1 = "id_nivel_uno";
    public static final String CAMPO_NAVE_TAREO_PLANTA_NIVEL1 = "nave";
    public static final String CAMPO_LINEA_TAREO_PLANTA_NIVEL1 = "linea";
    public static final String CAMPO_TURNO_TAREO_PLANTA_NIVEL1 = "turno";
    public static final String CAMPO_DNI_TAREO_PLANTA_NIVEL1 = "dni";
    public static final String CAMPO_FECHA_TAREO_PLANTA_NIVEL1 = "fecha";
    public static final String CAMPO_HORA_TAREO_PLANTA_NIVEL1 = "hora";
    public static final String CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL1 = "sincronizado";
    public static final String CREAR_TABLA_TAREO_PLANTA_NIVEL1 = "CREATE TABLE "+TABLA_TAREO_PLANTA_NIVEL1+" " +
            "("+CAMPO_ID_TAREO_PLANTA_NIVEL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_IDNIVEL1_TAREO_PLANTA_NIVEL1+" TEXT, "
            +CAMPO_NAVE_TAREO_PLANTA_NIVEL1+" TEXT, " + ""+CAMPO_LINEA_TAREO_PLANTA_NIVEL1+" TEXT, "+CAMPO_TURNO_TAREO_PLANTA_NIVEL1+" TEXT, "
            +CAMPO_DNI_TAREO_PLANTA_NIVEL1+" TEXT, " +CAMPO_FECHA_TAREO_PLANTA_NIVEL1+" TEXT, "+CAMPO_HORA_TAREO_PLANTA_NIVEL1+" TEXT, "
            +CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL1+" TEXT)";

    //DATOS TAREO PLANTA NIVEL 2
    public static final String TABLA_TAREO_PLANTA_NIVEL2 = "RRHH_TAREO_PLANTA_NIVEL2";
    public static final String CAMPO_ID_TAREO_PLANTA_NIVEL2 = "id";
    public static final String CAMPO_ANEXONIVEL1_TAREO_PLANTA_NIVEL2 = "anexo_nivel1";
    public static final String CAMPO_ID_GRUPO_TAREO_PLANTA_NIVEL2 = "id_grupo";
    public static final String CAMPO_CONTADOR_GRUPO_TAREO_PLANTA_NIVEL2 = "contador";
    public static final String CAMPO_DNI_TAREO_PLANTA_NIVEL2 = "anexo_supervisor";
    public static final String CAMPO_ESTADO_TAREO_PLANTA_NIVEL2 = "estado";
    public static final String CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL2 = "sincronizado";
    public static final String CREAR_TABLA_TAREO_PLANTA_NIVEL2 = "CREATE TABLE "+TABLA_TAREO_PLANTA_NIVEL2+" " +
            "("+CAMPO_ID_TAREO_PLANTA_NIVEL2+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_ANEXONIVEL1_TAREO_PLANTA_NIVEL2+
            " TEXT, " +CAMPO_ID_GRUPO_TAREO_PLANTA_NIVEL2+" TEXT, " + ""+CAMPO_CONTADOR_GRUPO_TAREO_PLANTA_NIVEL2+
            " TEXT, "+CAMPO_DNI_TAREO_PLANTA_NIVEL2+" TEXT, "+CAMPO_ESTADO_TAREO_PLANTA_NIVEL2+" TEXT, "+
            CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL2+" TEXT)";

    //DATOS NIVEL 3
    public static final String TABLA_TAREO_PLANTA_NIVEL3 = "RRHH_TAREO_PLANTA_NIVEL3";
    public static final String CAMPO_ID_TAREO_PLANTA_NIVEL3 = "id";
    public static final String CAMPO_ANEXO_GRUPO_TAREO_PLANTA_NIVEL3 = "id_grupo";
    public static final String CAMPO_PROCESO_TAREO_PLANTA_NIVEL3 = "proceso";
    public static final String CAMPO_ACTIVIDAD_TAREO_PLANTA_NIVEL3 = "actividad";
    public static final String CAMPO_LABOR_TAREO_PLANTA_NIVEL3 = "labor";
    public static final String CAMPO_MESA_TAREO_PLANTA_NIVEL3 = "mesa";
    public static final String CAMPO_DNI_TAREO_PLANTA_NIVEL3 = "dni";
    public static final String CAMPO_QRPERSONAL_TAREO_PLANTA_NIVEL3 = "qr_personal";
    public static final String CAMPO_FECHA_TAREO_PLANTA_NIVEL3 = "fecha";
    public static final String CAMPO_HORA_TAREO_PLANTA_NIVEL3 = "hora";
    public static final String CAMPO_ESTADO_TAREO_PLANTA_NIVEL3 = "estado";
    public static final String CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL3 = "sincronizado";
    public static final String CREAR_TABLA_TAREO_PLANTA_NIVEL3 = "CREATE TABLE "+TABLA_TAREO_PLANTA_NIVEL3+" " +
            "("+CAMPO_ID_TAREO_PLANTA_NIVEL3+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_ANEXO_GRUPO_TAREO_PLANTA_NIVEL3+" TEXT,"
            +CAMPO_PROCESO_TAREO_PLANTA_NIVEL3+" TEXT, "+CAMPO_ACTIVIDAD_TAREO_PLANTA_NIVEL3 +" TEXT, "+CAMPO_LABOR_TAREO_PLANTA_NIVEL3+
            " TEXT, "+CAMPO_MESA_TAREO_PLANTA_NIVEL3+" TEXT, "+CAMPO_DNI_TAREO_PLANTA_NIVEL3+" TEXT, "+CAMPO_QRPERSONAL_TAREO_PLANTA_NIVEL3
            +" TEXT, "+CAMPO_FECHA_TAREO_PLANTA_NIVEL3+ " TEXT, " +CAMPO_HORA_TAREO_PLANTA_NIVEL3+" TEXT, "+CAMPO_ESTADO_TAREO_PLANTA_NIVEL3
            +" TEXT, "+CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL3+" TEXT)";
}
