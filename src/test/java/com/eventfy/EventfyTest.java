package com.eventfy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EventfyTest {

    static Eventfy eventfy;

    @BeforeAll
    public static void initTest() {

        eventfy = Eventfy.getIstanceEventfy();
        
        //eventfy.setUtenteCorrente(new Gestore("andrea"));
        //eventfy.setImpiantoCorrente(new Impianto("Impianto1", "Luogo1", 100, 200, eventfy.getUtenteCorrente()));
        //eventfy.setPrenotazioneCorrente(new Prenotazione("Prenotazione1", "Descrizione prenotazione 1", 2, 130, LocalDate.now(), LocalTime.now(), new Artista("Massi"), eventfy.getImpiantoCorrente()));
    }
 
    @Test
    void testNuovoImpianto() {

        /*try{
            eventfy.nuovoImpianto("null", "null", 0, 0);
            fail("Non hai messo niente");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Non va");
        }*/

        try {
            assertNull(eventfy.getImpiantoCorrente());
            eventfy.nuovoImpianto("San Siro", "Milano", 80000, 100);
            assertNotNull(eventfy.getImpiantoCorrente());
        } catch (Exception e) {
            fail("Errore");
        }

    }

    @Test
    void testConfermaImpianto() {

        assertNull(eventfy.getImpiantoCorrente()); 

        Impianto nuovoImpianto = eventfy.nuovoImpianto("San Siro", "Milano", 80000, 100);
        eventfy.confermaImpianto();

        assertNull(eventfy.getImpiantoCorrente()); 
        assertEquals(1, eventfy.getListaImpianti().size());
        assertTrue(eventfy.getListaImpianti().contains(nuovoImpianto)); 
        
    }  
    @Test
    void testNuovaPrenotazione() {

        eventfy.setUtenteCorrente(new Artista("andrea"));
        eventfy.setImpiantoCorrente(new Impianto("Impianto1", "Luogo1", 100, 200, new Gestore("ciao")));

        eventfy.confermaImpianto();

        List<Impianto> impiantiDisponibili = eventfy.nuovaPrenotazione("Prenotazione1","Descrizione prenotazione 1", 2,130, LocalDate.now(), LocalTime.now());

        assertTrue(!impiantiDisponibili.isEmpty(), "La lista degli impianti disponibili non deve essere vuota");

        assertEquals(1, impiantiDisponibili.size(), "La lista degli impianti disponibili deve contenere 1 elemento");

        assertNotNull(eventfy.getPrenotazioneCorrente(), "La prenotazione corrente non deve essere nulla");

    }
    @Test
    void testPrenotaImpianto() {

        eventfy.setUtenteCorrente(new Artista("andrea"));
        eventfy.setImpiantoCorrente(new Impianto("Impianto1", "Luogo1", 100, 200, new Gestore("ciao")));

        List<Impianto> impiantiDisponibili = eventfy.nuovaPrenotazione("Prenotazione1","Descrizione prenotazione 1", 2,130, LocalDate.now(), LocalTime.now());

        int codice_impianto = eventfy.getImpiantoCorrente().getId();

        eventfy.confermaImpianto();

        eventfy.prenotaImpianto(codice_impianto);


        //assertEquals(eventfy.getPrenotazioneCorrente().getImpianto().getId(),codice_impianto, "L'impianto corrente deve corrispondere all'impianto selezionato");

        assertNotNull(eventfy.getPrenotazioneCorrente(), "La prenotazione corrente non deve essere nulla");
    }

    @Test
    void testConfermaPrenotazione() {
        
    }

}
