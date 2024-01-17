package com.eventfy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PrenotazioneTest {

    private static Eventfy eventfy;
    private static Prenotazione p;

    @BeforeAll
    public static void initTest() {

        eventfy = Eventfy.getIstanceEventfy();
        eventfy.populate();

        Utente a = eventfy.getUtente(3);
        Impianto i = eventfy.getImpianto(0);
 
        p = new Prenotazione("Evento Prova", "Un evento di prova ", 10, LocalDate.now(), 
                                LocalTime.now(), (Artista)a, i);

    }


    @Test
    void testAddBrano() {

        Brano b = new Brano("Take My Breath", "Dawn FM", 4);
        p.addBrano(b);     
        assertTrue(p.getEvento().getListaBrani().contains(b));  

    }
}
