package com.example.agroathos.ENTIDADES;

public class E_PersonalTrabajo {
    String id;
    String nombre;
    String jarras;

    public E_PersonalTrabajo(String id, String nombre, String jarras) {
        this.id = id;
        this.nombre = nombre;
        this.jarras = jarras;
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

}
