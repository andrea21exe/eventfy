package com.eventfy;

import java.util.Scanner;

public abstract class MenuStrategy {

    protected static Eventfy sistema;

    public MenuStrategy(){
        sistema = Eventfy.getIstanceEventfy();
    }

    public void menu() {
        displayMenu();
        int scelta = getOperazioneUtente();
        processaScelta(scelta);
    }

    abstract void displayMenu();

    protected int getOperazioneUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il numero dell'opzione desiderata: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Inserisci un numero valido.");
            scanner.next();
        }

        int i = scanner.nextInt();
        // scanner.close();
        return i;
    }

    abstract void processaScelta(int scelta);
}

   