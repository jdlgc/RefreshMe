package com.upm.refreshme.backend;

public class WebPage {

    private String nombre;
    private String url;
    private String categoria;
    private String ultimosCambios;

    public WebPage(String nombre, String url, String categoria, String ultimosCambios) {
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

    public String getUltimosCambios() {
        return ultimosCambios;
    }
}
