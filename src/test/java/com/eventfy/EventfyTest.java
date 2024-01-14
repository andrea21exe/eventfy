package com.eventfy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        eventfy.populate();
    }

    @Test
    void testNuovoImpianto() {

        //Login - SignUp
        eventfy.signUpLogIn(new Gestore("Carlo"));

        assertNull(eventfy.getImpiantoCorrente());
        eventfy.nuovoImpianto("San Siro", "Milano", 80000, 100);
        assertNotNull(eventfy.getImpiantoCorrente());


    }

    @Test
    void testConfermaImpianto() {

         //Login - SignUp
        eventfy.signUpLogIn(new Gestore("Carlo"));

        Impianto nuovoImpianto = eventfy.nuovoImpianto("Metro Stad", "Milano", 80000, 100);
        eventfy.confermaImpianto();

        assertNull(eventfy.getImpiantoCorrente());
        assertEquals(5, eventfy.getListaImpianti().size());
        assertTrue(eventfy.getListaImpianti().contains(nuovoImpianto));

    }

    @Test
    void testNuovaPrenotazione() {

         //Login - SignUp
        eventfy.signUpLogIn(new Artista("Metro Boomin"));
        
        assertNull(eventfy.getPrenotazioneCorrente());

        //Testo implicitamente il metodo isMaggioreUguale della classe Impianto
        List<Impianto> impiantiDisponibili = eventfy.nuovaPrenotazione("Prenotazione1", "Descrizione 1", 2,
                15000, LocalDate.now(), LocalTime.now());
        assertEquals(3, impiantiDisponibili.size());

        assertNotNull(eventfy.getPrenotazioneCorrente());

    }

    @Test
    void testPrenotaImpianto() {

        //Login - SignUp
        eventfy.signUpLogIn(new Artista("Dua Lipa"));

        eventfy.nuovaPrenotazione("P1", "d1", 329, 20, LocalDate.now(), LocalTime.now());
        //SCELGO L'IMPIANTO CON ID = 0
        eventfy.prenotaImpianto(0); 
        //L'impianto scelto deve essere associato alla prenotazione corrente
        assertEquals(eventfy.getImpianto(0), eventfy.getPrenotazioneCorrente().getImpianto()); 

        // QUANDO FAREMO LE ESTENSIONI PROVEREMO A SELEZIONARE UN IMPIANTO CHE 
        // NON E' NELLA MAPPA TEMPORANEA (CHE NON ESISTE O NON SODDISFA I REQUISITI DI CAPIENZA)
        /* 
        impiantiDisponibili = eventfy.nuovaPrenotazione("P2", "d2", 329, 10000, LocalDate.now(), LocalTime.now());
        //SCELGO L'IMPIANTO CON ID = 0
        eventfy.prenotaImpianto(1); 
        */
    }

    @Test
    void testConfermaPrenotazione() {

        eventfy.signUpLogIn(new Artista("Dua Lipa"));

        //Ottengo la dimensione della mappa delle prenotazioni pendenti prima di confermare una nuova prenotazione
        int mappaPrenotazioniPendenti_size = eventfy.getPrenotazioniPendenti().size();
        
        eventfy.nuovaPrenotazione("P1", "d1", 329, 20, LocalDate.now(), LocalTime.now());
        //SCELGO L'IMPIANTO CON ID = 3
        eventfy.prenotaImpianto(3); 
        //Controllo che Prenotazionecorrente NON sia null
        assertNotNull(eventfy.getPrenotazioneCorrente());
        //Confermo la Prenotazione
        Prenotazione p = eventfy.confermaPrenotazione();
        //Controllo che la prenotazionecorrente sia null
        assertNull(eventfy.getPrenotazioneCorrente());
        //Controllo che la dimensione della mappa dopo della conferma sia aumentata di 1 (dopo la conferma)
        assertEquals(mappaPrenotazioniPendenti_size + 1, eventfy.getPrenotazioniPendenti().size());
        //Controllo che la prenotazione sia stata inserita nella mappa delle prenotazioni pendenti
        assertTrue(eventfy.getPrenotazioniPendenti().containsValue(p));

    }

    @Test
    void mostraPrenotazioniPendentiTest() {
        //SCELGO un nuovo utente così da farci ritornare le sue prenotazioni pendenti
        eventfy.signUpLogIn(new Gestore("Gigi"));
        //Ritorno le prenotazioni pendente del gestore "Gigi" (devono essere 0 perchè appena registrato)
        List<Prenotazione> result = eventfy.mostraPrenotazioniPendenti();
        assertNotNull(result);
		assertEquals(0, result.size());

        //Facciamo il logIn con un altro utente già inserito
        eventfy.logIn(0);
        result = eventfy.mostraPrenotazioniPendenti();
        //L'utente con ID = 0 ha una prenotazione pendente
        assertTrue(result.size() > 0);


    }


    @Test
    void testSelezionaPrenotazionePendente() {

        eventfy.logIn(0);
        assertNull(eventfy.getMapPrenotazioniTemp());
        assertNull(eventfy.getPrenotazioneCorrente());

        eventfy.mostraPrenotazioniPendenti();
        assertNotNull(eventfy.getMapPrenotazioniTemp());

        //SCELGO il codice di prenotazione 0 già inserito nel sistema
        Prenotazione pRicercata = eventfy.selezionaPrenotazionePendente(0);
        // Verifica che la prenotazione sia stata selezionata correttamente
        assertNotNull(pRicercata);
        assertNotNull(eventfy.getPrenotazioneCorrente());
        assertEquals(0, pRicercata.getId());
    }

@Test
    void testAccettaPrenotazione() {
        
        eventfy.logIn(0);
        eventfy.mostraPrenotazioniPendenti();
        Prenotazione pRicercata = eventfy.selezionaPrenotazionePendente(0);
        // Accetta la prenotazione selezionata
        eventfy.accettaPrenotazione();
        // Verifica che la prenotazione sia stata spostata correttamente da pendente ad accettata
        assertTrue(eventfy.getPrenotazioniAccettate().containsValue(pRicercata));
        // Verifica che la prenotazione sia stata tolta da pendente
        assertFalse(eventfy.getPrenotazioniPendenti().containsValue(pRicercata));
        // Verifica che le variabili temporanee siano state svuotate
        assertNull(eventfy.getPrenotazioneCorrente());
        assertNull(eventfy.getMapPrenotazioniTemp());
        

    }

}
