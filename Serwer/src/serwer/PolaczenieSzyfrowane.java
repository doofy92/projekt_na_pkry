

package serwer;
/*
    Klasa odpowidzialana za nasluch na ustalonym porcie na polaczenie szyfrowane
*/
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

 /**   Klasa odpowidzialana za nasluch na ustalonym porcie na polaczenie szyfrowane*/
public class PolaczenieSzyfrowane extends Thread
{
    
    /**   obiekt klasy InetSocketAddress*/
    private InetSocketAddress inetSocektAdress = null;
    /**   obiekt klasy ServerSocket*/
    private ServerSocket serwerSocekt = null;
    /**   obiekt klasy socket*/
    private Socket soc = null;
    /**   zmienna przechowujaca port do komunikacji */
    private int portNasluchuZKlientami = 2340;
    /**   Obiket klasy kontenerNaPortyDlaKlinetow*/
    private KontenerNaPortyDlaKlientow kontenerNaPortyDlaKlinetow = null;
    /**   Obiket klasy istaKlientow*/
    private ListaKlientow listaKlientow;
     /**   Obiket klasy DataOutputStream*/
    private DataOutputStream dOut  = null;
    
    
    /**   konstruktor*/
    public PolaczenieSzyfrowane(KontenerNaPortyDlaKlientow kontenerNaPorty) throws IOException
    {
        this.inetSocektAdress = new InetSocketAddress("localhost", portNasluchuZKlientami);
        this.serwerSocekt = new ServerSocket();
        this.kontenerNaPortyDlaKlinetow = kontenerNaPorty;
        this.listaKlientow = new ListaKlientow();
        listaKlientow.start();
    }
    
    // watek naluchu 
    // jezeli jakis klient zglosi sie na tym porcie zostahe mu wyslny wolny port do komunkacji 
    // oraz zostanie on dodany do listy obslugiwanych klientow
    
    /**  watek naluchu 
     jezeli jakis klient zglosi sie na tym porcie zostahe mu wyslny wolny port do komunkacji 
     oraz zostanie on dodany do listy obslugiwanych klientow*/
    public void run()
    {
        Socket soc = null;
        int port;
         
        try {
            serwerSocekt.bind(this.inetSocektAdress);
        } catch (IOException ex) {
            Logger.getLogger(PolaczenieSzyfrowane.class.getName()).log(Level.SEVERE, null, ex);
        }
             while(true){
                 
                try{
                    
                    soc = serwerSocekt.accept();
                    dOut = new DataOutputStream(soc.getOutputStream());
                    port = this.kontenerNaPortyDlaKlinetow.zwrocWolnyPort();
                    listaKlientow.DodajKlienta(port);
                    byte[] portByte = String.valueOf(port).getBytes();
                    dOut.writeInt(portByte.length); // write length of the message
                    dOut.write(portByte);   
                    soc.close();
                    soc = null;
                    Thread.sleep(200);
                    
                 } catch (IOException ex) {
                Logger.getLogger(PolaczenieSzyfrowane.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(PolaczenieSzyfrowane.class.getName()).log(Level.SEVERE, null, ex);
            }
             }
         
        
    }
    
 
}
