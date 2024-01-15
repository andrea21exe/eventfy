package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;



public class Invito {
    private static int currentId = 0;
    private int id;
    private LocalDate data;
    private LocalTime ora;
    private Evento evento;


    public Invito(Evento evento) {
        this.id = currentId++;
        this.data = LocalDate.now();
        this.ora = LocalTime.now();
        this.evento=evento;
    }

    @Override
    public String toString() {
        return "Invito [id=" + id +", data=" + data + ", ora=" + ora +"Evento"+evento+"]";
    }


}
