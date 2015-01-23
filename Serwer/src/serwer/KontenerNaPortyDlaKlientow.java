package serwer;


/*
    Klasa przechowujaca porty do komunikacji z klinetami
*/

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**klasa KontenerNportyDlaKlientow*/
public class KontenerNaPortyDlaKlientow 
{
	/**obiekt klasy Map zawierajacy liste intow*/
    private Map<Integer, Boolean> mapa = null;
    /** obiekt klasy Iterator zawierajacy liste map*/
    private Iterator<Map.Entry<Integer, Boolean>> entries = null;
    /** metoda tworzaca HashMape*/
    public KontenerNaPortyDlaKlientow()
    {
        mapa = new HashMap<Integer, Boolean>();
        wypelnijMape();
        entries = mapa.entrySet().iterator();
        
        
    }
    /** metoda uzupelaniajaca HashMape*/
    private void wypelnijMape()
    {
        mapa.put(2301, Boolean.FALSE);
        mapa.put(2302, Boolean.FALSE);
        mapa.put(2303, Boolean.FALSE);
        mapa.put(2304, Boolean.FALSE);
        mapa.put(2305, Boolean.FALSE);
        mapa.put(2306, Boolean.FALSE);
        mapa.put(2307, Boolean.FALSE);
        mapa.put(2308, Boolean.FALSE);
        mapa.put(2309, Boolean.FALSE);
        mapa.put(2310, Boolean.FALSE);
        mapa.put(2311, Boolean.FALSE);
    }
    
    // wywoljac ta metode zostanie zworcony wolny port do komunikacji z klientem
    /** metoda zwracajaca wolny port */
    public int zwrocWolnyPort()
    {
        int port = 0;
        boolean czyWyjsc = false; 
         while(entries.hasNext() && !czyWyjsc)
         {
             Map.Entry<Integer, Boolean> entry = entries.next();
             System.out.println("Klucz w kontenerze " +entry.getKey());
             
             if(Boolean.valueOf(entry.getValue().toString()) == false)
             {
                 port = Integer.parseInt(entry.getKey().toString());
                 System.out.println("Port wyslany to:" + port);
                 entry.setValue(true);
                 czyWyjsc = true;
                 break;
             }
             
         }
        return port;
    }
    
}
