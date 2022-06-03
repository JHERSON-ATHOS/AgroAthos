package com.example.agroathos.ENTIDADES;

public class E_Supervisor {
    String id;
    String zona;
    String fundo;
    String cultivo;
    String dni;

    public E_Supervisor(String id, String zona, String fundo, String cultivo, String dni) {
        this.id = id;
        this.zona = zona;
        this.fundo = fundo;
        this.cultivo = cultivo;
        this.dni = dni;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getFundo() {
        return fundo;
    }

    public void setFundo(String fundo) {
        this.fundo = fundo;
    }

    public String getCultivo() {
        return cultivo;
    }

    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
