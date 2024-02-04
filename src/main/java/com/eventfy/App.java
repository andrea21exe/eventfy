package com.eventfy;

import java.util.Scanner;

public class App {

    private static Eventfy sistema;
    private static MenuStrategy menuStrategy;

    public static void main(String[] args) {

        sistema = Eventfy.getIstanceEventfy();
        sistema.populate();
        start();
        
    }

    private static void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Benvenuto! Seleziona un'opzione:");
        System.out.println("1. Accedi");
        System.out.println("2. Registrati");
        int scelta = scanner.nextInt();

        switch (scelta) {
            case 1:
                accedi();
                break;

            case 2:
                registrati();
                break;

            default:
                System.out.println("Scelta non valida.");
                return;
        }

        while (true) {
            menuStrategy.menu();
        }
    }

    private static void accedi() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci il tuo identificativo:");
        int id = scanner.nextInt();
        // Cerca l'utente con l'identificativo fornito
        Utente user = sistema.getUtente(id);
        if (user == null) {
            System.out.println("Utente non trovato.");
            return;
        }

        // Se l'utente esiste, si effettua il login...
        sistema.logIn(id);
        // ... ed si associa il corretto menuStrategy
        if (user instanceof Gestore) {
            menuStrategy = new MenuGestore();
        } else if (user instanceof Artista) {
            menuStrategy = new MenuArtista();
        } else {
            menuStrategy = new MenuFan();
        }
    }

    private static void registrati() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il tuo nome:");
        String nome = scanner.nextLine();

        System.out.println("Seleziona il tipo di utente:");
        System.out.println("1. Artista");
        System.out.println("2. Fan");
        System.out.println("3. Gestore");

        int iden;

        int tipoUtente = scanner.nextInt();
        switch (tipoUtente) {
            case 1:
                iden = sistema.signUpLogIn(new Artista(nome));
                menuStrategy = new MenuArtista();
                System.out.println("Il tuo identificativo e': " + iden);
                break;
            case 2:
                iden = sistema.signUpLogIn(new Fan(nome));
                menuStrategy = new MenuFan();
                System.out.println("Il tuo identificativo e': " + iden);
                break;
            case 3:
                iden = sistema.signUpLogIn(new Gestore(nome));
                menuStrategy = new MenuGestore();
                System.out.println("Il tuo identificativo e': " + iden);
                break;

            default:
                System.out.println("Tipo di utente non valido.");
                return;
        }        


        System.out.println("Registrazione avvenuta con successo!");

    }

}