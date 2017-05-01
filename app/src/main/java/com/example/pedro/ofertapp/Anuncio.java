package com.example.pedro.ofertapp;

/**
 * Created by Pedro on 01/05/2017.
 */
public class Anuncio {

    private Integer id;
    private String titulo;
    private Integer user_id;
    private String provincia;
    private String sector_profesional;
    private Integer precio_max;
    private String descripcion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getSector_profesional() {
        return sector_profesional;
    }

    public void setSector_profesional(String sector_profesional) {
        this.sector_profesional = sector_profesional;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Integer getPrecio_max() {
        return precio_max;
    }

    public void setPrecio_max(Integer precio_max) {
        this.precio_max = precio_max;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
