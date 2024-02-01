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
        // Ottengo il numero di impianti iniziale
        int numImpiantiIniziale = eventfy.getListaImpianti().size();

        Impianto nuovoImpianto = eventfy.nuovoImpianto("Metro Stad", "Milano", 80000, 100);
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

    }

    @Test
    void testConfermaPrenotazione() {

        eventfy.signUpLogIn(new Artista("21 savage"));

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
        assertNull(eventfy.getMapPrenotazioniTemp());
        assertNull(eventfy.getPrenotazioneCorrente());

        eventfy.mostraPrenotazioniPendenti();
        assertNotNull(eventfy.getMapPrenotazioniTemp());

        // SCELGO il codice di prenotazione 7 già inserito nel sistema
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
        assertEquals(7, result.size());

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
        RecensioneImpianto r8 = p8.getImpianto().getListaRecensioni().get(0);
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

        // Eliminazione di una prenotazione accettata
        codicePrenotazione = 9;
        // Verifica l'esistenza della prenotazione accettata
        assertNotNull(eventfy.getPrenotazioniAccettate().get(codicePrenotazione));
        // Verifica l'assenza nella mappa delle prenotazioni annullate
        assertNull(eventfy.getMappaPrenotazioniAnnullate().get(codicePrenotazione));

        eventfy.eliminaPrenotazione();
        eventfy.confermaEliminazione(codicePrenotazione);
        // Verifica che la prenotazione pendente con il codice 5 sia stata eliminata dalla mappa pr. accettate
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

        int codice_prenotazione = 10;

        Utente utenteCorrente = eventfy.getUtenteCorrente();
        // AlbertoFan vuole partecipare ad un evento di theweeknd
        eventfy.partecipaEvento(3);
        // Partecipo all'evento con ID 10 (Vedesi metodo populate, è un evento
        // partecipabile di theWeeknd)
        eventfy.confermaPartecipazione(codice_prenotazione);
        assertTrue(((Fan) utenteCorrente).isPartecipante(codice_prenotazione));
        assertTrue(eventfy.getPrenotazione(codice_prenotazione).hasPartecipante((Fan) utenteCorrente));
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
        // Registro ed effettuo il login con il fan id=6
        // l'utente partecipa all'evento(/prenotazione) id=11
        // Confronto il numero di recensioni di un evento prima e dopo la conferma di una nuova recensione
        int id_utente = 6;
        int id_prenotazione = 11;
        eventfy.logIn(id_utente);

        int numRecensioniIniziali = eventfy.getPrenotazione(id_prenotazione).getNumRecensioniEvento();

        int voto = 5;
        String commento = "Bello";
        eventfy.confermaRecensioneEvento(id_prenotazione, commento, voto);
        //il numero di recensioni iniziale e finale deve differire di 1
        assertEquals(numRecensioniIniziali + 1, eventfy.getPrenotazione(id_prenotazione).getNumRecensioniEvento());

        //Provo a rieffettuare la procedura con una prenotazione non recensibile
        id_prenotazione = 12;
        numRecensioniIniziali = eventfy.getPrenotazione(id_prenotazione).getNumRecensioniEvento();
        eventfy.confermaRecensioneEvento(id_prenotazione, commento, voto);
        //Il numero di recensioni prima e dopo deve essere uguale
        assertEquals(numRecensioniIniziali, eventfy.getPrenotazione(id_prenotazione).getNumRecensioniEvento());


    }


    @Test
    void mostraPrenotazioniAccettateGestoreTest(){

        //faccio il login con un gestore
        eventfy.logIn(1);

        List<Prenotazione> prenotazioniAccettate = eventfy.mostraPrenotazioniAccettateGestore();

        //verifico che mi venga ritornata una lista 
        assertNotNull(prenotazioniAccettate);

        //controllo che la lsuat ritornata sia della dimensione esatta
        assertEquals(7, prenotazioniAccettate.size());
    }

    @Test
    void visualizzaEventiOrganizzatiTest(){

        //faccio il login con un'artista
        eventfy.logIn(3);

        List<Evento> eventiOrganizzati = eventfy.visualizzaEventiOrganizzati();

        //verifico che mi venga ritornata una lista 
        assertNotNull(eventiOrganizzati);

        assertEquals(8, eventiOrganizzati.size());
    }


    @Test
    void mostraEventiArtistaTest(){


        List<Evento> eventiOrganizzatiArtista = eventfy.mostraEventiArtista("theweeknd");

        //verifico che mi venga ritornata una lista 
        assertNotNull(eventiOrganizzatiArtista);

        assertEquals(8, eventiOrganizzatiArtista.size());
    }


    @Test
    void mostraPrenotazioniPendentiGestoreTest(){

        //faccio il login con un gestore
        eventfy.logIn(1);

        List<Prenotazione> prenotazioniPendenti = eventfy.mostraPrenotazioniPendentiGestore();

        //verifico che mi venga ritornata una lista 
        assertNotNull(prenotazioniPendenti);

        //verifico che la dimensione della lista sia corretta
        assertEquals(1, prenotazioniPendenti.size());
    }


}