package com.example.appguitarrasfirebase;

public class DtoGuitarra {
    private String modelo;
    private String marca;
    private String cor;
    private String imagem;

    public DtoGuitarra(){};

    public DtoGuitarra(String modelo, String marca, String cor, String imagem) {
        this.modelo = modelo;
        this.marca = marca;
        this.cor = cor;
        this.imagem = imagem;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return marca+" "+modelo+" "+cor;
    }
}
