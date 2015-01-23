package serwer;


/*
    Klasa odpowiedzialana za wysylanie i odbieranie wiadomosci("trzymanie tcyh dwoch oiektow klas w jedym miejsu" ) ale dla ustalenia klucza 
    na wlasciwym porcie
*/
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Klasa odpowiedzialana za wysylanie i odbieranie wiadomosci("trzymanie tcyh dwoch oiektow klas w jedym miejsu" ) ale dla ustalenia klucza 
    na wlasciwym porcie*/
public class WysylanieIOdbieranieWiadomosciodKlienta extends Thread
{
    
    /** Lista obiektow klasy  WysylanieIOdbieranieWiadomosciodKlienta */
    private Socket polaczenie = null;
    /** Obiekt klasy InetSocketAddressy */
    private InetSocketAddress inetSocektAdress = null;
    
    /** Obiekt klasy ServerSocket */
    private ServerSocket serwerSocket = null;
    /** Obiekt klasy odbieranieWiadomosci*/
    private Nasluch odbieranieWiadomosci = null;
     /** Obiekt klasy WysylanieWiadomosci*/
    private WysylanieWiadomosci wysylanieWiadomosci = null;
    
    /** konstruktor*/
    public WysylanieIOdbieranieWiadomosciodKlienta(String port) throws IOException
    {
        System.out.println("Inicjacja odbierania polaczenia");
        this.inetSocektAdress = new InetSocketAddress("localhost", Integer.valueOf(port));
        this.serwerSocket = new ServerSocket();
        this.serwerSocket.bind(inetSocektAdress); 
        
    }
    
    /** metoda watku*/
    public void run()
    {
        
            try {
                this.polaczenie = serwerSocket.accept();
                this.odbieranieWiadomosci = new Nasluch(this.polaczenie);
                if(this.polaczenie == null){
                    System.out.println("ustawiany jest null do wyslania");
                }
                else
                {
                    System.out.println(" nie jest ustawiany null do wyslania");
                }
                this.wysylanieWiadomosci = new WysylanieWiadomosci(this.polaczenie);
                this.odbieranieWiadomosci.start();
            } catch (IOException ex) {
                Logger.getLogger(WysylanieIOdbieranieWiadomosciodKlienta.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Blad!!!!");
            }
    }
    
    
    /** metoda wysylajaca wiadomosc*/
    public void WyslijWiadomosc(String wiadomosc)
    {
        if(this.odbieranieWiadomosci.zwrocPolaczenie() == null)
        {
            System.out.println("polaczenie jest null");
        }
        else
        {
            System.out.println("Wiadomosc do wyslania do klineta to: " + wiadomosc);
        }
        WysylanieWiadomosci wys = new WysylanieWiadomosci(this.odbieranieWiadomosci.zwrocPolaczenie());
        wys.ustawTrescWiadomosci(wiadomosc);
        wys.start();
        
    }
    
    /** metoda zwracajaca wiadomosc od klineta*/
    public String wiadomoscOdKlienta()
    {
        return this.odbieranieWiadomosci.wiadomoscOdklienta();
    }
    
    /** metoda ustaiwajaca kontener na null*/
    public void ustawKontenrNaNull()
    {
        this.odbieranieWiadomosci.ustawKontenerNaWiadomoscOdklienta(null);
    }

    
     /** metoda zwracajaca Obiekt klasy socekt*/
    public Socket zworcPolaczenie()
    {
        return this.polaczenie;
    }
}
