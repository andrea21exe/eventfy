package com;

import java.time.LocalDate;
import java.time.LocalTime;

public class Recensione {
    LocalDate dataCommento;
    LocalTime oraCommento;
    String commento;
    int voto;
    private static int currentId = 0;
    private int id;

    public Recensione(LocalDate dataCommento, LocalTime oraCommento, String commento, int voto) {
        this.dataCommento = dataCommento;
        this.oraCommento = oraCommento;
        this.commento = commento;
        this.id = currentId++;

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
