package separaciourls;

import java.awt.Desktop;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;

/**
 * És el controlador del patró model-vista-controlador. S'encarrega de
 * gestionar la classe Interficie. És a dir, capta els esdeveniments
 * de la GUI, identifica de quin esdeveniment es tracta ivrealitza
 * les operacions oportunes. També mostra missatges d'interès per a l'usuari.
 * @author Bernardo Miquel Riera / Juan Gabriel Florit Gomila
 * @version 2.0
 * @see separaciourls.Interficie
 */
public class GestorInterficie implements ActionListener, ChangeListener {

    //Referencies a objectes de Interficie i variables auxiliars:
    private JRadioButton opcio1;
    private JRadioButton opcio2;
    private JRadioButton opcio3;
    private JTextField textURLOrigen;
    private String URLOrigen;
    private JTextField textURLDesti;
    private String URLDesti;
    private JTextField textParaula;
    private String paraula;
    private JEditorPane areaURL = new JEditorPane();
    private int opcioSeleccionada;//opcio 1 seleccionada per defecte, se'n encarrega el constructor d'interficie
    private int profunditatMaxima = 1;//serà 1 per defecte
    private static int solucioFinal = -1;
    private static ArrayList cami = null;
    private boolean estatEnmagatzemat = false;//per a saber si s'ha guardat l'estat del programa

    /**
     * Mètode que rep un radioButton per a fer que aquest apunti a un item local
     * radioButton
     * @param opcio1 opció 1 del radio button
     */
    public void setOpcio1(JRadioButton opcio1) {
        this.opcio1 = opcio1;
    }

    /**
     * Mètode que rep un radioButton per a fer que aquest apunti a un item local
     * radioButton
     * @param opcio2 opció 2 del radio button
     */
    public void setOpcio2(JRadioButton opcio2) {
        this.opcio2 = opcio2;
    }

    /**
     * Mètode que rep un radioButton per a fer que aquest apunti a un item local
     * radioButton
     * @param opcio3 opció 3 del radio button
     */
    public void setOpcio3(JRadioButton opcio3) {
        this.opcio3 = opcio3;
    }

    /**
     * Mètode que rep el JTextField que conté la URL origen per a fer que aquest
     * apunti a un item local
     * @param textURLOrigen camp URL origen
     */
    public void setTextURLOrigen(JTextField textURLOrigen) {
        this.textURLOrigen = textURLOrigen;
    }

    /**
     * Mètode que rep el JTextField que conté la URL destí per a fer que aquesta
     * apunti a un item local
     * @param textURLDesti camp URL destí
     */
    public void setTextURLDesti(JTextField textURLDesti) {
        this.textURLDesti = textURLDesti;
    }

    /**
     * Mètode que rep el JTextField que conté la paraula a cercar per a fer que
     * aquest apunti a un item local
     * @param textParaula camp paraula
     */
    public void setTextParaula(JTextField textParaula) {
        this.textParaula = textParaula;
    }

    /**
     * Mètode que rep el JEditorPane per a fer que aquest apunti a un item local
     * @param areaURL àrea on s'interpreta codi HTML de planes web
     */
    public void setAreaURL(JEditorPane areaURL) {
        this.areaURL = areaURL;
    }

    /**
     * Rep un esdeveniment i identifica de quin es tracta per a poder realitzar
     * les accions que pertoquin.
     * @param e Esdeveniment que ha succeït.
     * @see #cercar() 
     * @see separaciourls.CalculadoraDistancies
     */
    public void actionPerformed(ActionEvent e) {
        String comand = e.getActionCommand();//Guardam dins l'string l'AcionComand
        if (comand.equals("Cercar")) {
            CalculadoraDistancies.resetValorsCerca();
            cercar();
        } else if (comand.equals("esborrar camps")) {
            Interficie.buidarCampsDeText();
        } else if (comand.equals("opcio1")) {
            opcioSeleccionada = 1;
            Interficie.setComboOpcio(opcioSeleccionada - 1);
        } else if (comand.equals("opcio2")) {
            opcioSeleccionada = 2;
            Interficie.setComboOpcio(opcioSeleccionada - 1);
        } else if (comand.equals("opcio3")) {
            opcioSeleccionada = 3;
            Interficie.setComboOpcio(opcioSeleccionada - 1);
        } else if (comand.equals("ComboOpcio")) {//
            opcioSeleccionada = Interficie.getComboOpcio().getSelectedIndex() + 1;
        } else if (comand.equals("guarda estat")) {
            backupEstat();
        } else if (comand.equals("recupera estat")) {
            rollbackEstat();
        } else if (comand.equals("manual")) {
            obrirArxiu("manual.pdf");
        } else if (comand.equals("API del programa")) {
            obrirArxiu("dist/javadoc/index.html");
        } else if (comand.equals("Sobre")) {
            Interficie.mostrarMissatgeSobre();
        } else if (comand.equals("Sortir")) {
            System.exit(0);//tancam la máquina virtual
        }
        habilitarCampsSegonsOpcioSeleccionada();
    }

    /**
     * Rep un esdeveniment quan l'usuari mou l'slider i el canvia de nivell. El
     * mètode guarda el nou nivell seleccionat.
     * @param e esdeveniment relacionat amb l'slider
     */
    public void stateChanged(ChangeEvent e) {
        JSlider nivell = (JSlider) e.getSource();
        if (!nivell.getValueIsAdjusting()) {
            profunditatMaxima = nivell.getValue();
        }
    }

    /**
     * Quan se'l crida, afaga totes les dades que ha introduït l'usuari i
     * inicialitza els atributs que prendran part en la cerca.
     */
    private void getValorsCerca() {
        URLOrigen = textURLOrigen.getText();
        URLDesti = textURLDesti.getText();
        paraula = textParaula.getText();
        //Falta profunditatMaxima, però aquest atribut ja pren valors just
        //després de que l'usuari mogui l'slider
    }

    /**
     * Cerca segons l'opció seleccionada i alhora controla que no es
     * produixin possibles errors d'usuari.
     * @throws IOException Problema durant la entrada de dades
     * @throws MalformedURLException S'ha introduït una URL mal formada, incorrecta
     * @throws ExcepcioValorNul Algun dels camps necessaris per la cerca es troba buit o és incorrecte
     */
    private void cercar() {
        int excepcio = 0;
        String campURLOrigen = textURLOrigen.getText();
        String campURLDesti = textURLDesti.getText();
        try {
            if ((campURLOrigen.equals("") && textURLOrigen.isEnabled()) || (campURLDesti.equals("") && textURLDesti.isEnabled()) || (textParaula.getText().equals("") && textParaula.isEnabled())) {
                excepcio = 1;
                throw new ExcepcioValorNul();
            } else if (campURLOrigen.contains(" ")) {
                excepcio = 2;
                throw new ExcepcioValorNul();
            } else if (campURLDesti.contains(" ")) {
                excepcio = 3;
                throw new ExcepcioValorNul();
            }
            getValorsCerca();
            areaURL.setPage(URLOrigen);
            Interficie.informarUsuari("Iniciant cerca per a la opcio " + opcioSeleccionada + "...");
            cercarSegonsOpcioSeleccionada();
        } catch (ExcepcioValorNul evn) {
            switch (excepcio) {
                case 1:
                    Interficie.informarUsuari("Un dels camps necessaris està buit.");
                    Interficie.mostrarMissatgeAlerta("Un dels camps necessaris està buit.");
                    break;
                case 2:
                    Interficie.informarUsuari("La URL origen conté espais en blanc.");
                    Interficie.mostrarMissatgeAlerta("La URL origen conté espais en blanc.");
                    break;
                case 3:
                    Interficie.informarUsuari("La URL desti conté espais en blanc.");
                    Interficie.mostrarMissatgeAlerta("La URL desti conté espais en blanc.");
                    break;
            }

        } catch (MalformedURLException mf) {
            Interficie.informarUsuari("La URL origen està mal formada.");
            Interficie.mostrarMissatgeAlerta("La URL origen està mal formada.");
        } catch (IOException ex) {
            Logger.getLogger(Interficie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Classe anidada que hereda de Exception. Serveix per controlar excepcions.
     * @see #cercar() 
     */
    private class ExcepcioValorNul extends Exception {

        /**
         * Mètode que agafa l'error per si l'usuari s'equivoca
         */
        public ExcepcioValorNul() {
            super("Algun camp necessari es troba buit.");
        }
    }

    /**
     * Segons el tipus de cerca que tenguem seleccionat, crida al mètode que
     * pertoqui per a fer la cerca que l'usuari haurà estipulat. Després informa
     * del resultat de la cerca.
     */
    private void cercarSegonsOpcioSeleccionada() {
        solucioFinal = -1;
        if (opcioSeleccionada == 1) {
            CalculadoraDistancies.cercarSeparacioMinimaOpcio1(URLOrigen, URLDesti, profunditatMaxima);
        } else if (opcioSeleccionada == 2) {
            CalculadoraDistancies.cercarSeparacioMinimaOpcio2(URLOrigen, paraula, profunditatMaxima);
        } else if (opcioSeleccionada == 3) {
            CalculadoraDistancies.cercarSeparacioMinimaOpcio3(URLOrigen, URLDesti, paraula, profunditatMaxima);
        }
        if (solucioFinal == -1) {
            Interficie.informarUsuari("No s'ha trobat solució");
        } else {
            Interficie.informarUsuari("La solució és: " + solucioFinal);
            Interficie.informarUsuari("El camí de la solució en ordre descendent és:");
            //imprimim el path de URLs de la solució:
            for (int i=0; i<cami.size(); i++) {
                Interficie.informarUsuari(cami.get(i).toString());
            }
        }
    }

    /**
     * Mètode que fa un set a la solució final d'una cerca
     * @param solucioFinal solució de la cerca
     */
    public static void setSolucioFinal(int solucioFinal) {
        GestorInterficie.solucioFinal = solucioFinal;
    }

    /**
     * Mètode que fa un set al cami que conforma una solució d'una cerca
     * @param cami cami de la solució
     */
    public static void setCami(ArrayList cami) {
        GestorInterficie.cami = cami;
    }
    
    /**
     * Actualitza les altres opcions de cerca. Rep per paràmetre les 2 opcions
     * que no han estat seleccionades (en total són 3). Com que tan sols n'hi
     * pot haver una seleccionada, es comprova si les opcions pasades per
     * paràmetre estan seleccionades i si és així les des-selecciona.
     * @param opcioA Una de les opcions que no ha d'estar seleccionada
     * @param opcioB La segona opció no ha d'estar seleccionada
     * @param opcioC La tercera opció no ha d'estar seleccionada
     */
    private void actualitzarOpcions(JRadioButton opcioA, JRadioButton opcioB, JRadioButton opcioC) {
        opcioA.setSelected(true);
        if (opcioB.isSelected()) {
            opcioB.setSelected(false);
        }
        if (opcioC.isSelected()) {
            opcioC.setSelected(false);
        }
    }

    /**
     * Mètode que s'encarrega d'obrir l'arxiu corresponent que es reb per
     * paràmetre
     * @param ruta path de l'arxiu a obrir
     * @throws IOException Problema durant l'input/output de dades
     */
    private void obrirArxiu(String ruta) {
        try {
            File path = new File(ruta);
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            Interficie.informarUsuari("No s'ha pogut obrir el fitxer.");
            Interficie.mostrarMissatgeAlerta("No s'ha pogut obrir el fitxer.");
        }
    }

    /**
     * Mètode que s'encarrega d'habilitar els camps modificables segons la opció
     * seleccionada.
     */
    private void habilitarCampsSegonsOpcioSeleccionada() {
        if (opcioSeleccionada == 1) {
            Interficie.habilitarCamps(true, true, false);
            actualitzarOpcions(opcio1, opcio2, opcio3);//a l'opció 1 no s'indrodueix cap paraula
        } else if (opcioSeleccionada == 2) {
            Interficie.habilitarCamps(true, false, true);//a l'opció 2 no s'indrodueix cap URL desti
            actualitzarOpcions(opcio2, opcio1, opcio3);
        } else if (opcioSeleccionada == 3) {
            Interficie.habilitarCamps(true, true, true);
            actualitzarOpcions(opcio3, opcio1, opcio2);
        }
    }
    
    /**
     * El mètode s'encarrega d'enmagatzemar l'estat actual del programa. Comprova si
     * s'ha duit a terme correctament (aplicat patró singleton).
     * @see separaciourls.Memento
     */
    private void backupEstat() {
        getValorsCerca();
        //Com que Memento s'ha implementat amb el patró singleton accedim a ell
        //estàticament a través de getSnapshot que ens torna el Memento instanciat:
        Memento.setValorsAGUardar(URLOrigen, URLDesti, paraula, opcioSeleccionada, profunditatMaxima, solucioFinal, cami);
        if (Memento.getSnapshot().isCreatCorrectament()) {
            estatEnmagatzemat = true;
            //Feedback per a l'usuari:
            Interficie.informarUsuari("L'estat s'ha guardat satisfactòriament");
        } else {
            estatEnmagatzemat = false;
            //Feedback per a l'usuari:
            Interficie.informarUsuari("Ha ocorregut un problema al guardar l'estat");
        }
    }
    
    /**
     * Recuperam l'estat enmagatzemat sempre que s'hagui guardat prèviament
     * @see separaciourls.Memento
     */
    private void rollbackEstat() {
        if (!estatEnmagatzemat) {
            //No es pot recuperar un estat si abans no se n'ha guardat cap
            Interficie.informarUsuari("No hi ha cap estat guardat");
        } else {
            //Recuperam l'estat del programa:
            URLOrigen = Memento.getSnapshot().getURLOrigen();
            URLDesti = Memento.getSnapshot().getURLDesti();
            paraula = Memento.getSnapshot().getParaula();
            opcioSeleccionada = Memento.getSnapshot().getOpcioSeleccionada();
            profunditatMaxima = Memento.getSnapshot().getProfunditatMaxima();
            solucioFinal = Memento.getSnapshot().getSolucioFinal();
            cami = Memento.getSnapshot().getCami();

            //Operacions per a resturar l'estat:
            textURLOrigen.setText(URLOrigen);
            textURLDesti.setText(URLDesti);
            textParaula.setText(paraula);
            Interficie.setComboOpcio(opcioSeleccionada - 1);
            habilitarCampsSegonsOpcioSeleccionada();//Té en compte la variable recuperada "opcioSeleccionada"
            JSlider sliderNivell = Interficie.getSliderNivell();
            sliderNivell.setValue(profunditatMaxima);
            
            //Feedback per a l'usuari:
            Interficie.informarUsuari("L'estat s'ha recuperat satisfactòriament");
            if (solucioFinal == -1) {
                Interficie.informarUsuari("No s'havia trobat cap solució a l'estat anterior");
            } else {
                Interficie.informarUsuari("La solució trobada anteriorment era: " + solucioFinal);
                Interficie.informarUsuari("El camí de la solució en ordre descendent era:");
                //imprimim el path de URLs de la solució:
                for (int i=0; i<cami.size(); i++) {
                    Interficie.informarUsuari(cami.get(i).toString());
                }
            }
        }
    }
    
}