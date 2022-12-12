package com.upm.refreshme.backend;

import java.util.Date;

public class WebPage {

    private String nombre;
    private String url;
    private String categoria;
    private Date ultimosCambios;

    public WebPage(String nombre, String url, String categoria, Date ultimosCambios) {
        this.nombre = nombre;
        this.url = url;
        this.categoria = categoria;
        this.ultimosCambios = ultimosCambios;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public String getCategoria() {
        return categoria;
    }

    public Date getUltimosCambios() {
        return ultimosCambios;
    }
}
