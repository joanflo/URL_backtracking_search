package separaciourls;

import java.util.*;
import tpurl.AgafarUrl;

/**
 * Classe on hi ha implementats els mètodes per a calcular la mínima distància
 * de separació entre dues URL's segons el tipus de cerca que realitzam (sense
 * restriccions, donada una paraula que aparegui a la URL desti i donada una
 * paraula que no aparegui en les URL's intermitjes).
 * @author Bernardo Miquel Riera / Juan Gabriel Florit Gomila
 * @version 2.0
 */
public class CalculadoraDistancies {

    //Aqui guardarem els links que ja haguem mirat per a no tornar a mirar-los i no entrar en bucles infinits:
    static Map linksComprovats = new HashMap();

    //Variable que val true quan trobam una solució:
    static boolean trobada = false;
    
    //Solució de la cerca:
    static int solucioFinal = -1;

    //Aquí guardarem el camí de la solució:
    static ArrayList cami = new ArrayList();

    //Variables per controlar la següent URL a guardar:
    static int nivellSuperiorAGuardar;
    static boolean guardarCami = false;

    /**
     * Algoritme recursiu de cerca que s'aplica quan l'usuari té seleccionada
     * la opció 1 (cerca sense cap restricció).
     * @param URLorigen URL origen de la que partim
     * @param URLdesti URL desti que volem trobar
     * @param profunditatMaxima profunditat m'axima de l'arbre de cerca
     */
    static public void cercarSeparacioMinimaOpcio1(String URLorigen, String URLdesti, int profunditatMaxima) {
        if (profunditatMaxima>=0){
            Interficie.informarUsuari("S'analitzarà la URL "+URLorigen+" al nivell "+profunditatMaxima);
            if (!URLorigen.equals(URLdesti)) {
                //Backtracking
                Vector linksURLorigen = new AgafarUrl(URLorigen).getLinks();
                for (int i=0; i<linksURLorigen.size(); i++) {
                    //Per cada link, comprovam que no l'haguem consultat ja, i si no és així
                    //procedim a analitzar-lo. Després el guardam per a no tornar a consultar-lo.
                    if (!linksComprovats.containsKey((String)linksURLorigen.get(i) + profunditatMaxima)) {
                        cercarSeparacioMinimaOpcio1((String)linksURLorigen.get(i), URLdesti, profunditatMaxima-1);
                        linksComprovats.put((String)linksURLorigen.get(i) + profunditatMaxima, URLorigen);
                    }
                }
            } else {
                //Si arribam aqui és perque hem trobat una possible solució
                if (solucioFinal <= profunditatMaxima) {
                    //Si és millor que la solució anterior la guardam
                    solucioFinal = profunditatMaxima;
                    GestorInterficie.setSolucioFinal(solucioFinal);
                    cami.clear();//Borram el cami anterior ja que hem trobat una solució millor
                    guardarCami = true;
                    nivellSuperiorAGuardar = profunditatMaxima;
                }
            }
            if (guardarCami && (nivellSuperiorAGuardar == profunditatMaxima)) {
                cami.add(URLorigen);
                nivellSuperiorAGuardar++;
                GestorInterficie.setCami(cami);
            }
        }
        //El cas trivial es troba implícit
    }

    /**
     * Algoritme recursiu de cerca que s'aplica quan l'usuari té seleccionada
     * la opció 2 (donada una paraula que aparegui a la URL desti).
     * @param URLorigen URL origen de la que partim
     * @param paraula paraula que es preten trobar a algun link
     * @param profunditatMaxima profunditat m'axima de l'arbre de cerca
     */
    static public void cercarSeparacioMinimaOpcio2(String URLorigen, String paraula, int profunditatMaxima) {
        if (profunditatMaxima>=0){
            Interficie.informarUsuari("S'analitzarà la URL "+URLorigen+" al nivell "+profunditatMaxima);
            String textURL = new AgafarUrl(URLorigen).getText();
            if (!textURL.contains(paraula)) {
                //Backtracking
                Vector linksURLorigen = new AgafarUrl(URLorigen).getLinks();
                for (int i=0; i<linksURLorigen.size(); i++) {
                    //Per cada link, comprovam que no l'haguem consultat ja, i si no és així
                    //procedim a analitzar-lo. Després el guardam per a no tornar a consultar-lo.
                    if (!linksComprovats.containsKey((String)linksURLorigen.get(i) + profunditatMaxima)) {
                        cercarSeparacioMinimaOpcio2((String)linksURLorigen.get(i), paraula, profunditatMaxima-1);
                        linksComprovats.put((String)linksURLorigen.get(i) + profunditatMaxima, URLorigen);
                    }
                }
            } else {
                //Si arribam aqui és perque hem trobat una possible solució
                if (solucioFinal <= profunditatMaxima) {
                    //Si és millor que la solució anterior la guardam
                    solucioFinal = profunditatMaxima;
                    GestorInterficie.setSolucioFinal(solucioFinal);
                    cami.clear();//Borram el cami anterior ja que hem trobat una solució millor
                    guardarCami = true;
                    nivellSuperiorAGuardar = profunditatMaxima;
                }
            }
            if (guardarCami && (nivellSuperiorAGuardar == profunditatMaxima)) {
                cami.add(URLorigen);
                nivellSuperiorAGuardar++;
                GestorInterficie.setCami(cami);
            }
        }
        //El cas trivial es troba implícit
    }

    /**
     * Algoritme recursiu de cerca que s'aplica quan l'usuari té seleccionada
     * la opció 3 (donada una paraula que no aparegui en les URL's intermitjes).
     * @param URLorigen URL origen de la que partim
     * @param URLdesti URL desti que volem trobar
     * @param paraula paraula que no pot apareixer
     * @param profunditatMaxima profunditat m'axima de l'arbre de cerca
     */
    static public void cercarSeparacioMinimaOpcio3(String URLorigen, String URLdesti, String paraula, int profunditatMaxima) {
        if (profunditatMaxima>=0){
            Interficie.informarUsuari("S'analitzarà la URL "+URLorigen+" al nivell "+profunditatMaxima);
            String textURL = new AgafarUrl(URLorigen).getText();
            if (!textURL.contains(paraula)) {
                //Si la URL origen no conté la paraula prohibida procedim al seu anàlisi
                if (!(URLorigen.equals(URLdesti))) {
                    //Backtracking
                    Vector linksURLorigen = new AgafarUrl(URLorigen).getLinks();
                    for (int i=0; i<linksURLorigen.size(); i++) {
                        //Per cada link, comprovam que no l'haguem consultat ja, i si no és així
                        //procedim a analitzar-lo. Després el guardam per a no tornar a consultar-lo.
                        if (!linksComprovats.containsKey((String)linksURLorigen.get(i) + profunditatMaxima)) {
                            cercarSeparacioMinimaOpcio3((String)linksURLorigen.get(i), URLdesti, paraula, profunditatMaxima-1);
                            linksComprovats.put((String)linksURLorigen.get(i) + profunditatMaxima, URLorigen);
                        }
                    }
                } else {
                    //Si arribam aqui és perque hem trobat una possible solució
                    if (solucioFinal <= profunditatMaxima) {
                        //Si és millor que la solució anterior la guardam
                        solucioFinal = profunditatMaxima;
                        GestorInterficie.setSolucioFinal(solucioFinal);
                        cami.clear();//Borram el cami anterior ja que hem trobat una solució millor
                        guardarCami = true;
                        nivellSuperiorAGuardar = profunditatMaxima;
                    }
                }
                if (guardarCami && (nivellSuperiorAGuardar == profunditatMaxima)) {
                    cami.add(URLorigen);
                    nivellSuperiorAGuardar++;
                    GestorInterficie.setCami(cami);
                }
            }
        }
        //El cas trivial es troba implícit
    }
    
    /**
     * Fa un reset de totes les variables per a dur a terme la cerca de manera
     * que se'n pugui realitzar una de nova.
     */
    static public void resetValorsCerca() {
        linksComprovats.clear();
        trobada = false;
        solucioFinal = -1;
        cami.clear();
        guardarCami = false;
    }
    
}