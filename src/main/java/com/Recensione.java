package com;

import java.time.LocalDate;
import java.time.LocalTime;

import com.eventfy.Artista;

public class Recensione {
    LocalDate dataCommento;
    LocalTime oraCommento;
    String commento;
    int voto;
    private static int currentId = 0;
    private int id;
    Artista artista;

    public Recensione(LocalDate dataCommento, LocalTime oraCommento, String commento, int voto, Artista artista) {
        this.dataCommento = dataCommento;
        this.oraCommento = oraCommento;
        this.commento = commento;
        this.id = currentId++;
        this.artista = artista;

        if (voto < 0 && voto>5) {
            throw new IllegalArgumentException("Il valore deve essere compreso tra 0 a 5");
        }
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
    

}
