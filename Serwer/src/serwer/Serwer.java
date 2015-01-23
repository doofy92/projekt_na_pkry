package serwer;

/*
    Funkcja wywolujaca wyswietlanie okna
*/
import java.io.IOException;


/**  klasa glowna odpowiedzialna za wywolanie watku klasy okno*/
public class Serwer {

    
    /**  konstruktor*/
    public Serwer() throws IOException
    {
        Okno okno = new Okno();
        Thread thread  = new Thread(okno);
        thread.start();
    }
    
     /**  główna klasa main*/
    public static void main(String[] args) throws IOException 
    {
        new Serwer();
    }
    
}
