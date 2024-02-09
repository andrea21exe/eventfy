package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eventfy {

    private static Eventfy sistema; // Singleton

    private Utente utenteCorrente;
    private Impianto impiantoCorrente;
    private Prenotazione prenotazioneCorrente;
    // private Invito invitoCorrente;

    private List<Impianto> listaImpianti;
    private Map<Integer, Impianto> mappaImpiantiTemp;
    private Map<Integer, Prenotazione> mappaPrenotazioniTemp;

    private Map<Integer, Utente> mappaUtenti;

    // UC2-UC3
    private Map<Integer, Prenotazione> mappaPrenotazioniPendenti;
    private Map<Integer, Prenotazione> mappaPrenotazioniAccettate;

    // UC6-UC7
    private Map<Integer, Utente> mappaUtentiTemp;

    // UC9
    // Recensione recensioneCorrente;

    // UC10
    private Map<Integer, Prenotazione> mappaPrenotazioniAnnullate;
    // UC12
    // private List<Evento> listaEventi;

    // Singleton
    private Eventfy() {
        listaImpianti = new ArrayList<Impianto>();
        mappaUtenti = new HashMap<Integer, Utente>();
        mappaPrenotazioniAccettate = new HashMap<Integer, Prenotazione>();
        mappaPrenotazioniPendenti = new HashMap<Integer, Prenotazione>();
        mappaPrenotazioniAnnullate = new HashMap<Integer, Prenotazione>();
    }

    public static Eventfy getIstanceEventfy() {
        if (sistema == null) {
            sistema = new Eventfy();
        }
        return sistema;
    }
    // -------

    public Impianto nuovoImpianto(String nome, String luogo, int capienza, int superficie) throws Exception {
        impiantoCorrente = new Impianto(nome, luogo, capienza, superficie, utenteCorrente);
        if (!isUnico(impiantoCorrente)) {
            impiantoCorrente = null;
            throw new Exception("Impianto identico presente nel sistema");
        }
        return impiantoCorrente;
    }

    public void confermaImpianto() {
        listaImpianti.add(impiantoCorrente);
        impiantoCorrente = null;
    }

    public List<Impianto> nuovaPrenotazione(String titolo, String descrizione, int durata, int capienza_min,
            LocalDate data, LocalTime ora) throws Exception {

        prenotazioneCorrente = new Prenotazione(titolo, descrizione, durata, data, ora, (Artista) utenteCorrente,
                impiantoCorrente); // inizialmente impianto corrente è null

        if (prenotazioneCorrente.hasEventoScaduto()) {
            prenotazioneCorrente = null;
            throw new Exception("Evento già scaduto");
        }

        mappaImpiantiTemp = new HashMap<Integer, Impianto>();

        for (Impianto impianto : listaImpianti) {
            if (impianto.maggioreUgualeDi(capienza_min)) {
                mappaImpiantiTemp.put(impianto.getId(), impianto);
            }
        }

        return new ArrayList<Impianto>(mappaImpiantiTemp.values());

    }

    public void prenotaImpianto(int codice_impianto) throws Exception {
        Impianto impianto = mappaImpiantiTemp.get(codice_impianto);
        if (impianto == null) {
            prenotazioneCorrente = null;
            throw new Exception("Hai inserito un ID dell'impianto scorretto");
        }
        prenotazioneCorrente.setImpianto(impianto);
    }

    public Prenotazione confermaPrenotazione() {
        int codice_prenotazione = prenotazioneCorrente.getId();
        mappaPrenotazioniPendenti.put(codice_prenotazione, prenotazioneCorrente);
        mappaImpiantiTemp = null;
        prenotazioneCorrente = null;
        return mappaPrenotazioniPendenti.get(codice_prenotazione);
    }

    public List<Prenotazione> mostraPrenotazioniPendenti() {

        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();
        for (int key : mappaPrenotazioniPendenti.keySet()) {
            Prenotazione p = mappaPrenotazioniPendenti.get(key);
            if (p.hasGestore((Gestore) utenteCorrente)) {
                // Controllo e annullamento automatico prenotazioni scadute
                if (p.hasEventoScaduto()) {
                    annullaPrenotazione(p);
                } else {
                    mappaPrenotazioniTemp.put(p.getId(), p);
                }
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    public Prenotazione selezionaPrenotazionePendente(int codice_prenotazione) throws Exception {

        prenotazioneCorrente = mappaPrenotazioniTemp.get(codice_prenotazione);
        if (prenotazioneCorrente == null) {

            throw new Exception("Hai inserito un ID della prenotazione errato");
        }
        return prenotazioneCorrente;
    }

    public void accettaPrenotazione() throws Exception {

        // Check che non ci sia una prenotazione nello stesso impianto nella stessa data
        List<Prenotazione> prenotazioniAccettateGestore = mostraPrenotazioniAccettateGestore();
        for (Prenotazione p : prenotazioniAccettateGestore) {
            if (p.hasImpianto(prenotazioneCorrente.getImpianto())
                    && p.hasDataEvento(prenotazioneCorrente.getDataEvento())) {
                mappaPrenotazioniTemp = null;
                prenotazioneCorrente = null;
                throw new Exception("Collisione data");
            }
        }

        int codice_prenotazione = prenotazioneCorrente.getId();
        mappaPrenotazioniAccettate.put(codice_prenotazione, prenotazioneCorrente);
        mappaPrenotazioniPendenti.remove(codice_prenotazione);
        mappaPrenotazioniTemp = null;
        prenotazioneCorrente = null;

    }

    public Impianto getImpiantoCorrente() {
        return impiantoCorrente;
    }

    public void setUtenteCorrente(Utente utente) {
        utenteCorrente = utente;
    }

    public List<Impianto> getListaImpianti() {
        return this.listaImpianti;
    }

    public Utente getUtenteCorrente() {
        return this.utenteCorrente;
    }

    public Prenotazione getPrenotazioneCorrente() {
        return this.prenotazioneCorrente;
    }

    public Map<Integer, Prenotazione> getPrenotazioniPendenti() {
        return mappaPrenotazioniPendenti;
    }

    public Map<Integer, Prenotazione> getPrenotazioniAccettate() {
        return mappaPrenotazioniAccettate;
    }

    public void logIn(int id) {
        utenteCorrente = mappaUtenti.get(id);
    }

    public Map<Integer, Prenotazione> getMapPrenotazioniTemp() {
        return mappaPrenotazioniTemp;
    }

    public Utente getUtente(int id) {
        return mappaUtenti.get(id);
    }

    public Map<Integer, Prenotazione> getMappaPrenotazioniAnnullate() {
        return mappaPrenotazioniAnnullate;
    }

    // CI SERVE UN MODO PER METTERE A NULL LA PRENOTAZIONE CORRENTE UNA VOLTA
    // CONCLUSO L'UC6
    public void setPrenotazioneCorrenteNull() {
        prenotazioneCorrente = null;
    }

    // EFFETTUA LA REGISTRAZIONE ED IL "LOG-IN"
    public int signUpLogIn(Utente utente) {
        mappaUtenti.put(utente.getId(), utente);
        utenteCorrente = utente;
        return utente.getId();
    }

    public Impianto getImpianto(int id) {
        for (Impianto i : listaImpianti) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    // Simile al metodo "mostraprenotazioniaccettate" sotto
    // UC6-----------------------------------------------------
    public List<Prenotazione> aggiungiScaletta() {

        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p = mappaPrenotazioniAccettate.get(key);
            if (utenteCorrente instanceof Artista) {

                if (p.hasArtista((Artista) utenteCorrente)) {
                    mappaPrenotazioniTemp.put(p.getId(), p);
                }
            }
        }

        return new ArrayList<>(mappaPrenotazioniTemp.values());
    }

    // ho sostituito la seconda operazione aggiungiScaletta2 con questa
    public List<Brano> recuperaBraniArtista(int codice_prenotazione) {

        prenotazioneCorrente = mappaPrenotazioniTemp.get(codice_prenotazione);
        mappaPrenotazioniTemp = null;

        if (utenteCorrente instanceof Artista) {
            // L'utente corrente è già quello della prenotazione corrente
            // utenteCorrente = prenotazioneCorrente.getArtista();
            return ((Artista) utenteCorrente).getListaBrani();
        }

        return null;

    }

    // supponiamo di visualizzare la mappa dei brani con i rispettivi codici e di
    // scegliere un codice di un brano
    public void aggiungiBrano(int codice_brano) throws Exception {
        prenotazioneCorrente.addBrano(codice_brano);
        prenotazioneCorrente = null;
    }

    // UC7-------------------------------------------------------
    // SImile al metodo "aggiungiscaletta" sopra. In astah abbiamo chiamato questo
    // metodo "invitaArtista"
    public List<Prenotazione> mostraPrenotazioniAccettate() {

        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p = mappaPrenotazioniAccettate.get(key);
            if (p.hasArtista((Artista) utenteCorrente)) {
                mappaPrenotazioniTemp.put(p.getId(), p);
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());

    }

    public List<Utente> selezionaPrenotazioneInvito(int codice_prenotazione) {

        prenotazioneCorrente = mappaPrenotazioniTemp.get(codice_prenotazione);
        prenotazioneCorrente.creaInvito();

        mappaUtentiTemp = new HashMap<Integer, Utente>();

        for (Utente utente : mappaUtenti.values()) {
            if (utente instanceof Artista && !utente.equals(utenteCorrente)) {
                mappaUtentiTemp.put(utente.getId(), utente);
            }
        }

        return new ArrayList<Utente>(mappaUtentiTemp.values());

    }

    public void invitaArtista(int codice_artista) throws Exception {

        Utente utenteSelezionato = mappaUtenti.get(codice_artista);

        if (!(utenteSelezionato instanceof Artista) || utenteSelezionato == null) {
            throw new Exception("L'artista non esiste");
        }

        prenotazioneCorrente.setDestinatarioInvito((Artista) utenteSelezionato);
        // L'invito va inserito nell'artista da invitare
        mappaUtentiTemp = null;
        prenotazioneCorrente = null;
    }

    // UC8 ----------------------------------------------------
    public List<Invito> gestisciInvito() {
        return ((Artista) utenteCorrente).getListaInvitiPendenti();
    }

    // seleziono un id dalla mappa inviti relativa all'artista
    public void accettaInvito(int codice_invito) throws Exception {
        ((Artista) utenteCorrente).accettaInvito(codice_invito);
    }

    // aggiunta dall'estensione
    public void rifiutaInvito(int codice_invito) throws Exception {
        ((Artista) utenteCorrente).rifiutaInvito(codice_invito);
    }

    // UC9
    // Non tutte le prenotazioni sono recensibili!!!!! solo quelle per le quali
    // l'artista si è già esibito
    public List<Prenotazione> inserisciRecensione() {
        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p = mappaPrenotazioniAccettate.get(key);

            if (p.hasArtista((Artista) utenteCorrente) && p.isRecensibile()) {
                mappaPrenotazioniTemp.put(p.getId(), p);
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    // recuperiamo l'impianto a cui vogliamo aggiungere un commento relativo alla
    // prenotazione
    // inserisco codice prenotazione
    public void creaRecensione(int codice_prenotazione, String commento, int voto) throws Exception {

        Prenotazione p = mappaPrenotazioniTemp.get(codice_prenotazione);
        mappaPrenotazioniTemp = null;

        if (p == null) {
            throw new Exception("Id non valido");
        }

        p.creaRecensione(commento, voto);

    }

    // UC10
    public List<Prenotazione> eliminaPrenotazione() {
        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p1 = mappaPrenotazioniAccettate.get(key);
            if (p1.hasArtista((Artista) utenteCorrente) && p1.isEliminabile()) {
                mappaPrenotazioniTemp.put(p1.getId(), p1);
            }
        }
        for (int key : mappaPrenotazioniPendenti.keySet()) {
            Prenotazione p2 = mappaPrenotazioniPendenti.get(key);
            if (p2.hasArtista((Artista) utenteCorrente) && p2.isEliminabile()) {
                mappaPrenotazioniTemp.put(p2.getId(), p2);
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    // Supponiamo di aver inserito l'id di una prenotazione ricavato dalla lista
    // delle prenotazioni che ci siamo ritornati

    // utilizzata acnhe nell'estensione del caso d'uso UC3
    public void confermaEliminazione(int codice_prenotazione) throws Exception {

        Prenotazione daEliminare = mappaPrenotazioniTemp.get(codice_prenotazione);

        if (daEliminare == null) {
            throw new Exception("Id non valido");
        }

        if (daEliminare != null) {
            annullaPrenotazione(daEliminare);
        }

        mappaPrenotazioniTemp = null;

    }

    // UC12

    // Da aggiungere questa funzione in astah, mostra gli artisti che hanno almeno
    // una prenotazione accettata e di conseguenza un evento
    // ritorna solo gli artisti che hanno almeno una prenotazione accettata
    public List<Utente> mostraArtistiEventi() {
        mappaUtentiTemp = new HashMap<Integer, Utente>();

        for (Utente utente : mappaUtenti.values()) {
            if (utente instanceof Artista) {
                for (int key : mappaPrenotazioniAccettate.keySet()) {
                    Prenotazione p = mappaPrenotazioniAccettate.get(key);
                    if (p.hasArtista((Artista) utente)) {
                        mappaUtentiTemp.put(utente.getId(), utente);
                        break;
                    }
                }
            }
        }
        return new ArrayList<Utente>(mappaUtentiTemp.values());
    }

    public List<Prenotazione> partecipaEvento(int codice_artista) throws Exception {

        Artista a = (Artista) mappaUtentiTemp.get(codice_artista);
        if (a == null) {
            mappaUtentiTemp = null;
            throw new Exception("Id non valido");
        }

        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();
        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p1 = mappaPrenotazioniAccettate.get(key);

            if (p1.hasArtista(a) && p1.isPartecipabile()) {
                mappaPrenotazioniTemp.put(p1.getId(), p1);
                // listaEventi.add(p1.getEvento());
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    public void confermaPartecipazione(int id_evento) throws Exception {

        Prenotazione p = mappaPrenotazioniTemp.get(id_evento);
        mappaPrenotazioniTemp = null;
        mappaUtentiTemp = null;

        if (p == null) {
            throw new Exception("Id non valido");
        }

        p.addPartecipazioneFan((Fan) utenteCorrente);

    }

    public Prenotazione getPrenotazione(int codice_prenotazione) {

        Prenotazione p = mappaPrenotazioniAccettate.get(codice_prenotazione);
        if (p == null) {
            p = mappaPrenotazioniPendenti.get(codice_prenotazione);
        }
        return p;
    }

    // UC13
    public List<Evento> inserisciRecensioneEvento() {
        if (utenteCorrente instanceof Fan) {
            return ((Fan) utenteCorrente).getListaEventiRecensibili();
        }
        return null;
    }

    public void confermaRecensioneEvento(int id_evento, String commento, int voto) throws Exception {

        Prenotazione p = mappaPrenotazioniAccettate.get(id_evento);

        // Se:
        // 1. la prenotazione è tra quelle accettate
        // 2. l'utente "è partecipante..."
        // 3. la prenotazione/evento è recensibile --->
        // ---> Viene creata la recensione

        if (p == null) {
            throw new Exception("Id non valido");
        }

        if (!(p.hasPartecipante((Fan) utenteCorrente))) {
            throw new Exception("Utente non partecipante");
        }

        if (!(p.isRecensibile())) {
            throw new Exception("Evento non recensibile");
        }

        p.creaRecensione(commento, voto, (Fan) utenteCorrente);

    }

    // UC4
    public List<Prenotazione> mostraPrenotazioniAccettateGestore() {

        ArrayList<Prenotazione> prenotazioniAccettate = new ArrayList<Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p1 = mappaPrenotazioniAccettate.get(key);
            if (p1.hasGestore((Gestore) utenteCorrente)) {

                prenotazioniAccettate.add(p1);
            }
        }

        return prenotazioniAccettate;
    }

    // UC5
    public void visualizzaEventiOrganizzati() {

        Prenotazione p1;
        int i = 0;
        for (int key : mappaPrenotazioniAccettate.keySet()) {
            p1 = mappaPrenotazioniAccettate.get(key);
            if (p1.hasArtista((Artista) utenteCorrente)) {
                p1.printEvento();
                i++;
            }
        }
        if (i == 0) {
            System.out.println("Nessun evento");
        }
    }

    // UC14
    public List<Prenotazione> mostraPrenotazioniPendentiGestore() {
        ArrayList<Prenotazione> prenotazioniPendenti = new ArrayList<Prenotazione>();

        for (int key : mappaPrenotazioniPendenti.keySet()) {
            Prenotazione p1 = mappaPrenotazioniPendenti.get(key);
            if (p1.hasGestore((Gestore) utenteCorrente)) {

                prenotazioniPendenti.add(p1);
            }
        }

        return prenotazioniPendenti;

    }

    // UC15
    public void mostraRecensioneImpianti(int id) {
        for (Impianto im : listaImpianti) {
            if (im.hasGestore((Gestore) utenteCorrente) && im.getId() == id) {
                im.printRecensioni();
            }
        }
    }

    // UC16
    // faccio inserire l'id dell'evento di cui voglio visualizzare le recensioni
    public void mostraRecensioneEvento(int id) throws Exception {

        Prenotazione p1 = mappaPrenotazioniAccettate.get(id);

        if (p1 == null || !(p1.hasArtista((Artista) utenteCorrente))) {
            throw new Exception("Evento non valido");
        }

        p1.printRecensioniEvento();

    }

    // UC17
    // Registra Brano
    public void registraBrano(String titolo, String album, int durata) {
        ((Artista) utenteCorrente).nuovoBrano(titolo, album, durata);
    }

    public List<Brano> visualizzaBrani() {
        return ((Artista) utenteCorrente).getListaBrani();
    }

    public void logout() {
        utenteCorrente = null;
    }

    // POPOLA LE MAPPE/LISTE
    public void populate() {

        // Popola la mappa degli utenti
        Gestore g1 = new Gestore("Gigi"); // id 0
        mappaUtenti.put(g1.getId(), g1);
        Gestore g2 = new Gestore("Paolo"); // id 1
        mappaUtenti.put(g2.getId(), g2);
        Gestore g3 = new Gestore("Marco"); // id 2
        mappaUtenti.put(g3.getId(), g3);
        Artista a1 = new Artista("THEWEEKND"); // id 3
        mappaUtenti.put(a1.getId(), a1);
        Artista a2 = new Artista("SZA"); // id 4
        mappaUtenti.put(a2.getId(), a2);
        Artista a3 = new Artista("DojaCat"); // id 5
        mappaUtenti.put(a3.getId(), a3);
        Fan f1 = new Fan("Max"); // id 6
        mappaUtenti.put(f1.getId(), f1);
        Fan f2 = new Fan("giulio"); // id 7
        mappaUtenti.put(f2.getId(), f2);

        a1.nuovoBrano("Starboy", "Starboy", 3); // id 1
        a1.nuovoBrano("Sacrifice", "Starboy", 3); // id 2
        a1.nuovoBrano("Out Of Time", "Dawn FM", 3); // id 3
        a2.nuovoBrano("Good Days", "SOS", 3); // id 4
        a2.nuovoBrano("Kill Bill", "SOS", 3); // id 5
        a3.nuovoBrano("Agora Hills", "Album D", 3); // id 6

        // Popola la lista impianti
        Impianto i1 = new Impianto("San Siro", "Milano", 75000, 700, g1);
        listaImpianti.add(i1);
        Impianto i2 = new Impianto("Pal1", "Catania", 3000, 200, g3);
        listaImpianti.add(i2);
        Impianto i3 = new Impianto("Stadio Arena", "Roma", 20000, 500, g2);
        listaImpianti.add(i3);
        Impianto i4 = new Impianto("Ippodromo C", "Bari", 45000, 1000, g1);
        listaImpianti.add(i4);

        // Popola le prenotazioni
        Prenotazione p0 = new Prenotazione("P0", "d0", 20, LocalDate.of(2059, 12, 1), LocalTime.now(), a3, i1);
        Prenotazione p1 = new Prenotazione("P1", "d1", 203, LocalDate.of(2028, 12, 1), LocalTime.now(), a1, i2);
        Prenotazione p2 = new Prenotazione("P2", "d2", 120, LocalDate.of(2049, 12, 1), LocalTime.now(), a2, i3);
        Prenotazione p3 = new Prenotazione("P3", "d3", 130, LocalDate.of(2027, 10, 1), LocalTime.now(), a1, i4);
        Prenotazione p4 = new Prenotazione("P4", "d4", 130, LocalDate.of(2025, 2, 1), LocalTime.now(), a1, i4);
        Prenotazione p5 = new Prenotazione("P5", "d5", 130, LocalDate.of(2026, 7, 1), LocalTime.now(), a1, i1);
        Prenotazione p6 = new Prenotazione("P6", "d6", 320, LocalDate.of(2022, 12, 1), LocalTime.now(), a3, i3);
        Prenotazione p7 = new Prenotazione("P7", "d7", 320, LocalDate.of(2025, 12, 1), LocalTime.now(), a3, i3);
        Prenotazione p8 = new Prenotazione("P8", "d8", 320, LocalDate.of(2021, 12, 1), LocalTime.now(), a1, i3);
        // la p9 verrà eliminata nel test
        Prenotazione p9 = new Prenotazione("P9", "d9", 320, LocalDate.of(2025, 6, 1), LocalTime.now(), a1, i3);
        Prenotazione p10 = new Prenotazione("P10", "d10", 320, LocalDate.of(2024, 11, 11), LocalTime.now(), a1, i3);
        Prenotazione p11 = new Prenotazione("P11", "d11", 120, LocalDate.of(2022, 11, 11), LocalTime.now(), a1, i3);
        Prenotazione p12 = new Prenotazione("P12", "d12", 40, LocalDate.of(2025, 5, 11), LocalTime.now(), a1, i3);
        Prenotazione p13 = new Prenotazione("P13", "d13", 40, LocalDate.of(2059, 12, 1), LocalTime.now(), a3, i1);

        mappaPrenotazioniPendenti.put(p0.getId(), p0);
        mappaPrenotazioniPendenti.put(p1.getId(), p1);
        mappaPrenotazioniPendenti.put(p7.getId(), p7);
        mappaPrenotazioniPendenti.put(p13.getId(), p13);
        mappaPrenotazioniAccettate.put(p2.getId(), p2);
        mappaPrenotazioniAccettate.put(p3.getId(), p3);
        mappaPrenotazioniAccettate.put(p4.getId(), p4);
        mappaPrenotazioniAccettate.put(p5.getId(), p5);
        mappaPrenotazioniAccettate.put(p6.getId(), p6);
        mappaPrenotazioniAccettate.put(p8.getId(), p8);
        mappaPrenotazioniAccettate.put(p9.getId(), p9);
        mappaPrenotazioniAccettate.put(p10.getId(), p10);
        mappaPrenotazioniAccettate.put(p11.getId(), p11);
        mappaPrenotazioniAccettate.put(p12.getId(), p12);

        // Inviti (A1 è il mittente di 2 inviti e destinatario di 2 inviti)

        Invito inv1 = new Invito(p3.getEvento(), p3.getArtista(), a2);
        Invito inv2 = new Invito(p4.getEvento(), p4.getArtista(), a2);

        Invito inv3 = new Invito(p2.getEvento(), p2.getArtista(), a1);
        Invito inv4 = new Invito(p5.getEvento(), p5.getArtista(), a3);

        a1.addInvitoPendente(inv3);
        a2.addInvitoPendente(inv1);
        a2.addInvitoPendente(inv2);
        a3.addInvitoPendente(inv4);

        // Popola le recensioni
        try {
            i1.recensisci("Commento 1", 4, a1);
            i2.recensisci("Commento 2", 5, a2);
            i3.recensisci("Commento 3", 3, a3);
        } catch (Exception e) {
        }

        try {
            p1.addPartecipazioneFan(f1);
            p2.addPartecipazioneFan(f2);
            p11.addPartecipazioneFan(f1);
            p12.addPartecipazioneFan(f1);
        } catch (Exception e) {
        }

        try {
            p1.creaRecensione("bell' evento", 4, f1);
            p2.creaRecensione("mal gestito", 1, f2);
        } catch (Exception e) {
        }
    }

    // UC18
    public List<Impianto> visualizzaImpiantiGestore() {

        ArrayList<Impianto> listaImpiantiGestore = new ArrayList<Impianto>();

        for (Impianto im : listaImpianti) {
            if (im.hasGestore((Gestore) utenteCorrente)) {
                listaImpiantiGestore.add(im);
            }
        }

        return listaImpiantiGestore;
    }

    // UC19
    public List<Prenotazione> visualizzaPrenotazioniPendentiArtista() {

        ArrayList<Prenotazione> prenotazioniPendenti = new ArrayList<Prenotazione>();
        for (int key : mappaPrenotazioniPendenti.keySet()) {
            Prenotazione p1 = mappaPrenotazioniPendenti.get(key);
            if (p1.hasArtista((Artista) utenteCorrente)) {
                prenotazioniPendenti.add(p1);
            }
        }

        return prenotazioniPendenti;

    }

    // per la modifica fatta sugli inviti devo ritornarmi la mappa utenti
    public Map<Integer, Utente> getMappaUtenti() {
        return mappaUtenti;
    }

    // Annulla prenotazione
    private void annullaPrenotazione(Prenotazione p) {
        if (mappaPrenotazioniPendenti.remove(p.getId()) == null) {
            mappaPrenotazioniAccettate.remove(p.getId());
        }

        mappaPrenotazioniAnnullate.put(p.getId(), p);
    }

    // Visualizza inviti accettati di cui l'utente corrente è mittente
    // Nella UI viene prima chiamato il metodo mostraPrenotazioniAccettate()

    // UC20
    public void mostraInvitiAccettati(int idEvento) throws Exception {

        Prenotazione p = mappaPrenotazioniTemp.get(idEvento);
        mappaPrenotazioniTemp = null;

        if (p == null) {
            throw new Exception("Id non valido");
        }

        p.printInvitiAccettati();

    }

    private boolean isUnico(Impianto impianto) {

        for (Impianto i : this.listaImpianti) {
            if (i.hasSameAttributes(impianto)) {
                return false;
            }
        }

        return true;

    }

    // UC21
    public void mostraRecensioniFan() {
        ((Fan) utenteCorrente).printRecensioni();
    }

    // UC22
    public void mostraRecensioniArtista() {
        for (Impianto impianto : listaImpianti) {
            impianto.printRecensioniArtista((Artista) utenteCorrente);
        }
    }

}
