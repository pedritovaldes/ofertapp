package com.example.pedro.ofertapp.model;

import java.io.Serializable;

/**
 * Created by Pedro on 04/12/2016.
 */
public class User implements Serializable{

    private Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private String descripcion;
    private String pass;

    public User(Integer id, String nombre, String email, String telefono, String descripcion, String pass) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.pass = pass;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
