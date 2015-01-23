package serwer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** klasa glowne polaczenie*/
public class GlownePolaczenie 
{
	/** obiekt klasy InetSocketAdress*/
    private InetSocketAddress inetSocektAdress = null;
    /** obiekt klasy ServerSocket */ 
    private ServerSocket serwerSocekt = null;
    /** obiekt klasy Socket*/
    private Socket polaczenie = null;
    
    /** konstruktor */
    public GlownePolaczenie(InetSocketAddress isa, ServerSocket ss,  Socket s)
    {
        this.inetSocektAdress = isa;
        this.serwerSocekt = ss;
        this.polaczenie = s; 
        
    }
    /** metoda zwracajaca polaczenie*/
    public Socket zwrocPolaczenie() throws IOException
    {
        try{
            serwerSocekt.bind(inetSocektAdress);
        } catch (IOException ex) {
            Logger.getLogger(GlownePolaczenie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        polaczenie = serwerSocekt.accept();
        return polaczenie;
    }
    /** metoda zamykajaca serwer*/
    public void serwerClose() throws IOException
    {
        serwerSocekt.close();
    }
}
