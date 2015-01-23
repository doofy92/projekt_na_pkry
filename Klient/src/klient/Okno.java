package klient;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.bind.DatatypeConverter;
import sun.misc.BASE64Encoder;

/** Klasa odpowiadająca za wyświetlanie okna aplikacji na ekranie */

public class Okno extends JFrame implements Runnable{
    
    /*
        Pola naszej klasy, ktora dziedziczy po Runnable, bo ma metode, ktora odpowiada za watek
    */
    
    // pole do pisania wiadomosci do drugiego klineta
    
    /** Obiekt klasy JTextArea służący jako pole do pisania wiadomości do drugiego klineta */
    
    private JTextArea textAreaToRead = new JTextArea(5,20); 
    // pole do odbierania wiadomosci od drugiego klienta
    
     /** Obiekt klasy JTextArea służący jako pole do odbierania wiadomości do drugiego klineta */
    private JTextArea textAreaToWrite = new JTextArea(5,20);
    // przycisk do wysylania wiadomosci do klineta
    
    /** Obiekt klasy JButton służący do wysyłania wiadomości do drugiego klineta */
    private JButton buttonToSend = new JButton("Send");
    
    //przycisk do laczenia sie z serwerem i ustalania klucza sesyjnego
    
    /** Obiekt klasy JButton służący do połączenia się z drugim klinetem */
    private JButton buttonToConnect  = new JButton("Connect");
    
    // tabica stringow
    
    /** Tablica obiektów String służąca do wyboru portów */
    private String[] porty = {"Wybierz Port","2300"};
    
    // lista rozwijana do wybrania portu do laczenia sie z serwerem
    /** Obiekt klasy JComboBoox służący do wyboru portu dołączenia się z serwerem */
    
    private JComboBox wyborPortow = new JComboBox(porty);
    
    
    private JScrollPane pane = new JScrollPane ();
    // obiekt klasy OdbieranieWiadomosci
    
    /** Obiekt klasy OdbieranieWiadomosci służący  */
    private OdbieranieWiadomosci pojedynczyOdbior = null;
    
    // obiekt klasy WysylanieWiadomosci
    
    /** Obiekt klasy WysyłanieWiadomości służący  */
    private WysylanieIOdbieranie wysIOdb = null;
    
    //Obiekt klasy OdbieranieWiadomosciSzyfrowanej
    
    /** Obiekt klasy OdbieranieWiadosciSzyfornej  */
    private OdbieranieWiadomosciSzyfrowanej odbieranieWiadomosciZaszyfrowanej = null;
    
    /** zmiena służąca do przehcowywanie numeru portu do połączń szyfrowanych  */
    
    private int portDoLaczeniaWiadomosciSzyfrowanych = 2340;
    
    //Drugi obiekt klasy OdbieranieWiadomosciSzyfrowanej
    
     /** Obiekt klasy OdbieranieWiadomosciSzyfrowanej  */
    private OdbieranieWiadomosciSzyfrowanej odbieranieWiadomosciSzyfrowanejNiePojedynczej = null;
    
    
    // Konstruktor naszej klasy odpowiedzialny za wyswietlanie komponentow na farme
    
    /** OKonstruktor klasy Okno  */
    public Okno()
    {
       
       setTitle("Klient");    
       
      
       JPanel p =new JPanel();
       
       
       p.add(textAreaToRead);
       
       p.add(pane);
       
       
        
       p.add(textAreaToWrite);
       
       
       wyborPortow.addActionListener(akcjaListyRozwijanej);
       
      
       p.add(wyborPortow);
       
       
       buttonToSend.addActionListener(akcjaPrzyciskuSend);
       
       
       p.add(buttonToSend);
       
       
       buttonToConnect.addActionListener(akcjaPrzyciskuConnect);
       
       
       p.add(buttonToConnect);
       
       pane.getViewport().setView (textAreaToRead);
       
       
       add(p);
       
       
       setSize(600,600); 
       
      
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       
       setVisible(true);
    }
    
    
    // metoda odpowiadajaca za zdarzenie zwiaznae z rozwinieciem naszej listy
    
    /** metoda odpowiadajaca za zdarzenie zwiaznae z rozwinieciem naszej listy */
     private ActionListener akcjaListyRozwijanej  = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
            {
              System.out.println("lista rozwijana");
              System.out.println(wyborPortow.getSelectedItem() + " -wybrane z listy");
            }
    };
     
     // metoda odpowiadajaca za zdarzenie zwiazane z przycisnieciem przycisku connect
     
     /** metoda odpowiadajaca za zdarzenie zwiazane z przycisnieciem przycisku connect */
     private ActionListener akcjaPrzyciskuConnect  = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
            {
                
                try {
                    //inicjaliujemy obiekt klasy OdbierenieWidomosci wybranem portem z listy oraz wartoscia true, ktora wskazuje 
                    // na to czy jest to pojedyncze polaczenie, za pomoca ktorego otrzymamy wlasciwy port komunikacji z serwrem
                    //(jesli true to wlasnie tak jest jesli false to nie jest tak)
                    pojedynczyOdbior = new OdbieranieWiadomosci(String.valueOf(wyborPortow.getSelectedItem()), true);
                    // wywolanie funkcji wygenerujParametrADiffiHelman
                    pojedynczyOdbior.wygenereujParametrADiffiHelman();
                    //wystartowanie watku zwiaznego z tym obiektem
                    pojedynczyOdbior.start();
                } catch (IOException ex) {
                    Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                    // petla, ktora jest nam potrzebna do tego ze czekamy az serwer wysle nam odpowiedni port do komunikacji z nim 
                
                   while(pojedynczyOdbior.zwrocOtrzymaneDane() == null)
                   {
                       System.out.println();
                   }
                   // jesli otrzymamy wiadomosc z portem to wylaczamy watek nasluchu z serwerem na pocie 2300
                   pojedynczyOdbior.interrupt();

                try {
                    // usypiamy watek na 1 sek
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {

                    // inicjalizacja obiektu klasy WysylanieIOdbieranie 
                    wysIOdb = new WysylanieIOdbieranie(pojedynczyOdbior.zworcOtrzymanyPort(), String.valueOf(pojedynczyOdbior.daneDoWyslaniaDiffiHelman()), pojedynczyOdbior.zwrocParametrP(), pojedynczyOdbior.zwrocParametrQ(), pojedynczyOdbior.zwrocParamterA());
                    // wystartowanie watku
                    wysIOdb.start();
                    //inicjalizacja obiektu textAree
                    wysIOdb.ustawTextArea(textAreaToRead);
                    // inicjalizacja obiektu
                    odbieranieWiadomosciZaszyfrowanej = new OdbieranieWiadomosciSzyfrowanej(true, portDoLaczeniaWiadomosciSzyfrowanych);
                    // wystartowanie watku
                    odbieranieWiadomosciZaszyfrowanej.start();
                    // uspienie watku na 0.5 sek
                    Thread.sleep(500);
                    
                    // zwrocenie wlasciwego portu do komunikacji wiadomosci zaszyfrowanych 
                    int port = odbieranieWiadomosciZaszyfrowanej.zwrocOdebranyPort();
                    
                    // inicjalizacja obiektu ktory bedzie nam odpowiadal za komuniacje przeszylania wiadomosci zaszyfrowanych
                     odbieranieWiadomosciSzyfrowanejNiePojedynczej = new OdbieranieWiadomosciSzyfrowanej(false, odbieranieWiadomosciZaszyfrowanej.zwrocOdebranyPort());
                     // wystartowanie watku nasluchu
                     odbieranieWiadomosciSzyfrowanejNiePojedynczej.start();
                     // // zwrocenie klucza sesjnego potrzebnego do szyfrowania wiadomsoci
                     int klucz = wysIOdb.zwrocKlucz();
                    
                     System.out.println("Klucz sesyjny zwracany to!!!: " + klucz);
                     // ustawienie klucza sesyjnego
                     odbieranieWiadomosciSzyfrowanejNiePojedynczej.ustawKlucz(klucz); 
                     
                } catch (Exception ex) {


                } 
          }
      
    };
      
     // metoda odpowiadajaca za klikniecie przycisku send
     
     /**  metoda odpowiadajaca za klikniecie przycisku send */
     private  ActionListener akcjaPrzyciskuSend  = new ActionListener()
     {
          public void actionPerformed(ActionEvent e)
          {
              
              int klucz = wysIOdb.zwrocKlucz();
              //ustawianie klucza sesyjego
              odbieranieWiadomosciSzyfrowanejNiePojedynczej.ustawKlucz(klucz);
              System.out.println("klucz przy wysylaniu to: " + klucz);
             
              try {
                  // wysylanie wiadomosci zaszyfrowanej
                  odbieranieWiadomosciSzyfrowanejNiePojedynczej.wyslijWiadomoscSzyfrowana(textAreaToRead.getText(), klucz);
              } catch (NoSuchAlgorithmException ex) {
                  Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
              } catch (NoSuchPaddingException ex) {
                  Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
              } catch (InvalidKeyException ex) {
                  Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
              } catch (InvalidAlgorithmParameterException ex) {
                  Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalBlockSizeException ex) {
                  Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
              } catch (BadPaddingException ex) {
                  Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IOException ex) {
                  Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
              }
              
          }
     };
      
     // metoda watku odpowiadajaca za ustawianie klucza sesyjnego oraz ustawiania w textArea wiadomosci przychodzacej od klienta
     
     /**   metoda watku odpowiadajaca za ustawianie klucza sesyjnego oraz ustawiania w textArea wiadomosci przychodzacej od klienta */
    public void run()
    {
        Boolean czyDzialac = true;
        
        while(true){
            int klucz = 0;
            try{
                klucz = wysIOdb.zwrocKlucz();
                
                if(odbieranieWiadomosciSzyfrowanejNiePojedynczej.zwrocWiadomoscOdSerweraDoTextArea() != null)
                {
                    this.textAreaToWrite.setText(odbieranieWiadomosciSzyfrowanejNiePojedynczej.zwrocWiadomoscOdSerweraDoTextArea());
                    odbieranieWiadomosciSzyfrowanejNiePojedynczej.ustawWiadomoscOdSerweraDoTextAreaNaNull();
                }

                if(klucz != 0 && czyDzialac){

                    odbieranieWiadomosciSzyfrowanejNiePojedynczej.ustawKlucz(klucz);
                    czyDzialac = false;
                }

               Thread.sleep(300);
            }catch (InterruptedException ex) {
                Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
            }catch(NullPointerException ex1)
            {
                
            }finally{
                
            }
            
        }
        
    } 
    
}
