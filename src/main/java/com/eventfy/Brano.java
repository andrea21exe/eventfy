package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;

public class Brano {
    private static int currentId = 0;

    private final int id;
    private String titolo;
    private String album;
    private int durata;
    private Artista artista;

     public Brano(String titolo, String album, int durata, Artista artista) {
        this.titolo = titolo;
        this.album = album;
        this.durata = durata;
        this.artista = artista;
        this.id = currentId++;
    }

    @Override
    public String toString() {
        return "Brano [id=" + id + ", titolo=" + titolo + ", album=" + album + ", durata=" + durata + ", artista="
                + artista + "]";
    }

}
