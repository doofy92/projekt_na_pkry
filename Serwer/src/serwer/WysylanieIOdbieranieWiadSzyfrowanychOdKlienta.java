
package serwer;
/*
    Klasa odpowiedzialna za nasluch na wiaodmosci szyfrowanych od klineta 
*/
import static com.sun.org.apache.xml.internal.serialize.OutputFormat.Defaults.Encoding;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Klasa odpowiedzialna za nasluch na wiaodmosci szyfrowanych od klineta  */
public class WysylanieIOdbieranieWiadSzyfrowanychOdKlienta extends Thread{
    
    /** obiekt klasy socekt odpowiedzialny za polacznie gniazdka */
    private Socket polaczenie = null;
    /** obiekt klasy InetSocketAddress */
    private InetSocketAddress inetSocektAdress = null;
    /** obiekt klasy InetSocketAddress */
    private ServerSocket serwerSocket = null;
    /** zmiena przechowujaca port */
    private int port;
    
    /** Obiekt klasy DataInputStream*/
    private DataInputStream dIn = null;
     /** Obiekt klasy DataOutputStream*/
    private DataOutputStream dOut = null;
    /** zmienna przechowujaca dlugosc bitow*/
    private int dlugoscBitow;
    /** tablica bytow przechowujca wiadomosc od klineta*/
    private byte[] wiadomoscOdKlienta;
    
    
    /** Konstruktor*/
    public WysylanieIOdbieranieWiadSzyfrowanychOdKlienta(int port) throws IOException
    {
        this.inetSocektAdress = new InetSocketAddress("localhost", port);
        this.serwerSocket = new ServerSocket();
        this.serwerSocket.bind(inetSocektAdress); 
        this.port = port;
    }
    // watek nasluchu najpierw akceptcja na wlasciwym porcie potem czytanie wiadomosci przychodzacej i ustawinie jej
    
    /** watek nasluchu najpierw akceptcja na wlasciwym porcie potem czytanie wiadomosci przychodzacej i ustawinie jej*/
    public void run()
    {
        try {
            System.out.println("Czekam na zgloszenie sie kineta na porcie: " + this.port);
            this.polaczenie = serwerSocket.accept();
            dIn = new DataInputStream(this.polaczenie.getInputStream());
            dOut = new DataOutputStream(this.polaczenie.getOutputStream());
             System.out.println("akceptacja polaczenia na porcie " + this.port);
        } catch (IOException ex) {
            Logger.getLogger(WysylanieIOdbieranieWiadSzyfrowanychOdKlienta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(true){
            
            try {
                this.dlugoscBitow =  dIn.readInt();
            } catch (IOException ex) {
                Logger.getLogger(WysylanieIOdbieranieWiadSzyfrowanychOdKlienta.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    dIn.close();
                    dOut.close();
                } catch (IOException ex1) {
                    Logger.getLogger(WysylanieIOdbieranieWiadSzyfrowanychOdKlienta.class.getName()).log(Level.SEVERE, null, ex1);
                }catch(Exception e)
                {
                    System.out.println("1wtf?");
                    try {
                    dIn.close();
                    dOut.close();
                } catch (IOException ex1) {
                    Logger.getLogger(WysylanieIOdbieranieWiadSzyfrowanychOdKlienta.class.getName()).log(Level.SEVERE, null, ex1);
                }
                }
                System.out.println("wtf?");
            }
                
                this.wiadomoscOdKlienta = new byte[this.dlugoscBitow];

                if(this.dlugoscBitow>0) {

                    try {
                        dIn.readFully(this.wiadomoscOdKlienta, 0, this.wiadomoscOdKlienta.length); // read the message
                    } catch (IOException ex) {
                        Logger.getLogger(WysylanieIOdbieranieWiadSzyfrowanychOdKlienta.class.getName()).log(Level.SEVERE, null, ex);
                        
                    }
                }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(WysylanieIOdbieranieWiadSzyfrowanychOdKlienta.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
    
    
    /** metoda zwracajaca tablice bytow w ktorej znajduje sie wiadomosc od klienta*/
    public byte[] zwrocWiadomoscOdKlienta()
    {
        return this.wiadomoscOdKlienta;
    }
    
    /** metoda wysylajaca wiadmosc */
    public void wysylanieWiadomosci(byte[] wiadomosc) throws IOException
    {
        System.out.println("Wiadomosc wysylana na porcie: " + this.port + " dlugosc wiadomosci " + wiadomosc.length);
        dOut.writeInt(wiadomosc.length); 
        dOut.write(wiadomosc);
    }
    
    /** metoda ustawiajaca wiadomosc od klienta na null */
    public void ustawWiadomoscOdKlientaNaNull()
    {
        this.wiadomoscOdKlienta = null;
    }
    
    
}
