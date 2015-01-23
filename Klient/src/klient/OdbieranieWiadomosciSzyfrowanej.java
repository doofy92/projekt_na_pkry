
package klient;




import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTextArea;
import sun.misc.BASE64Decoder;

/**  Klasa odpowiadajaca za odbieranie wiadomosci zaszyfrowanej */
public class OdbieranieWiadomosciSzyfrowanej extends Thread
{
    
        /**  Obiekt klasy socket slużacy jako gniadko sieciowe */
        private Socket polaczenie = null;
        
        /**  Obiekt klasy DataInputStream sluzacy do odbierania danych*/
        private DataInputStream dIn = null;
        
        /**  Obiekt klasy DataInputStream sluzacy za przesylanie danych*/
        private DataOutputStream out = null;
        
        /**  zmienna przechowujaa informache czy pojednyczy nasluch jest*/
        private Boolean czyPojedynczy;
        
        /**  zmienna przechowuja nr portu*/
        private int port;
        
        /**  tablica byteprzechowujaca wiadomosci od serwera*/
        private byte[] wiadomoscOdSerwera = null;
        
        /**  zmienna przechowujaca dlugosc bitowa*/
        private int dlugoscBitowa;
        
        /**  zmienna przechowujaca odebrany port*/
        private int OdberanyPort= 0;
        
        /**  zmienna przechowujaca kluc do szyfrowania*/
        private int kluczDoSzyfrowania;
        
        /**  zmienna obiekt klasy TextArea*/
        private JTextArea textAreaToWrite;
        
        /**  zmienna przechowujaca wiadomosci od serwera*/
        private String wiadooscOdSerweraDoTextArea = null;
        
        
        /**  konstruktor klasy*/
        public OdbieranieWiadomosciSzyfrowanej(Boolean czyPojedynczy, int port) throws IOException
        {
            this.port = port;
            this.polaczenie = new Socket("localhost", port);
            this.czyPojedynczy = czyPojedynczy;
            dIn = new DataInputStream(this.polaczenie.getInputStream());
            out = new DataOutputStream(this.polaczenie.getOutputStream());
            
        }
        
        //watek
        
        /**  metoda odpowiadajaca za watek */
        public void run()
        {
            if(czyPojedynczy)
            {
                try {
                    this.dlugoscBitowa = dIn.readInt();
                } catch (IOException ex) {
                    Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.wiadomoscOdSerwera = new byte[dlugoscBitowa];
                if(this.dlugoscBitowa > 0) {

                    try {
                        dIn.readFully(wiadomoscOdSerwera, 0, wiadomoscOdSerwera.length); // read the message
                        this.OdberanyPort = Integer.parseInt(new String(wiadomoscOdSerwera, StandardCharsets.UTF_8));
                        System.out.println("Port do szyfrowania: " + this.OdberanyPort);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else
            {
                System.out.println("Utowrzony nasluc na porcie: " + this.port);
                System.out.println("Dlugosc bitowa: " + this.dlugoscBitowa);
                
                while(true)
                {
                    try {
                        this.dlugoscBitowa = dIn.readInt();
                        this.wiadomoscOdSerwera = new byte[this.dlugoscBitowa];
                    } catch (IOException ex) {
                        Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  if(this.dlugoscBitowa >= 0) {

                    try {
                        
                        System.out.println("Przed odbiorem klucz: " + this.kluczDoSzyfrowania);
                        dIn.readFully(this.wiadomoscOdSerwera, 0, this.wiadomoscOdSerwera.length); // read the message 
                        
                        this.wiadooscOdSerweraDoTextArea = odszyfrujWiadomosc(this.kluczDoSzyfrowania, this.wiadomoscOdSerwera);
                        System.out.println("Wiadomosc odszyfrowana to: " + this.wiadooscOdSerweraDoTextArea);
                        
                    }   catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidKeyException ex) {
                            Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidAlgorithmParameterException ex) {
                            Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchPaddingException ex) {
                            Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalBlockSizeException ex) {
                            Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (BadPaddingException ex) {
                            Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(OdbieranieWiadomosciSzyfrowanej.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
   
        
        // metoda wysylajaca wiadomosc zaszyfrowana
        
        /**  metoda wysylajaca wiadomosc zaszyfrowana*/
        public void wyslijWiadomoscSzyfrowana(String wiadomosc, int klucz) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
        {
            byte[] bf = zaszyfrujWiadomosc(klucz, wiadomosc);
            out.writeInt(bf.length);
            out.write(bf);
            System.out.println("Wyslana wiadomosc zaszyfrona to: " + wiadomosc + " na porcie " + this.port);
        }
        
        
        // zwracanie wiadomosci przychodzacej od klienta
        
        /**  zwracanie wiadomosci przychodzacej od klienta*/
        public String zwrocWiadomoscOdSerweraDoTextArea()
        {
            return this.wiadooscOdSerweraDoTextArea;
        }
        
        
        /**  metoda ustawiajaca wiadmosc od serwera na null*/
        public void ustawWiadomoscOdSerweraDoTextAreaNaNull()
        {
            this.wiadooscOdSerweraDoTextArea = null;
        }
        
        // zwracanie wiadomosci przychodzacej od klineta w byte[]
        
        /**  metoda zwracajaca wiadomosci przychodzacej od klineta w byte[]*/
        public byte[] zwrocwiadomoscOdSerwera()
        {
            return this.wiadomoscOdSerwera;
        }
        
        
        /**  metoda zwracajaca port odebrany*/
        public int zwrocOdebranyPort()
        {
            return this.OdberanyPort;
        }
        
        /**  metoda ustawiajaca klucz*/
        public void ustawKlucz(int klucz)
        {
            this.kluczDoSzyfrowania = klucz;
        }
        
        /**  metoda ustawiajaca obiekt kalsy text area*/
        public void ustawObiektTextArea(JTextArea textAreaToWrite)
        {
            this.textAreaToWrite = textAreaToWrite;
        }
        
        
        // metoda odszyfrowujaca wiadomsc przychodzaca
        
        /**  metoda odszyfrowujaca wiadomsc przychodzaca*/
        public String odszyfrujWiadomosc(int klucz, byte[] wiadomosc) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
        {
            String key1 = String.format("%16s", Integer.toBinaryString(klucz)).replace(' ', '0');
            byte[] key2 = key1.getBytes();
            SecretKeySpec secret = new SecretKeySpec(key2, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);    
            byte[] decrypted = cipher.doFinal(wiadomosc);           
            return new String(decrypted, StandardCharsets.UTF_8);
           
        }
        
        
        // metoda szyfrujaca wiadomosc przychodzaca
        
        /**  metoda szyfrujaca wiadomosc przychodzaca*/
        public byte[] zaszyfrujWiadomosc(int klucz, String wiadomosc) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
        {
            String key1 = String.format("%16s", Integer.toBinaryString(klucz)).replace(' ', '0');
            byte[] key2 = key1.getBytes();
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec secret = new SecretKeySpec(key2, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
            byte[] encrypted = cipher.doFinal(wiadomosc.getBytes(StandardCharsets.UTF_8));
            
            return encrypted;
        }
    
}
