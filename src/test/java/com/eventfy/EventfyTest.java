package com.eventfy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

        // Login - SignUp
        eventfy.signUpLogIn(new Gestore("Carlo"));

        assertNull(eventfy.getImpiantoCorrente());
        try {
            eventfy.nuovoImpianto("San Siro", "Milano", 80000, 100);
        } catch (Exception e) {
            fail();
        }

        assertNotNull(eventfy.getImpiantoCorrente());

        try {
            eventfy.nuovoImpianto("San Siro", "Milano", 0, 100);
        } catch (Exception e) {
            assertEquals("Impianto identico presente nel sistema", e.getMessage());
            ;
        }

    }

    @Test
    void testConfermaImpianto() {

        // Login - SignUp
        eventfy.signUpLogIn(new Gestore("Carlo"));
        // Ottengo il numero di impianti iniziale
        int numImpiantiIniziale = eventfy.getListaImpianti().size();

        Impianto nuovoImpianto;
        try {
            nuovoImpianto = eventfy.nuovoImpianto("Metro Stad", "Milano", 88000, 100);
        } catch (Exception e) {
            fail();
            return;
        }

        eventfy.confermaImpianto();

        assertNull(eventfy.getImpiantoCorrente());
        assertEquals(numImpiantiIniziale + 1, eventfy.getListaImpianti().size());
        assertTrue(eventfy.getListaImpianti().contains(nuovoImpianto));

    }

    @Test
    void testNuovaPrenotazione() {

        // Login - SignUp
        eventfy.logIn(5);
        assertNull(eventfy.getPrenotazioneCorrente());

        // Testo implicitamente il metodo isMaggioreUguale della classe Impianto
        List<Impianto> impiantiDisponibili;
        try {
            impiantiDisponibili = eventfy.nuovaPrenotazione("Prenotazione1", "Descrizione 1", 2,
                    15000, LocalDate.of(2029, 9, 1), LocalTime.now());
        } catch (Exception e) {
            fail();
            return;
        }

        assertEquals(3, impiantiDisponibili.size());
        assertNotNull(eventfy.getPrenotazioneCorrente());

    }

    @Test
    void testNuovaPrenotazioneEccezioni() {

        // Login - SignUp
        eventfy.logIn(5);
        assertNull(eventfy.getPrenotazioneCorrente());

        // Se un evento è già scaduto, la richiesta non può essere creata
        try {
            eventfy.nuovaPrenotazione("Prenotazione1", "Descrizione 1", 2,
                    15000, LocalDate.of(2022, 9, 1), LocalTime.now());
        } catch (Exception e) {
            assertEquals("Evento già scaduto", e.getMessage());
        }

        try {
            eventfy.nuovaPrenotazione("Prenotazione1", "Descrizione 1", 2,
                    15000, LocalDate.of(2024, 3, 5), LocalTime.now());
        } catch (Exception e) {
            assertEquals("Evento già scaduto", e.getMessage());
        }

    }

    @Test
    void testPrenotaImpianto() {

        // Login - SignUp
        eventfy.signUpLogIn(new Artista("Dua Lipa"));

        try {
            eventfy.nuovaPrenotazione("P1", "d1", 329, 20, LocalDate.of(2024, 10, 10), LocalTime.now());
        } catch (Exception e) {
            fail();
        }

        try {
            eventfy.prenotaImpianto(0);
        } catch (Exception e) {
            fail();
        }

        // L'impianto scelto deve essere associato alla prenotazione corrente
        assertEquals(eventfy.getImpianto(0), eventfy.getPrenotazioneCorrente().getImpianto());

    }

    @Test
    void testPrenotaImpiantoEccezioni() {

        // Login - SignUp
        eventfy.signUpLogIn(new Artista("Dua Lipa"));

        try {
            eventfy.nuovaPrenotazione("P1", "d1", 329, 20, LocalDate.of(2030, 10, 10), LocalTime.now());
        } catch (Exception e) {
            fail();
        } // SCELGO L'IMPIANTO CON ID = 0

        try {
            eventfy.prenotaImpianto(49);
        } catch (Exception e) {
            assertEquals("Hai inserito un ID dell'impianto scorretto", e.getMessage());
        }
    }

    @Test
    void testConfermaPrenotazione() {

        eventfy.signUpLogIn(new Artista("21 savage"));

        // Ottengo la dimensione della mappa delle prenotazioni pendenti prima di
        // confermare una nuova prenotazione
        int mappaPrenotazioniPendenti_size = eventfy.getPrenotazioniPendenti().size();

        try {
            eventfy.nuovaPrenotazione("P1", "d1", 329, 20, LocalDate.of(2089, 3, 3), LocalTime.now());
        } catch (Exception e) {
            fail();
        }

        try {
            // scelgo l'impianto 3
            eventfy.prenotaImpianto(3);
        } catch (Exception e) {
            fail();
        }

        // Controllo che Prenotazionecorrente NON sia null
        assertNotNull(eventfy.getPrenotazioneCorrente());
        // Confermo la Prenotazione
        Prenotazione p = eventfy.confermaPrenotazione();
        // Controllo che la prenotazionecorrente sia null
        assertNull(eventfy.getPrenotazioneCorrente());
        // Controllo che la dimensione della mappa dopo della conferma sia aumentata di
        // 1 (dopo la conferma)
        assertEquals(mappaPrenotazioniPendenti_size + 1, eventfy.getPrenotazioniPendenti().size());
        // Controllo che la prenotazione sia stata inserita nella mappa delle
        // prenotazioni pendenti
        assertTrue(eventfy.getPrenotazioniPendenti().containsValue(p));

    }

    @Test
    void mostraPrenotazioniPendentiTest() {
        // Facciamo il logIn con un utente (gestore) già inserito
        eventfy.logIn(2);
        List<Prenotazione> result = eventfy.mostraPrenotazioniPendenti();
        // L'utente con ID = 2 ha una prenotazione pendente
        assertTrue(result.size() > 0);

    }

    @Test
    void testSelezionaPrenotazionePendente() {
        // Facciamo il logIn con un utente già inserito
        eventfy.logIn(1);
        assertNull(eventfy.getPrenotazioneCorrente());

        eventfy.mostraPrenotazioniPendenti();
        assertNotNull(eventfy.getMapPrenotazioniTemp());

        // SCELGO il codice di prenotazione 7 già inserito nel sistema
        Prenotazione pRicercata = null;
        try {
            pRicercata = eventfy.selezionaPrenotazionePendente(7);
        } catch (Exception e) {
            fail();
        }
        // Verifica che la prenotazione sia stata selezionata correttamente
        assertNotNull(pRicercata);
        assertNotNull(eventfy.getPrenotazioneCorrente());
        assertEquals(7, pRicercata.getId());

        eventfy.setPrenotazioneCorrenteNull();

        // controllo che se viene passato un id non presente mi da errore

        assertNull(eventfy.getPrenotazioneCorrente());

        eventfy.mostraPrenotazioniPendenti();
        assertNotNull(eventfy.getMapPrenotazioniTemp());

        // SCELGO il codice di prenotazione 7 già inserito nel sistema
        try {
            pRicercata = eventfy.selezionaPrenotazionePendente(6);
        } catch (Exception e) {
            assertEquals("Hai inserito un ID della prenotazione errato", e.getMessage());
        }
    }

    @Test
    void testAccettaPrenotazione() {
        // Facciamo il logIn con un gestore esistente
        eventfy.logIn(0);
        eventfy.mostraPrenotazioniPendenti();

        Prenotazione pRicercata = null;

        try {
            pRicercata = eventfy.selezionaPrenotazionePendente(0);
        } catch (Exception e) {
            fail();
        }
        // Accetta la prenotazione selezionata
        // System.out.println(pRicercata);
        try {
            eventfy.accettaPrenotazione();
        } catch (Exception e) {
            fail();
        }
        // Verifica che la prenotazione sia stata spostata correttamente da pendente ad
        // accettata
        assertTrue(eventfy.getPrenotazioniAccettate().containsValue(pRicercata));
        // Verifica che la prenotazione sia stata tolta da pendente
        assertFalse(eventfy.getPrenotazioniPendenti().containsValue(pRicercata));
        // Verifica che le variabili temporanee siano state svuotate
        assertNull(eventfy.getPrenotazioneCorrente());
        assertNull(eventfy.getMapPrenotazioniTemp());

        // Proviamo ad accettare una prenotazione che ha una stessa data di P0
        eventfy.mostraPrenotazioniPendenti();
        try {
            pRicercata = eventfy.selezionaPrenotazionePendente(13);
        } catch (Exception e) {
            assertEquals("Hai inserito un ID della prenotazione errato", e.getMessage());
        }

        try {
            eventfy.accettaPrenotazione();
        } catch (Exception e) {
            assertEquals("Collisione data", e.getMessage());
        }

    }

    @Test
    void testRecuperaBraniArtista() {
        // Facciamo il logIn con un gestore esistente
        eventfy.logIn(3);

        eventfy.aggiungiScaletta();
        List<Brano> brani = eventfy.recuperaBraniArtista(5);
        assertNotNull(eventfy.getPrenotazioneCorrente());
        assertEquals(3, brani.size());

    }

    @Test
    void testAggiungiBrano() {
        // Facciamo il logIn con un artista esistente
        eventfy.logIn(3);

        int lunghezzaScalettaIniziale = eventfy.getPrenotazione(5).getEvento().getListaBrani().size();
        System.out.println(lunghezzaScalettaIniziale);

        eventfy.aggiungiScaletta();
        List<Brano> listaBrani = eventfy.recuperaBraniArtista(5);
        for (Brano b : listaBrani) {
            System.out.println(b);
        }
        try {
            eventfy.aggiungiBrano(0);
        } catch (Exception e) {
            fail();
            return;
        }
        assertEquals(lunghezzaScalettaIniziale + 1, eventfy.getPrenotazione(5).getEvento().getListaBrani().size());

        // consideriamo il caso in cui aggiungiamo un brano già presente

        eventfy.getPrenotazione(5).getEvento().getListaBrani().size();
        eventfy.aggiungiScaletta();
        eventfy.recuperaBraniArtista(5);

        try {
            eventfy.aggiungiBrano(0);
        } catch (Exception e) {
            assertEquals("Brano già inserito", e.getMessage());
        }

        try {
            eventfy.aggiungiBrano(200);
        } catch (Exception e) {
            assertEquals("Id brano non valido", e.getMessage());
        }

    }

    @Test
    void mostraPrenotazioniAccettateTest() {
        eventfy.logIn(3);// Artista TheWeeknd

        // Chiamata al metodo mostraPrenotazioniAccettate
        List<Prenotazione> result = eventfy.mostraPrenotazioniAccettate();
        assertNotNull(result);
        // L'utente con ID = 0 ha una prenotazione pendente
        assertEquals(7, result.size());

    }

    @Test
    void SelezionaPrenotazioneInvito() {
        eventfy.logIn(3);// Artista TheWeeknd

        eventfy.mostraPrenotazioniAccettate();
        // Chiamata al metodo selezionaArtista
        List<Utente> artisti = eventfy.selezionaPrenotazioneInvito(4);

        // Verifica che la lista non sia vuota
        assertFalse(artisti.isEmpty());
    }

    @Test
    // da rivedere
    void invitaArtistaTest() {
        eventfy.logIn(3);// Artista TheWeeknd

        eventfy.mostraPrenotazioniAccettate();
        eventfy.selezionaPrenotazioneInvito(4);

        // Prendo la lunghezza della lista per andarla a confrontare sucessivamente
        Artista artista = (Artista) eventfy.getMappaUtenti().get(5);
        int sizeMappaInvitiIniziale = artista.getListaInvitiPendenti().size();

        try {
            eventfy.invitaArtista(5);
        } catch (Exception e) {
            fail();
        }
        // Deve essere stato registrato un invito
        assertEquals(sizeMappaInvitiIniziale + 1, artista.getListaInvitiPendenti().size());

        // Ora controlliamo il caso di fallimento della funzione
        // prima nel caso in cui si inserisce un id non esistente
        eventfy.mostraPrenotazioniAccettate();
        eventfy.selezionaPrenotazioneInvito(4);

        try {
            eventfy.invitaArtista(12);
        } catch (Exception e) {
            assertEquals("L'artista non esiste", e.getMessage());
        }

        // provo ad invitare un gestore
        try {
            eventfy.invitaArtista(1);
        } catch (Exception e) {
            assertEquals("L'artista non esiste", e.getMessage());
        }

        // ora nel caso in cui l'artista è stato già invitato
        eventfy.mostraPrenotazioniAccettate();
        eventfy.selezionaPrenotazioneInvito(4);

        try {
            eventfy.invitaArtista(5);
        } catch (Exception e) {
            assertEquals("Artista già invitato", e.getMessage());
        }

    }

    @Test
    void gestisciInvitoTest() {

        eventfy.logIn(3);// Artista TheWeeknd
        assertEquals(eventfy.gestisciInvito(), ((Artista) eventfy.getUtenteCorrente()).getListaInvitiPendenti());

        eventfy.logIn(4);
        assertEquals(eventfy.gestisciInvito(), ((Artista) eventfy.getUtenteCorrente()).getListaInvitiPendenti());

        // Provo con un nuovo utente creato ad'hoc, deve avere una lista inviti con 0
        // elementi
        eventfy.signUpLogIn(new Artista("A11"));
        assertEquals(eventfy.gestisciInvito(), ((Artista) eventfy.getUtenteCorrente()).getListaInvitiPendenti());

    }

    @Test
    void accettaInvitoTest() {

        // Creo ed accetto un invito;

        // Creazione invito
        eventfy.logIn(4);// Artista TheWeeknd

        Artista artistaCorrente = (Artista) eventfy.getUtenteCorrente();

        int numInvitiAccettatiIniziale = artistaCorrente.getListaInvitiAccettati().size();
        int numInvitiPendentiIniziale = artistaCorrente.getListaInvitiPendenti().size();

        List<Invito> listaInviti = eventfy.gestisciInvito();

        for (Invito i : listaInviti) {
            System.out.println(i);
        }

        try {
            eventfy.accettaInvito(1);
        } catch (Exception e) {
            fail();
        }

        assertEquals(numInvitiAccettatiIniziale + 1, artistaCorrente.getListaInvitiAccettati().size());
        assertEquals(numInvitiPendentiIniziale - 1, artistaCorrente.getListaInvitiPendenti().size());

        // ora verifico quando il test fallisce
        eventfy.gestisciInvito();
        try {
            eventfy.accettaInvito(3);
        } catch (Exception e) {
            assertEquals("Id invito non valido", e.getMessage());
        }

    }

    @Test
    void rifiutaInvitotoTest() {

        // Creo ed accetto un invito;

        // Creazione invito
        eventfy.logIn(3);// Artista TheWeeknd

        Artista artistaCorrente = (Artista) eventfy.getUtenteCorrente();

        int numInvitiRifiutatiIniziale = artistaCorrente.getListaInvitiRifiutati().size();
        int numInvitiPendentiIniziale = artistaCorrente.getListaInvitiPendenti().size();

        List<Invito> listaInviti = eventfy.gestisciInvito();

        for (Invito i : listaInviti) {
            System.out.println(i);
        }

        try {
            eventfy.rifiutaInvito(2);
        } catch (Exception e1) {
            fail();
        }

        assertEquals(numInvitiRifiutatiIniziale + 1, artistaCorrente.getListaInvitiRifiutati().size());
        assertEquals(numInvitiPendentiIniziale - 1, artistaCorrente.getListaInvitiPendenti().size());

        // ora verifichiamo in caso di errore
        numInvitiRifiutatiIniziale = artistaCorrente.getListaInvitiRifiutati().size();
        numInvitiPendentiIniziale = artistaCorrente.getListaInvitiPendenti().size();

        eventfy.gestisciInvito();

        try {
            eventfy.rifiutaInvito(10);
        } catch (Exception e1) {
            assertEquals("Id invito non valido", e1.getMessage());
        }

        assertEquals(numInvitiRifiutatiIniziale, artistaCorrente.getListaInvitiRifiutati().size());
        assertEquals(numInvitiPendentiIniziale, artistaCorrente.getListaInvitiPendenti().size());

    }

    @Test
    void inserisciRecensioneTest() {
        eventfy.logIn(3);// Artista TheWeeknd
        List<Prenotazione> prenotazioniArtista = eventfy.inserisciRecensione();
        assertNotNull(prenotazioniArtista);
        // Verifica che la lista contenga le prenotazioni dell'artista corrente
        for (Prenotazione prenotazione : prenotazioniArtista) {
            assertTrue(prenotazione.hasArtista((Artista) eventfy.getUtenteCorrente()));
        }
        assertTrue(prenotazioniArtista.size() > 0);
    }

    // ----- Sono qui
    @Test
    void testCreaRecensione() {
        eventfy.logIn(3);// Artista TheWeeknd

        // provo un id non valido
        eventfy.inserisciRecensione();
        try {
            eventfy.creaRecensione(800, "Impianto al top, bravi", 5);
        } catch (Exception e) {
            assertEquals("Id non valido", e.getMessage());
        }

        // provo un voto non valido
        eventfy.inserisciRecensione();
        try {
            eventfy.creaRecensione(8, "Impianto al top, bravi", 6);
        } catch (Exception e) {
            assertEquals("Il valore deve essere compreso tra 0 a 5", e.getMessage());
        }

        // recensione corretta
        eventfy.inserisciRecensione();
        try {
            eventfy.creaRecensione(8, "Impianto al top, bravi", 5);
        } catch (Exception e) {
            fail();
        }
        Prenotazione p8 = eventfy.getPrenotazione(8);
        RecensioneImpianto r8 = p8.getImpianto().getListaRecensioni().get(0);
        assertEquals("Impianto al top, bravi", r8.getCommento());
        assertEquals(5, r8.getVoto());
        assertEquals(eventfy.getUtenteCorrente(), r8.getUtente());

        // Provo ad inserire una recensione per una prenotazione "già recensita"
        eventfy.inserisciRecensione();
        try {
            eventfy.creaRecensione(8, "Impianto al top, bravi", 5);
        } catch (Exception e) {
            assertEquals("E' già stato recensito l'impianto per questa prenotazione", e.getMessage());
        }

        assertNull(eventfy.getMapPrenotazioniTemp());
    }

    @Test
    void eliminaPrenotazioneTest() {
        eventfy.logIn(3);// Artista TheWeeknd
        // Inserisco tutte le prenotazioni accettate e pendenti in una lista
        // "tuttePrenotazioni"
        List<Prenotazione> tuttePrenotazioni = new ArrayList<Prenotazione>(
                eventfy.getPrenotazioniAccettate().values());
        List<Prenotazione> prenotazioniPendenti = new ArrayList<Prenotazione>(
                eventfy.getPrenotazioniPendenti().values());

        tuttePrenotazioni.addAll(prenotazioniPendenti);

        // richiamo il metodo da testare
        List<Prenotazione> prenotazioni = eventfy.eliminaPrenotazione();

        // controllo se tutte le prenotazioni all'interno della lista fornita dal metodo
        // sono dell'artista corrente
        for (Prenotazione p : prenotazioni) {
            assertTrue(p.hasArtista((Artista) eventfy.getUtenteCorrente()));
        }
        // Controllo se in "prenotazioni" sono presenti tutte le prenotazioni
        // dell'artista corrente
        assertTrue(tuttePrenotazioni.containsAll(prenotazioni));
    }

    @Test
    void confermaEliminazioneTest() {
        eventfy.logIn(3);// Artista TheWeeknd

        // Eliminazione di una prenotazione pendente
        int codicePrenotazione = 1;
        // Verifica l'esistenza della prenotazione pendente
        assertNotNull(eventfy.getPrenotazioniPendenti().get(codicePrenotazione));
        // Verifica l'assenza nella mappa delle prenotazioni annullate
        assertNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));

        eventfy.eliminaPrenotazione();
        try {
            eventfy.confermaEliminazione(codicePrenotazione);
        } catch (Exception e) {
            fail();
        }

        // Verifica che la prenotazione pendente con il codice 1 sia stata eliminata
        assertNull(eventfy.getPrenotazioniPendenti().get(codicePrenotazione));
        // Verifica la presenza della prenotazione cancellata nella mappa delle pr.
        // annullate
        assertNotNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));

        // Eliminazione di una prenotazione accettata
        codicePrenotazione = 9;
        // Verifica l'esistenza della prenotazione accettata
        assertNotNull(eventfy.getPrenotazioniAccettate().get(codicePrenotazione));
        // Verifica l'assenza nella mappa delle prenotazioni annullate
        assertNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));

        eventfy.eliminaPrenotazione();
        try {
            eventfy.confermaEliminazione(codicePrenotazione);
        } catch (Exception e) {
            fail();
        }
        // Verifica che la prenotazione pendente con il codice 5 sia stata eliminata
        // dalla mappa pr. accettate
        assertNull(eventfy.getPrenotazioniAccettate().get(codicePrenotazione));
        // Verifica la presenza della prenotazione cancellata nella mappa delle pr.
        // annullate
        assertNotNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));

        // Eliminazione prenotazione non valida
        codicePrenotazione = 345;
        eventfy.eliminaPrenotazione();
        try {
            eventfy.confermaEliminazione(codicePrenotazione);
        } catch (Exception e) {
            assertEquals("Id non valido", e.getMessage());
            ;
        }
    }

    // 12
    @Test
    void mostraArtistiEventi() {
        // Registro ed effettuo il login con un nuovo utente (FAN)
        eventfy.signUpLogIn(new Utente("albertoFan"));
        // Esegui la funzione mostraArtistiEventi
        List<Utente> artisti = eventfy.mostraArtistiEventi();
        // Verifica che la lista non sia nulla e che contenga artisti
        assertNotNull(artisti);
        assertFalse(artisti.isEmpty());
        // verifica se ogni elemento è effettivamente un'istanza di Artista
        for (Utente utente : artisti) {
            assertTrue(utente instanceof Artista);
        }
    }

    @Test
    void partecipaEventoTest() {
        // Registro ed effettuo il login con un nuovo utente (FAN)
        eventfy.signUpLogIn(new Utente("albertoFan"));
        // AlbertoFan vuole partecipare ad un evento di theweeknd (prima si prova un
        // artista non valido con codice non esistente)
        eventfy.mostraArtistiEventi();

        // provo un artista non valido
        List<Prenotazione> prenotazioniTheWeekndPartecipabili = null;
        try {
            prenotazioniTheWeekndPartecipabili = eventfy.partecipaEvento(300);
        } catch (Exception e) {
            assertEquals("Id non valido", e.getMessage());
        }

        // artista valido
        eventfy.mostraArtistiEventi();
        try {
            prenotazioniTheWeekndPartecipabili = eventfy.partecipaEvento(3);
        } catch (Exception e) {
            fail();
        }
        // Deve esistere almeno una prenotazione partecipabile
        assertTrue(prenotazioniTheWeekndPartecipabili.size() > 0);

    }

    @Test
    void confermaPartecipazioneTest() {
        // Registro ed effettuo il login con un nuovo utente (FAN)
        eventfy.signUpLogIn(new Fan("albertoFan"));

        int codice_prenotazione = 105;
        Utente utenteCorrente = eventfy.getUtenteCorrente();

        // AlbertoFan vuole partecipare ad un evento di theweeknd
        eventfy.mostraArtistiEventi();
        try {
            eventfy.partecipaEvento(3);
        } catch (Exception e) {
            fail();
        }

        // provo un evento non valido
        try {
            // metodo da testare
            eventfy.confermaPartecipazione(codice_prenotazione);
        } catch (Exception e) {
            assertEquals("Id non valido", e.getMessage());
        }

        // Partecipo all'evento con ID 10 (Vedesi metodo populate, è un evento
        // partecipabile di theWeeknd)
        codice_prenotazione = 10;
        eventfy.mostraArtistiEventi();
        try {
            eventfy.partecipaEvento(3);
        } catch (Exception e) {
        }
        try {
            // metodo da testare
            eventfy.confermaPartecipazione(codice_prenotazione);
        } catch (Exception e) {
            fail();
        }

        assertTrue(((Fan) utenteCorrente).isPartecipante(codice_prenotazione));
        assertTrue(eventfy.getPrenotazione(codice_prenotazione).hasPartecipante((Fan) utenteCorrente));
        assertNull(eventfy.getMapPrenotazioniTemp());

        // Provo a partecipare nuovamente allo stesso evento
        eventfy.mostraArtistiEventi();
        try {
            eventfy.partecipaEvento(3);
        } catch (Exception e) {
        }
        try {
            // metodo da testare
            eventfy.confermaPartecipazione(codice_prenotazione);
        } catch (Exception e) {
            assertEquals("Utente già partecipante", e.getMessage());
        }

    }

    // 13
    @Test
    void inserisciRecensioneEvento() {
        // Effettuo il login con un utente (FAN)
        eventfy.logIn(6);
        // Chiamo il metodo e verifica il risultato
        List<Evento> listaEventi = eventfy.inserisciRecensioneEvento();
        // Assicurati che la lista non sia nulla
        assertNotNull(listaEventi);
        // Verifica se ogni elemento nella lista è effettivamente un'istanza di Evento
        for (Evento evento : listaEventi) {
            assertTrue(evento instanceof Evento);
        }
    }

    @Test
    void confermaRecensioneEvento() {
        // Registro ed effettuo il login con il fan id=6
        // l'utente partecipa all'evento(/prenotazione) id=11
        int id_utente = 6;
        int id_prenotazione = 11;
        eventfy.logIn(id_utente);

        // Provo ad effettuare una recensione scorretta
        int voto = -1;
        String commento = "Bello";
        try {
            eventfy.confermaRecensioneEvento(id_prenotazione, commento, voto);
        } catch (Exception e) {
            assertEquals("Il valore deve essere compreso tra 0 a 5", e.getMessage());
            ;
        }

        // Confronto il numero di recensioni di un evento prima e dopo la conferma di
        // una nuova recensione
        int numRecensioniIniziali = eventfy.getPrenotazione(id_prenotazione).getNumRecensioniEvento();
        voto = 5;
        commento = "Bello";
        try {
            eventfy.confermaRecensioneEvento(id_prenotazione, commento, voto);
        } catch (Exception e) {
            fail();
        }
        // il numero di recensioni iniziale e finale deve differire di 1
        assertEquals(numRecensioniIniziali + 1, eventfy.getPrenotazione(id_prenotazione).getNumRecensioniEvento());

        // Provo a rieffettuare la procedura con una prenotazione non recensibile
        id_prenotazione = 12;
        numRecensioniIniziali = eventfy.getPrenotazione(id_prenotazione).getNumRecensioniEvento();
        try {
            eventfy.confermaRecensioneEvento(id_prenotazione, commento, voto);
        } catch (Exception e) {
            assertEquals("Evento non recensibile", e.getMessage());
        }

        // provo a recensire nuovamente lo stesso evento
        id_prenotazione = 11;
        try {
            eventfy.confermaRecensioneEvento(id_prenotazione, commento, voto);
        } catch (Exception e) {
            assertEquals("L'evento è già stato recensito dal fan corrente", e.getMessage());
        }

    }

    @Test
    void mostraPrenotazioniAccettateGestoreTest() {

        // faccio il login con un gestore
        eventfy.logIn(1);

        List<Prenotazione> prenotazioniAccettate = eventfy.mostraPrenotazioniAccettateGestore();

        // verifico che mi venga ritornata una lista
        assertNotNull(prenotazioniAccettate);

        // controllo che la lsuat ritornata sia della dimensione esatta
        assertEquals(7, prenotazioniAccettate.size());
    }

    @Test
    void mostraPrenotazioniPendentiGestoreTest() {

        // faccio il login con un gestore
        eventfy.logIn(1);

        List<Prenotazione> prenotazioniPendenti = eventfy.mostraPrenotazioniPendentiGestore();

        // verifico che mi venga ritornata una lista
        assertNotNull(prenotazioniPendenti);

        // verifico che la dimensione della lista sia corretta
        assertEquals(1, prenotazioniPendenti.size());
    }

    @Test
    void visualizzaImpiantiGestoreTest() {

        // faccio il login con un gestore
        eventfy.logIn(1);

        List<Impianto> listaImpianti = eventfy.visualizzaImpiantiGestore();

        // verifico che mi venga ritornata una lista
        assertNotNull(listaImpianti);

        // verifico che la dimensione della lista sia corretta
        assertEquals(1, listaImpianti.size());

    }

    @Test

    void visualizzaPrenotazioniPendentiArtista() {
        eventfy.logIn(3);

        List<Prenotazione> listaPrenotazioni = eventfy.visualizzaPrenotazioniPendentiArtista();

        // verifico che mi venga ritornata una lista
        assertNotNull(listaPrenotazioni);
    }
    /*
    @Test
    void mostraInvitiAccettatiTest() {

        eventfy.logIn(4);

        try {
            eventfy.accettaInvito(0);
        } catch (Exception e) {
            fail();
        }

        List<Prenotazione> listaPrenotazioni = eventfy.mostraPrenotazioniAccettate();

        // verifico che mi venga ritornata una lista
        assertNotNull(listaPrenotazioni);

        // verifico che la dimensione della lista sia corretta
        assertEquals(1, listaPrenotazioni.size());

    }
     */
}