package serwer;
/*
    Klasa odpowiadajaca za nasluch na danym porcie wiadomosci od klineta
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/** klasa nasluchu */
public class Nasluch extends Thread
{
	/** obiekt klasy Socket*/
    private Socket polaczenie = null;
    /** obiekt klasy String */
    private String wiadomoscOdklienta = null;
    /** obiekt klasy BufferedReader*/
    private BufferedReader in = null;
    /** zmienna odpowiadajaca za nasluch*/
    private boolean czyNasluchiwac = true;
    /** metoda nasluchu */
    public Nasluch(Socket polaczenie)
    {   
        this.polaczenie = polaczenie;
        try {
            // obiekt potrzebny do odbierania wiadomsoci od klineta
                in = new BufferedReader(
                        new InputStreamReader(
                                polaczenie.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(Nasluch.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    // watek nasluchu wiadomosci od klineta
    /** watek nasluchu */
    public void run()
    {
        System.out.println("Nasluch dla klienta ustawiony");
        while(czyNasluchiwac){
            try {
                wiadomoscOdklienta= in.readLine();
                System.out.println("Wiadomosc od klineta to: " + wiadomoscOdklienta);
                Thread.sleep(200);
            } catch (IOException ex) {
                Logger.getLogger(Nasluch.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Nasluch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /** metoda zwracajaca wiadaomosc od klienta */
    public String wiadomoscOdklienta()
    {
        return wiadomoscOdklienta;
    }
    /** metoda zapisujaca wiadaomosc do klienta */
    public void ustawKontenerNaWiadomoscOdklienta(String wiadomosc)
    {
        this.wiadomoscOdklienta = wiadomosc;
    }
    /** metoda zwracajaca polaczenie */
    public Socket zwrocPolaczenie()
    {
        return this.polaczenie;
    }
    
}
