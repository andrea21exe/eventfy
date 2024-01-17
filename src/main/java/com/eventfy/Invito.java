package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;



public class Invito {
    private static int currentId = 0;
    private int id;
    private LocalDate data;
    private LocalTime ora;
    private Evento evento;
    private Artista artistaMittente;
    private Artista artistaDestinatario;

    public Invito(Evento evento, Artista artistaMittente, Artista artistaDestinatario) {
        this.id = currentId++;
        this.data = LocalDate.now();
        this.ora = LocalTime.now();
        this.evento=evento;
        this.artistaMittente=artistaMittente;
        this.artistaDestinatario=artistaDestinatario;
    }

    public Invito(Evento evento, Artista artistaMittente) {
        this.id = currentId++;
        this.data = LocalDate.now();
        this.ora = LocalTime.now();
        this.evento=evento;
        this.artistaMittente=artistaMittente;
    }

    public void setDestinatario(Artista destinatario){
        this.artistaDestinatario = destinatario;
    }


    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Invito [id=" + id + ", data=" + data + ", ora=" + ora + ", evento=" + evento + ", artistaMittente="
                + artistaMittente + ", artistaDestinatario=" + artistaDestinatario + "]";
    }

}
