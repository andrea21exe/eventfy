package com.eventfy;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        
        Eventfy sistema = Eventfy.getIstanceEventfy();
        sistema.setUtenteCorrente(new Gestore("Andrea"));
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
        System.out.println("2. Opzione 2");
        System.out.println("3. Opzione 3");
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
                System.out.println("Hai selezionato l'Opzione 2");
                break;
            case 3:
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
}
