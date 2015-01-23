package serwer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/**  Klasa odpowiadajaca za wyswieltanie okna aplikacji */

public class Okno extends JFrame implements Runnable
{
    /** pole pokazujace tekst pierwszego klienta*/
    private JTextArea poleTekstoweDlaPierwszegoKlienta = new JTextArea(5,20);
    /** pole pokazujace tekst drugiego klienta*/
    private JTextArea poleTekstoweDlaDrugiegoKlienta = new JTextArea(5,20);
    /** pole pokazujace tekst trzeciego klienta*/
    private JTextArea poleTekstoweDlaTrzeciegoKlienta = new JTextArea(5,20);
    
    private JPanel panel = new JPanel();
    
    //Sockety zwiazane z odbieranie polaczen od klientow na porcie 2300
    /** obiekt przechowujacy socket*/
    private InetSocketAddress inetSocektAdress = null;
    /** obiekt przechowujacy socket*/
    private ServerSocket serwerSocekt = null;
    /** obiekt przechowujacy socket*/
    private Socket polaczenie = null; 
    
    // Obiekty poszczegolnych klas
    /** obikekt klasy GlownePolaczenie */
    private GlownePolaczenie glownePolaczenie = null; 
    /** obiekt klasy KontenerNaPortyDlaKlientow*/
    private KontenerNaPortyDlaKlientow kontenerNaPortyDlaKlienta = null;
    /** obiekt klasy WysylanieWiadomosci */ 
    private WysylanieWiadomosci wysylanieWiadomosci = null;
    /** Obiekt klasy Nasluch */
    private Nasluch nasluchNaGlownymPorcie = null;
    /** obiekt klasy WysylanieIodbieranieWiadomosciOdKlientow */
    private WysylanieIodbieranieWiadomoscOdKlientow wysylanieIOdbieranieWiadomosci = null;
    /** obiekt klasy ParametryPODiffeHelman */
    private ParametryPIQDiffiHelman parametryPIQ = null;
    /** zmienna int przechowujaca P z algorytmu D-H*/
    private int parametrPDiffHelman;
    /** zmienna int przechowujaca Q z algorytmu D-H*/
    private int parametrQDiffHelman;
    /** zmienna int przechowujaca czy jest polaczenie szyfrowane*/
    private PolaczenieSzyfrowane polaczenieSzyfrowane = null;
    
    
    // konstruktor wyswietlajacy okno na ekranie
    /** konstruktor okna*/
    public Okno() throws IOException
    {
        this.inetSocektAdress = new InetSocketAddress("localhost",2300);
        this.serwerSocekt = new ServerSocket();
        kontenerNaPortyDlaKlienta = new KontenerNaPortyDlaKlientow();
        this.wysylanieIOdbieranieWiadomosci = new WysylanieIodbieranieWiadomoscOdKlientow();
        this.polaczenieSzyfrowane = new PolaczenieSzyfrowane(this.kontenerNaPortyDlaKlienta);
        this.polaczenieSzyfrowane.start();
        this.parametryPIQ = new ParametryPIQDiffiHelman();
        this.parametrPDiffHelman = this.parametryPIQ.zwrocLiczbeP();
        this.parametrQDiffHelman = this.parametryPIQ.zwrocLiczbeQ();
        setTitle("Serwer"); 
        panel.add(poleTekstoweDlaPierwszegoKlienta);
        panel.add(poleTekstoweDlaDrugiegoKlienta);
        panel.add(poleTekstoweDlaTrzeciegoKlienta);
        
       add(panel);
       setSize(600,600); 
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setVisible(true); 
    }
    
    // metoda watku
    // w tej metodzie serwer slucha na porcie 2300 az jakis klient sie z nim polaczy, nastepnie wysyla port do komunikacji oraz 
    // paramtery diffiego helmana p i q
    /** metoda watku run odpowiada za nasluchiwanie*/
    public void run()
    {
            Socket soc = null;
            
        
            try 
            {
                serwerSocekt.bind(this.inetSocektAdress);
            } catch (IOException ex) {
                Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
            }

            while(true)
            {

                try {
                    
                    soc = serwerSocekt.accept();
                    Object port = kontenerNaPortyDlaKlienta.zwrocWolnyPort();
                    if(wysylanieIOdbieranieWiadomosci.zwrocLiczbeKlientow() <= 1)
                    {
                            wysylanieWiadomosci = new WysylanieWiadomosci(soc, String.valueOf(port) + " " + this.parametrPDiffHelman + " " + 
                            this.parametrQDiffHelman + " " + wysylanieIOdbieranieWiadomosci.zwrocLiczbeKlientow());
                    }
                    else
                    {
                        this.parametrPDiffHelman = parametryPIQ.zwrocLiczbeP();
                        this.parametrQDiffHelman = parametryPIQ.zwrocLiczbeQ();
                        wysylanieIOdbieranieWiadomosci.uaktualnienieParametrowPIQdifiHelman(this.parametrPDiffHelman, this.parametrQDiffHelman);
                        wysylanieWiadomosci = new WysylanieWiadomosci(soc, String.valueOf(port) + " " + this.parametrPDiffHelman + " " +
                        this.parametrQDiffHelman + " " + wysylanieIOdbieranieWiadomosci.zwrocLiczbeKlientow());
                        
                    }
                    
                        
                        wysylanieIOdbieranieWiadomosci.dodajKlienta(String.valueOf(port));
                        wysylanieWiadomosci.start();

                        Thread.sleep(1000);
                        
                        if(wysylanieIOdbieranieWiadomosci.zwrocLiczbeKlientow() == 2)
                        {
                            System.out.println("Startuje watek wyslanie i odbieranie wiadomosci od klintow");
                            wysylanieIOdbieranieWiadomosci.start();
                        }
                      
                    soc = null;

                } catch (Exception exc) {
                    exc.printStackTrace();
            }

        }          
    }

}
