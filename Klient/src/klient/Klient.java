package klient;



/** Główna klasa wywołująca klasę odpowiadajacą za wyświetlanie okna */


public class Klient extends Thread{

    /** Konstruktor klasy tworzący wątek klasy Okno i startujący go */
    public Klient()
    {
        Okno okno = new Okno();
        Thread thread  = new Thread(okno);
        thread.start();
    }
    
    
    
    /** Główna metoda main wywołująca Klasę klienta */
    
    public static void main(String[] args) throws InterruptedException
    {
        Klient klinet = new Klient();
        klinet.start();
    }
    
}
