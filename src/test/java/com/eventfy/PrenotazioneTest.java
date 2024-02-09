package com.eventfy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
                LocalTime.now(), (Artista) a, i);

    }

    @Test
    void testAddBrano() {

        try {
            p.addBrano(0);
        } catch (Exception e) {
            fail();
        }
        assertTrue(p.hasBrano(0));


        //provo ad aggiungere lo stesso brano

        try {
            p.addBrano(0);
        } catch (Exception e) {
            assertEquals("Brano già inserito", e.getMessage());
        }

        //provo ad aggiunger eun codice brano che non esiste

        try {
            p.addBrano(99);
        } catch (Exception e) {
            assertEquals("Id brano non valido", e.getMessage());
        }

    }

    @Test
    void testCreaRecensioneArtista() {
        try {
            p.creaRecensione("Impianto bello", 4);
        } catch (Exception e) {
            fail();
        }
        // Prendo il commento appena inserito
        RecensioneImpianto r = p.getImpianto().getListaRecensioni().get(0);
        assertEquals("Impianto bello", r.getCommento());
        assertEquals(4, r.getVoto());
        assertEquals(p.getArtista(), r.getUtente());


        //considero il caso in cui inserisco una recensione già inserita

        try {
            p.creaRecensione("Impianto bello", 4);
        } catch (Exception e) {
            assertEquals("E' già stato recensito l'impianto per questa prenotazione", e.getMessage());
        }

    }

}
