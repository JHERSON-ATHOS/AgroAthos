package com.example.agroathos.ENTIDADES;

public class E_PersonalTrabajo {
    String id;
    String nombre;
    String jarras;
    String hora_inicio;
    String hora_final;

    public E_PersonalTrabajo(String id, String nombre, String jarras, String hora_inicio, String hora_final) {
        this.id = id;
        this.nombre = nombre;
        this.jarras = jarras;
        this.hora_inicio = hora_inicio;
        this.hora_final = hora_final;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getJarras() {
        return jarras;
    }

    public void setJarras(String jarras) {
        this.jarras = jarras;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }
}
