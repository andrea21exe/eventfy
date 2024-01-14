package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        
        Eventfy sistema = Eventfy.getIstanceEventfy();
        sistema.populate();
        sistema.signUpLogIn(new Gestore("Andrea"));

        menu();
    }

    private static void menu(){
        displayMenu();
        int scelta = getOperazioneUtente();
        processaScelta(scelta);
    }

    private static void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Inserisci un nuovo impianto");
        System.out.println("2. Richiedi prenotazione impianto");
        System.out.println("3. Gestisci prenotazione");
    }

    private static int getOperazioneUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il numero dell'opzione desiderata: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Inserisci un numero valido.");
            scanner.next();
        }

        int i = scanner.nextInt();
        //scanner.close();
        return i;
    }

    private static void processaScelta(int scelta) {
        switch (scelta) {
            case 1:
                inserisciImpianto();
                break;
            case 2:
                nuovaPrenotazione();
                System.out.println("Hai selezionato l'Opzione 2");
                break;
            case 3:
                gestisciPrenotazioni();
                System.out.println("Hai selezionato l'Opzione 3");
                break;
            default:
                System.out.println("Opzione non valida");
        }
    }

    private static void inserisciImpianto() {

        Eventfy sistema = Eventfy.getIstanceEventfy();
        Scanner input = new Scanner(System.in);

        //Inserisci dati impianto
        System.out.println("Hai selezionato: Inserisci nuovo impianto");
        System.out.println("Inserisci il nome dell'impianto"); 
        String nome = input.nextLine(); //nome

        System.out.println("Inserisci il luogo dell'impianto");
        String luogo = input.nextLine(); //luogo

        //capienza (int)
        System.out.println("Inserisci la capienza (intero)");
        while(!input.hasNextInt()){
            System.out.println("Capienza deve essere un numero intero, riprova");
            input.next();
        }
        int capienza = input.nextInt();

        //superficie (int)
        System.out.println("Inserisci la superficie (intero)");
        while(!input.hasNextInt()){
            System.out.println("Superficie deve essere un numero intero, riprova");
            input.next();
        }
        int superficie = input.nextInt();
        input.nextLine();

        //Visualizza il riepilogo dell'impianto inserito
        Impianto impianto = sistema.nuovoImpianto(nome, luogo, capienza, superficie);
        System.out.println("");
        System.out.println("Riepilogo impianto:");
        System.out.println(impianto.toString());
        System.out.println("");

        System.out.println("Premi invio per confermare l'inserimento");
        System.out.println("");
        input.nextLine();

        //Conferma l'inserimento
        sistema.confermaImpianto();        

        //Ritorna al menù
        menu();
    }

    public static void nuovaPrenotazione(){

        Eventfy sistema = Eventfy.getIstanceEventfy();
        Scanner input = new Scanner(System.in);
        
        sistema.signUpLogIn(new Artista("Andrea"));

        System.out.println("Hai selezionato: Inserisci nuova prenotazione");
        System.out.println("Inserisci il titolo dell'evento"); 
        String titolo = input.nextLine(); //titolo

        System.out.println("Inserisci una descrizione dell'evento");
        String descrizione = input.nextLine(); //descrizione

        System.out.println("Per quante ore vuoi prenotare l'impianto?");
        while(!input.hasNextInt()){
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int durata = input.nextInt(); //durata

        System.out.println("Qual è la capienza minima dell'impianto in cui vuoi esibirti?");
        while(!input.hasNextInt()){
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int capienza = input.nextInt(); //capienza
        input.nextLine();


        System.out.println("Quando vuoi esibirti? Inserisci la data nel formato dd.mm.yyyy");
        String dataString = input.nextLine(); //data
        LocalDate data = null;
        try {
            // Dividi la stringa in giorno, mese e anno
            String[] giornoMeseAnno = dataString.split("\\.");

            // Estrai giorno, mese e anno come interi
            int giorno = Integer.parseInt(giornoMeseAnno[0]);
            int mese = Integer.parseInt(giornoMeseAnno[1]);
            int anno = Integer.parseInt(giornoMeseAnno[2]);

            data = LocalDate.of(anno, mese, giorno);

        } catch (Exception e) {
            System.out.println("Formato non valido");
        }

        System.out.println("Inserisci l'ora nel formato hh.mm");
        String oraString = input.nextLine(); //ora
        LocalTime orario = null;
        try {
            // Dividi la stringa in ora, minuti
            String[] oraMinuti = oraString.split("\\.");

            // Estrai ora minuti come interi
            int ora = Integer.parseInt(oraMinuti[0]);
            int minuti = Integer.parseInt(oraMinuti[1]);

            orario = LocalTime.of(ora, minuti);

        } catch (Exception e) {
            System.out.println("Formato non valido");
        }
        
        //Si DOVREBBE verificare che i vari parametri siano validi
        List<Impianto> impiantiDisponibili = sistema.nuovaPrenotazione(titolo, descrizione, durata, capienza, data, orario);

        //Stampa la lista di impianti disponibili
        for(Impianto i : impiantiDisponibili){
            System.out.println(i);
        }

        System.out.println("");
        System.out.println("Inserisci l'ID dell'impianto di cui vuoi richiedere la prenotazione");
        while(!input.hasNextInt()){
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int id = input.nextInt();
        input.nextLine();

        sistema.prenotaImpianto(id);
        System.out.println("");
        System.out.println("Premi invio per confermare la prenotazione");
        input.nextLine();
        System.out.println(sistema.confermaPrenotazione());

        menu();

    }

    public static void gestisciPrenotazioni(){
        Eventfy sistema = Eventfy.getIstanceEventfy();
        Scanner input = new Scanner(System.in);
        // Ottiene la lista delle prenotazioni pendenti dal sistema
        List<Prenotazione> prenotazioniPendenti=sistema.mostraPrenotazioniPendenti();
       // Se non ci sono prenotazioni pendenti, torna al menu principale
        if (prenotazioniPendenti == null || prenotazioniPendenti.isEmpty()) {
            System.out.println("Nessuna prenotazione pendente.");
            menu();  // Torna al menu principale se non ci sono prenotazioni pendenti.
            return;
        }
        // Stampa le prenotazioni pendenti
        System.out.println("Prenotazioni Pendenti");
        for(Prenotazione p: prenotazioniPendenti){
            System.out.println(p);
        }
         // Chiede all'utente di inserire il codice della prenotazione da gestire
        System.out.println("Inserisci il codice della prenotazione da gestire:");
        while(!input.hasNextInt()){
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int codice_prenotazione=input.nextInt();
       // Se la prenotazione è valida
        Prenotazione p=sistema.selezionaPrenotazionePendente(codice_prenotazione);
        if(p!=null){
            System.out.println("Hai selezionato la prenotazione:"+p);
            System.out.println("Vuoi accettare questa Prenotazione?(Si/No)");
            String risp=input.next().toLowerCase();
            if (risp.equals("si")) {
                sistema.accettaPrenotazione();
                System.out.println("Prenotazione accettata!");
            } else {
                System.out.println("Operazione annullata.");
            }
            // Se la prenotazione non è valida
        } else {
            System.out.println("Prenotazione non valida.");
        }

        //Ritorna al menù
        menu();
    }

}
