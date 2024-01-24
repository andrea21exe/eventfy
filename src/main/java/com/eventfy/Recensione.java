package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;

public class Recensione {
    private static int currentId = 0;

    private int id;
    private LocalDate dataCommento;
    private LocalTime oraCommento;
    private String commento;
    private int voto;
    private Artista artista;

    public Recensione(LocalDate dataCommento, LocalTime oraCommento, String commento, int voto, Artista artista) {

        if (voto < 0 || voto > 5) {
            throw new IllegalArgumentException("Il valore deve essere compreso tra 0 a 5");
        }

        this.dataCommento = dataCommento;
        this.oraCommento = oraCommento;
        this.commento = commento;
        this.id = currentId++;
        this.artista = artista;
        this.voto = voto;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Recensione [dataCommento=" + dataCommento + ", oraCommento=" + oraCommento + ", commento=" + commento
                + ", voto=" + voto + ", id=" + id + "]";
    }

    public String getCommento() {
        return this.commento;
    }

    public int getVoto() {
        return this.voto;
    }

    public LocalDate getData() {
        return this.dataCommento;
    }

    public Artista getArtista() {
        return this.artista;
    }
}
