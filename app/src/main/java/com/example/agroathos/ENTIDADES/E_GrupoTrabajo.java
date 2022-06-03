package com.example.agroathos.ENTIDADES;

public class E_GrupoTrabajo {
    long id;
    String id_supervisor;
    String fundo;
    String modulo;
    String lote;
    String labor;
    String dni;

    public E_GrupoTrabajo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_supervisor() {
        return id_supervisor;
    }

    public void setId_supervisor(String id_supervisor) {
        this.id_supervisor = id_supervisor;
    }

    public String getFundo() {
        return fundo;
    }

    public void setFundo(String fundo) {
        this.fundo = fundo;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getLabor() {
        return labor;
    }

    public void setLabor(String labor) {
        this.labor = labor;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
