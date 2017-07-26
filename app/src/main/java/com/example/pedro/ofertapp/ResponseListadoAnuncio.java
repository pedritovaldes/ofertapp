package com.example.pedro.ofertapp;

import com.example.pedro.ofertapp.model.ListadoAnuncios;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedro on 26/07/2017.
 */
public class ResponseListadoAnuncio {

    private List<ListadoAnuncios> anuncios = new ArrayList<ListadoAnuncios>();

    public List<ListadoAnuncios> getListadoAnuncios() {
        return anuncios;
    }
}
