package com.example.projetofinalmobile;

public class AudioItem {
    private String nomeArquivo;
    private int duracaoSegundos;

    public AudioItem(String nomeArquivo, int duracaoSegundos) {
        this.nomeArquivo = nomeArquivo;
        this.duracaoSegundos = duracaoSegundos;
    }

    public String getNomeArquivo() { return nomeArquivo; }
    public int getDuracaoSegundos() { return duracaoSegundos; }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
}