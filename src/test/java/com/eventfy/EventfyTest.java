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
        eventfy.nuovoImpianto("San Siro", "Milano", 80000, 100);
        assertNotNull(eventfy.getImpiantoCorrente());

    }

    @Test
    void testConfermaImpianto() {

        // Login - SignUp
        eventfy.signUpLogIn(new Gestore("Carlo"));

        Impianto nuovoImpianto = eventfy.nuovoImpianto("Metro Stad", "Milano", 80000, 100);
        eventfy.confermaImpianto();

        assertNull(eventfy.getImpiantoCorrente());
        assertEquals(5, eventfy.getListaImpianti().size());
        assertTrue(eventfy.getListaImpianti().contains(nuovoImpianto));

    }

    @Test
    void testNuovaPrenotazione() {

        // Login - SignUp
        eventfy.logIn(5);
        // Testo implicitamente il metodo isMaggioreUguale della classe Impianto
        List<Impianto> impiantiDisponibili = eventfy.nuovaPrenotazione("Prenotazione1", "Descrizione 1", 2,
                15000, LocalDate.now(), LocalTime.now());
        assertEquals(3, impiantiDisponibili.size());
        assertNotNull(eventfy.getPrenotazioneCorrente());

    }

    @Test
    void testPrenotaImpianto() {

        // Login - SignUp
        eventfy.signUpLogIn(new Artista("Dua Lipa"));

        eventfy.nuovaPrenotazione("P1", "d1", 329, 20, LocalDate.now(), LocalTime.now());
        // SCELGO L'IMPIANTO CON ID = 0
        eventfy.prenotaImpianto(0);
        // L'impianto scelto deve essere associato alla prenotazione corrente
        assertEquals(eventfy.getImpianto(0), eventfy.getPrenotazioneCorrente().getImpianto());

        // QUANDO FAREMO LE ESTENSIONI PROVEREMO A SELEZIONARE UN IMPIANTO CHE
        // NON E' NELLA MAPPA TEMPORANEA (CHE NON ESISTE O NON SODDISFA I REQUISITI DI
        // CAPIENZA)
        /*
         * impiantiDisponibili = eventfy.nuovaPrenotazione("P2", "d2", 329, 10000,
         * LocalDate.now(), LocalTime.now());
         * //SCELGO L'IMPIANTO CON ID = 0
         * eventfy.prenotaImpianto(1);
         */
    }

    @Test
    void testConfermaPrenotazione() {

        eventfy.signUpLogIn(new Artista("Dua Lipa"));

        // Ottengo la dimensione della mappa delle prenotazioni pendenti prima di
        // confermare una nuova prenotazione
        int mappaPrenotazioniPendenti_size = eventfy.getPrenotazioniPendenti().size();

        eventfy.nuovaPrenotazione("P1", "d1", 329, 20, LocalDate.now(), LocalTime.now());
        // SCELGO L'IMPIANTO CON ID = 3
        eventfy.prenotaImpianto(3);
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
        // Facciamo il logIn con un utente già inserito
        eventfy.logIn(2);
        List<Prenotazione> result = eventfy.mostraPrenotazioniPendenti();
        // L'utente con ID = 2 ha una prenotazione pendente
        assertTrue(result.size() > 0);

    }

    @Test
    void testSelezionaPrenotazionePendente() {
        // Facciamo il logIn con un utente già inserito
        eventfy.logIn(1);
        assertNull(eventfy.getMapPrenotazioniTemp());
        assertNull(eventfy.getPrenotazioneCorrente());

        eventfy.mostraPrenotazioniPendenti();
        assertNotNull(eventfy.getMapPrenotazioniTemp());

        // SCELGO il codice di prenotazione 6 già inserito nel sistema
        Prenotazione pRicercata = eventfy.selezionaPrenotazionePendente(7);
        // Verifica che la prenotazione sia stata selezionata correttamente
        assertNotNull(pRicercata);
        assertNotNull(eventfy.getPrenotazioneCorrente());
        assertEquals(7, pRicercata.getId());
    }

    @Test
    void testAccettaPrenotazione() {
        // Facciamo il logIn con un gestore esistente
        eventfy.logIn(0);

        eventfy.mostraPrenotazioniPendenti();
        Prenotazione pRicercata = eventfy.selezionaPrenotazionePendente(0);
        // Accetta la prenotazione selezionata
        eventfy.accettaPrenotazione();
        // Verifica che la prenotazione sia stata spostata correttamente da pendente ad
        // accettata
        assertTrue(eventfy.getPrenotazioniAccettate().containsValue(pRicercata));
        // Verifica che la prenotazione sia stata tolta da pendente
        assertFalse(eventfy.getPrenotazioniPendenti().containsValue(pRicercata));
        // Verifica che le variabili temporanee siano state svuotate
        assertNull(eventfy.getPrenotazioneCorrente());
        assertNull(eventfy.getMapPrenotazioniTemp());

    }

    @Test
    void testRecuperaBraniArtista() {
        // Facciamo il logIn con un gestore esistente
        eventfy.logIn(3);

        eventfy.aggiungiScaletta();
        List<Brano> brani = eventfy.recuperaBraniArtista(5);
        assertEquals(3, brani.size());

    }

    @Test
    void testAggiungiBrano() {
        // Facciamo il logIn con un gestore esistente
        eventfy.logIn(3);

        eventfy.aggiungiScaletta();
        eventfy.recuperaBraniArtista(5);
        eventfy.aggiungiBrano(0);
        eventfy.aggiungiBrano(2);

        assertEquals(2, eventfy.getPrenotazioneCorrente().getEvento().getListaBrani().size());

    }

    @Test
    void mostraPrenotazioniAccettateTest() {
        eventfy.logIn(3);// Artista TheWeeknd

        // Chiamata al metodo mostraPrenotazioniAccettate
        List<Prenotazione> result = eventfy.mostraPrenotazioniAccettate();
        assertNotNull(result);
        // L'utente con ID = 0 ha una prenotazione pendente
        assertEquals(5, result.size());

    }

    @Test
    void SelezionaPrenotazioneInvito() {
        eventfy.logIn(3);// Artista TheWeeknd
        
        assertNull(eventfy.getInvitoCorrente());
        eventfy.mostraPrenotazioniAccettate();
        // Chiamata al metodo selezionaArtista
        List<Utente> artisti = eventfy.selezionaPrenotazioneInvito(4);
        assertNotNull(eventfy.getInvitoCorrente());
        // Verifica che la lista non sia vuota
        assertFalse(artisti.isEmpty());
    }

    @Test
    void invitaArtistaTest() {
        eventfy.logIn(3);// Artista TheWeeknd
        //Prendo la lunghezza della lista per andarla a confrontare sucessivamente
        int sizeMappaInvitiIniziale = eventfy.getMappaInvitiPendenti().size();
        eventfy.mostraPrenotazioniAccettate();
        eventfy.selezionaPrenotazioneInvito(4);
        eventfy.invitaArtista(0);
        // Deve essere stato registrato un invito
        assertEquals(sizeMappaInvitiIniziale + 1, eventfy.getMappaInvitiPendenti().size());

    }

    @Test
    void gestisciInvitoTest() {
        eventfy.logIn(3);// Artista TheWeeknd
        List<Invito> listaInviti = eventfy.gestisciInvito();
        assertEquals(1, listaInviti.size());

        eventfy.logIn(4);
        listaInviti = eventfy.gestisciInvito();
        assertEquals(1, listaInviti.size());

        // Provo con un nuovo utente creato ad'hoc
        eventfy.signUpLogIn(new Artista("A11"));
        listaInviti = eventfy.gestisciInvito();
        assertEquals(0, listaInviti.size());

    }

    @Test
    void selezionaInvitoTest() {
        eventfy.logIn(3);// Artista TheWeeknd

        eventfy.gestisciInvito();
        Evento e = eventfy.selezionaInvito(2);
        // L'evento relativo all'invito selezionato ha ID = 1
        assertEquals(0, e.getId());

    }

    @Test
    void accettaInvitoTest() {
        eventfy.logIn(3);// Artista TheWeeknd

        int numInvitiPendentiIniziale = eventfy.getMappaInvitiPendenti().size();
        int numInvitiAccettatiIniziale = eventfy.getMappaInvitiAccettati().size();

        eventfy.gestisciInvito();
        eventfy.selezionaInvito(2);
        // L'evento relativo all'invito selezionato ha ID = 1
        eventfy.accettaInvito();

        // Deve esserci un invito accettato in più
        assertEquals(numInvitiAccettatiIniziale + 1, eventfy.getMappaInvitiAccettati().size());

        // Deve esserci un invito pendente in meno
        assertEquals(numInvitiPendentiIniziale - 1, eventfy.getMappaInvitiPendenti().size());

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
        List<Prenotazione> prenotazioniArtista = eventfy.inserisciRecensione();
        eventfy.creaRecensione(8, "Impianto al top, bravi", 5);

        // Non so se questa cosa si può fare
        Prenotazione p8 = eventfy.getPrenotazione(8);
        Recensione r8 = p8.getImpianto().getListaRecensioni().get(0);
        assertEquals("Impianto al top, bravi", r8.getCommento());
        assertEquals(5, r8.getVoto());
        assertEquals(eventfy.getUtenteCorrente(), r8.getUtente());

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
        eventfy.confermaEliminazione(codicePrenotazione);
        // Verifica che la prenotazione pendente con il codice 1 sia stata eliminata
        assertNull(eventfy.getPrenotazioniPendenti().get(codicePrenotazione));
        // Verifica la presenza della prenotazione cancellata nella mappa delle pr.
        // annullate
        assertNotNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));

        // Eliminazione di una prenotazione pendente
        codicePrenotazione = 5;
        // Verifica l'esistenza della prenotazione pendente
        assertNotNull(eventfy.getPrenotazioniAccettate().get(codicePrenotazione));
        // Verifica l'assenza nella mappa delle prenotazioni annullate
        assertNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));

        eventfy.eliminaPrenotazione();
        eventfy.confermaEliminazione(codicePrenotazione);
        // Verifica che la prenotazione pendente con il codice 1 sia stata eliminata
        assertNull(eventfy.getPrenotazioniAccettate().get(codicePrenotazione));
        // Verifica la presenza della prenotazione cancellata nella mappa delle pr.
        // annullate
        assertNotNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));
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
        // AlbertoFan vuole partecipare ad un evento di theweeknd
        List<Prenotazione> prenotazioniTheWeekndPartecipabili = eventfy.partecipaEvento(3);
        // Deve esistere almeno una prenotazione partecipabile
        assertTrue(prenotazioniTheWeekndPartecipabili.size() > 0);
        // Nelle estensioni dovremmo provare il caso in cui un utente cerca di
        // partecipare ad un evento piu di una volta
    }

    @Test
    void confermaPartecipazioneTest() {
        // Registro ed effettuo il login con un nuovo utente (FAN)
        eventfy.signUpLogIn(new Fan("albertoFan"));
        Utente utenteCorrente = eventfy.getUtenteCorrente();
        // AlbertoFan vuole partecipare ad un evento di theweeknd
        eventfy.partecipaEvento(3);
        // Partecipo all'evento con ID 9 (Vedesi metodo populate, è un evento
        // partecipabile di theWeeknd)
        eventfy.confermaPartecipazione(9);
        assertTrue(((Fan) utenteCorrente).isPartecipante(9));
        assertTrue(eventfy.getPrenotazione(9).hasPartecipante((Fan) utenteCorrente));
        assertNull(eventfy.getMapPrenotazioniTemp());
        // Nelle estensioni dovremmo provare il caso in cui un utente cerca di
        // partecipare ad un evento piu di una volta

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
        // Registro ed effettuo il login con un nuovo utente (FAN)
        eventfy.signUpLogIn(new Fan("albertoFan"));
        Fan utenteCorrente = (Fan)eventfy.getUtenteCorrente();
        eventfy.partecipaEvento(3);
        int codice_evento = 9;
        eventfy.confermaPartecipazione(codice_evento);
        assertTrue(eventfy.getPrenotazione(9).hasPartecipante(utenteCorrente));
        assertTrue(((Fan) eventfy.getUtenteCorrente()).isPartecipante(codice_evento));
        String commento = "Evento fantastico!";
        int voto = 5;
        // Conferma la recensione
        eventfy.confermaRecensioneEvento(codice_evento, commento, voto);
        assertTrue(utenteCorrente.getListaEventi().size()>0);
        assertNull(eventfy.getPrenotazioneCorrente());
        assertNull(eventfy.getMapPrenotazioniTemp());

    }

}