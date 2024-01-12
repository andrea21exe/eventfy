package com.eventfy;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main(String[] args) {
        displayMenu();
        int scelta = getOperazioneUtente();
        processaScelta(scelta);
    }

    private static void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Opzione 1");
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
        return scanner.nextInt();
    }

    private static void processaScelta(int scelta) {
        switch (scelta) {
            case 1:
                System.out.println("Hai selezionato l'Opzione 1");
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
}
