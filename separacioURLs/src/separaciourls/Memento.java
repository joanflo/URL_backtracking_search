
package separaciourls;

import java.util.ArrayList;

/**
 * Permet guardar l'estat del programa i posteriorment recuperar-lo.
 * @author Bernardo Miquel Riera / Juan Gabriel Florit Gomila
 * @version 1.0
 */
public class Memento {
    
    //Variables que conformen l'estat del programa:
    private static String URLOrigen;
    private static String URLDesti;
    private static String paraula;
    private static int opcioSeleccionada;
    private static int profunditatMaxima;
    private static int solucioFinal;
    private static ArrayList cami;
    
    //Indica si s'ha guardat l'estat correctament:
    private boolean creatCorrectament = false;
            
    //Declaram objecte al que se li aplica el patró singleton:
    private static Memento snapshot = null;
    
    /**
     * Recollim l'estat del programa que es pasa per paràmetre i el guardam als
     * objectes locals.
     * @param URLOrigen URL origen
     * @param URLDesti URL destí
     * @param paraula paraula de la cerca
     * @param opcioSeleccionada tipus de cerca
     * @param profunditatMaxima profunditat màxima de l'arbre de cerques
     * @param solucioFinal solució de la cerca
     * @param cami cami de la solució de la cerca
     */
    private Memento() {        
        creatCorrectament = true;
    }
    
    /**
     * Torna l'estat del programa (objecte Memento). Si encara no estava
     * instanciat es crea.
     * @return estat del programa
     */
    public static Memento getSnapshot() {
        if (snapshot == null) {
            creaInstancia();
        }
        return snapshot;
    }
    
    /**
     * Encarregat de crear la instancia de Memento. És synchronized per a futures
     * revisions del programa multithreated on podrien apareixer problemes de concurrencia.
     * (en aquest entorn seria encara més robust per evitar més d'una instancia)
     */
    private synchronized static void creaInstancia() {
        snapshot = new Memento();
    }
    
    /**
     * Torna un array idèntic a l'original passat per paràmetre.
     * @param cami array original
     * @return la còpia de l'array
     */
    private static ArrayList duplicarArray(ArrayList cami){
        ArrayList aux = new ArrayList();
        for (int i=0; i<cami.size(); i++) {
            aux.add(cami.get(i));
        }
        return aux;
    }
    
    /**
     * S'encarrega de fer un set de totes les variables que representen
     * l'estat del porgrama
     * @param URLOrigen URL origen
     * @param URLDesti URL destí
     * @param paraula paraula a cercar
     * @param opcioSeleccionada opció de cerca seleccionada
     * @param profunditatMaxima profunditat màxima de cerca
     * @param solucioFinal solució final de la cerca
     * @param cami camí de la solució
     */
    public static void setValorsAGUardar(String URLOrigen, String URLDesti, String paraula, int opcioSeleccionada, int profunditatMaxima, int solucioFinal, ArrayList cami) {
        setURLOrigen(URLOrigen);
        setURLDesti(URLDesti);
        setParaula(paraula);
        setOpcioSeleccionada(opcioSeleccionada);
        setProfunditatMaxima(profunditatMaxima);
        setSolucioFinal(solucioFinal);
        setCami(cami);
    }
    
    
    //SETTERS de cada una de les variables que conformen l'estat del programa:

    /**
     * Set de la url destí
     * @param URLDesti URL destí
     */
    private static void setURLDesti(String URLDesti) {
        //Duplicam els Strings:
        if (URLDesti != null) {
             Memento.URLDesti = new String(URLDesti);
        }
    }

    /**
     * Set de la URL d'origen
     * @param URLOrigen URL origen
     */
    private static void setURLOrigen(String URLOrigen) {
        //Duplicam els Strings:
        if (URLOrigen != null) {
            Memento.URLOrigen = new String(URLOrigen);
        }
    }

    /**
     * Set del camí de la solució
     * @param cami cami de la solució
     */
    private static void setCami(ArrayList cami) {
        //L'ArrayList es duplica:
        if (cami != null) {
            Memento.cami = duplicarArray(cami);
        }
    }

    /**
     * Set de la opció de cerca seleccionada
     * @param opcioSeleccionada opció seleccionada
     */
    private static void setOpcioSeleccionada(int opcioSeleccionada) {
        //Els tipus bàsics es pasen per còpia:
        Memento.opcioSeleccionada = opcioSeleccionada;
    }

    /**
     * Set de la paraula a cercar
     * @param paraula paraula a cercar
     */
    private static void setParaula(String paraula) {
        //Duplicam els Strings:
        if (paraula != null) {
             Memento.paraula = new String(paraula);
        }
    }

    /**
     * Set de la profunditat de cerca màxima
     * @param profunditatMaxima profunditat de cerca
     */
    private static void setProfunditatMaxima(int profunditatMaxima) {
        //Els tipus bàsics es pasen per còpia:
        Memento.profunditatMaxima = profunditatMaxima;
    }

    /**
     * Set de la solució final de la cerca
     * @param solucioFinal solució final
     */
    private static void setSolucioFinal(int solucioFinal) {
        //Els tipus bàsics es pasen per còpia:
        Memento.solucioFinal = solucioFinal;
    }
    
    
    
    //GETTERS de cada una de les variables que conformen l'estat del programa:
    
    /**
     * Torna la URL origen.
     * @return URL origen
     */
    public String getURLOrigen() {
        return new String(URLOrigen);
    }
    
    /**
     * Torna la URL destí.
     * @return URL destí
     */
    public String getURLDesti() {
        return new String(URLDesti);
    }
    
    /**
     * Torna la paraula de la cerca.
     * @return paraula
     */
    public String getParaula() {
        return new String(paraula);
    }
    
    /**
     * Torna la opció de cerca seleccionada.
     * @return enter que representa la opció seleccionada
     */
    public int getOpcioSeleccionada() {
        return opcioSeleccionada;
    }
    
    /**
     * Torna la profunditat màxima que es permet explorar dins l'arbre de solucions
     * que es va conformant.
     * @return profunditat màxima de l'arbre de solucions
     */
    public int getProfunditatMaxima() {
        return profunditatMaxima;
    }
    
    /**
     * Torna la solució final d'una cerca
     * @return solució final
     */
    public int getSolucioFinal() {
        return solucioFinal;
    }
    
    /**
     * Torna el camí que forma una solució trobada en una cerca
     * @return camí d'una solució
     */
    public ArrayList getCami() {
        if (cami != null) {
            return duplicarArray(cami);
        } else {
            return null;
        }
    }

    /**
     * Diu si el patró s'ha creat correctament
     * @return confirmació de patró creat correctament
     */
    public boolean isCreatCorrectament() {
        return creatCorrectament;
    }
    
}