package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;



public class Invito {
    private static int currentId = 0;
    private int id;
    private LocalDate data;
    private LocalTime ora;
    private Evento evento;
    private Utente artistaMittente;
    private Utente artistaInvitato;

    public Invito(Evento evento, Utente artistaMittente, Utente artistaInvitato) {
        this.id = currentId++;
        this.data = LocalDate.now();
        this.ora = LocalTime.now();
        this.evento=evento;
        this.artistaMittente=artistaMittente;
        this.artistaInvitato=artistaInvitato;
    }

    public int getId() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "Invito [id=" + id +", data=" + data + ", ora=" + ora +"Evento"+evento+", artistaMittente"+artistaMittente+", artistaInvitato"+artistaInvitato+"]";
    }


}
