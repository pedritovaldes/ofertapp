package com.example.pedro.ofertapp.model;

/**
 * Created by Pedro on 26/07/2017.
 */
public class ListadoAnuncios {

    private Integer id;
    private String titulo;
    private Integer user_id;
    private String provincia;
    private String sector_profesional;
    private Integer precio_maximo;
    private String descripcion;
    private String name;
    private String email;
    private String telefono;

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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getSector_profesional() {
        return sector_profesional;
    }

    public void setSector_profesional(String sector_profesional) {
        this.sector_profesional = sector_profesional;
    }

    public Integer getPrecio_maximo() {
        return precio_maximo;
    }

    public void setPrecio_maximo(Integer precio_maximo) {
        this.precio_maximo = precio_maximo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
