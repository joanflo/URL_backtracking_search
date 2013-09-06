
package separaciourls;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * La classe Interficie és l'encarregada de pintar la part gráfica del programa
 * per a l'usuari.
 * Auqesta classe representa la vista del patró model-vista-controlador.
 * @author Bernardo Miquel Riera / Juan Gabriel Florit Gomila
 * @version 2.0
 * @see separaciourls.GestorInterficie
 */
public class Interficie {

    private final static String NEW_LINE = "\n";//constant per canviar de linia
    private GestorInterficie gestor = new GestorInterficie();//Controlador de la vista
    private JFrame finestraPrincipal = new JFrame("Separació entre URLs");//JFrame principal
    private JMenuBar barraPrincipal = new JMenuBar();
    private JMenu menuOpcions = new JMenu("Opcions");
    private JPanel panellCentre = new JPanel();
    private JPanel panellEsquerra = new JPanel();
    private JPanel panellDreta = new JPanel();
    private JPanel panellUsuari = new JPanel();
    private JEditorPane areaURL = new JEditorPane();
    private static JTextArea areaProcessos = new JTextArea();
    private static JTextField textURLOrigen = new JTextField();
    private static JTextField textURLDesti = new JTextField();
    private static JTextField textParaula = new JTextField();
    private static JComboBox comboOpcio = new JComboBox();
    private static JSlider sliderNivell = new JSlider(0,3);

    /**
     * Crida als mètodes que acaben conformant la GUI.
     */
    public Interficie() {
        finestraPrincipal.setMinimumSize(new Dimension(850,500));
        finestraPrincipal.setPreferredSize(new Dimension(850,500));
        finestraPrincipal.setLayout(new BorderLayout());
        afegirComponents();//afegim els components al JFrame finestraPrincipal
        //finestraPrincipal.setResizable(false);
        setImatgePrograma();
        finestraPrincipal.setVisible(true);
        finestraPrincipal.pack();
        finestraPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        comboOpcio.setSelectedIndex(0);//Per defecte l'opció triada serà la primera
        informarUsuari("Programa iniciat correctament...");
    }

    /**
     * Mètode encarregat de canviar l'icona de la finestra.
     */
    private void setImatgePrograma() {
        ImageIcon imatgePrograma = new ImageIcon(getClass().getResource("grafo.gif"));
        finestraPrincipal.setIconImage(imatgePrograma.getImage());
    }

    /**
     * Mètode que afegeix tots els components que conformen el JFrame finestraPrincipal
     * @throws IOException Problema al carregar l'HTML
     */
    private void afegirComponents() {
        //Afegim el botó de cercar al menú opcions:
        JMenuItem cerca = new JMenuItem("Cercar");
        cerca.addActionListener(gestor);
        cerca.setActionCommand("Cercar");
        menuOpcions.add(cerca);

        //Afegim el submenú de restricció al menú opcions:
        JMenu restriccio = new JMenu("Restricció");
        restriccio.addActionListener(gestor);
        restriccio.setActionCommand("Restriccio");
        menuOpcions.add(restriccio);
        
        //Afegim l'opció de cerca "Sense cap restricció":
        JRadioButton opcio1 = new JRadioButton("Sense cap restricció");
        opcio1.addActionListener(gestor);
        opcio1.setActionCommand("opcio1");
        opcio1.setSelected(true);//tindrem la opció 1 seleccionada per defecte
        gestor.setOpcio1(opcio1);
        restriccio.add(opcio1);
        
        //Afegim l'opció de cerca "Donada una paraula que aparegui a la URL destí":
        JRadioButton opcio2 = new JRadioButton("Cercar una paraula a partir d'una URL origen");
        opcio2.addActionListener(gestor);
        opcio2.setActionCommand("opcio2");
        gestor.setOpcio2(opcio2);
        restriccio.add(opcio2);

        //Afegim l'opció de cerca "Donada una paraula que no aparegui a les URLs intermitjes":
        JRadioButton opcio3 = new JRadioButton("Donada una paraula que no aparegui a les URLs intermitjes");
        opcio3.addActionListener(gestor);
        opcio3.setActionCommand("opcio3");
        gestor.setOpcio3(opcio3);
        restriccio.add(opcio3);
        
        //Afegim el botó per a guardar l'estat del programa al menú opcions:
        menuOpcions.addSeparator();
        JMenuItem guardaEstat = new JMenuItem("Guardar estat");
        guardaEstat.addActionListener(gestor);
        guardaEstat.setActionCommand("guarda estat");
        menuOpcions.add(guardaEstat);
        
        //Afegim el botó per a recuperar l'estat del programa al menú opcions:
        JMenuItem recuperaEstat = new JMenuItem("Recuperar estat");
        recuperaEstat.addActionListener(gestor);
        recuperaEstat.setActionCommand("recupera estat");
        menuOpcions.add(recuperaEstat);
        
        //Afegim el botó de manual d'usuari al menú opcions:
        menuOpcions.addSeparator();
        JMenuItem manual = new JMenuItem("Manual d'usuari");
        manual.addActionListener(gestor);
        manual.setActionCommand("manual");
        menuOpcions.add(manual);

        //Afegim el botó de API programa al menú opcions:
        JMenuItem APIPrograma = new JMenuItem("API del programa");
        APIPrograma.addActionListener(gestor);
        APIPrograma.setActionCommand("API del programa");
        menuOpcions.add(APIPrograma);

        //Afegim el botó about al menú opcions:
        JMenuItem about = new JMenuItem("Sobre el programa");
        about.addActionListener(gestor);
        about.setActionCommand("Sobre");
        menuOpcions.add(about);
        barraPrincipal.add(menuOpcions);

        //Afegim el botó de sortir al menú opcions:
        menuOpcions.addSeparator();
        JMenuItem sortir = new JMenuItem("Sortir");
        sortir.addActionListener(gestor);
        sortir.setActionCommand("Sortir");
        barraPrincipal.add(menuOpcions);
        menuOpcions.add(sortir);

        panellEsquerra.setLayout(new GridBagLayout());
        GridBagConstraints limitacions = new GridBagConstraints();
        limitacions.weightx = 0.0;//Mai s'estirarà una columna
        panellUsuari.setLayout(new GridBagLayout());

        //Afegim l'etiqueta per a la URL origen:
        limitacions.anchor = GridBagConstraints.EAST;
        JLabel etURLOrigen = new JLabel();
        etURLOrigen.setText("URL origen:   ");
        limitacions.gridx = 0;
        limitacions.gridy = 0;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        panellUsuari.add(etURLOrigen, limitacions);

        //Afegim el camp per a la URL origen:
        limitacions.fill = GridBagConstraints.HORIZONTAL;
        limitacions.gridx = 1;
        limitacions.gridy = 0;
        limitacions.gridwidth = 2;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        textURLOrigen.setText("");
        gestor.setTextURLOrigen(textURLOrigen);
        panellUsuari.add(textURLOrigen, limitacions);
        limitacions.fill = GridBagConstraints.NONE;//restauram valor per defecte

        //Afegim l'etiqueta per a la URL destí:
        limitacions.anchor = GridBagConstraints.EAST;
        JLabel etURLDesti = new JLabel();
        etURLDesti.setText("URL destí:   ");
        limitacions.gridx = 0;
        limitacions.gridy = 1;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        panellUsuari.add(etURLDesti, limitacions);

        //Afegim el camp per a la URL destí:
        limitacions.fill = GridBagConstraints.HORIZONTAL;
        limitacions.gridx = 1;
        limitacions.gridy = 1;
        limitacions.gridwidth = 2;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        textURLDesti.setText("");
        gestor.setTextURLDesti(textURLDesti);
        panellUsuari.add(textURLDesti, limitacions);
        limitacions.fill = GridBagConstraints.NONE;//restauram valor per defecte

        //Afegim l'etiqueta per a la paraula:
        limitacions.anchor = GridBagConstraints.EAST;
        JLabel etParaula = new JLabel();
        etParaula.setText("Paraula:   ");
        limitacions.gridx = 0;
        limitacions.gridy = 2;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        panellUsuari.add(etParaula, limitacions);

        //Afegim el camp per a la paraula:
        limitacions.fill = GridBagConstraints.HORIZONTAL;
        limitacions.gridx = 1;
        limitacions.gridy = 2;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        textParaula.setText("");
        textParaula.setEnabled(false);
        gestor.setTextParaula(textParaula);
        panellUsuari.add(textParaula, limitacions);
        limitacions.fill = GridBagConstraints.NONE;//restauram valor per defecte

        //Afegim l'estiqueta profunditat:
        limitacions.anchor = GridBagConstraints.EAST;
        JLabel etProfunditat = new JLabel();
        etProfunditat.setText("Profunditat:   ");
        limitacions.gridx = 0;
        limitacions.gridy = 3;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        panellUsuari.add(etProfunditat, limitacions);

        //Afegim l'slider per seleccionar el nivell de profunditat:
        limitacions.fill = GridBagConstraints.HORIZONTAL;
        sliderNivell.setMajorTickSpacing(1);
        sliderNivell.setSnapToTicks(true);
        sliderNivell.setPaintLabels(true);
        sliderNivell.addChangeListener(gestor);
        limitacions.gridx = 1;
        limitacions.gridy = 3;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.0;
        sliderNivell.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1),BorderFactory.createEmptyBorder(10,10,10,10)));
        panellUsuari.add(sliderNivell, limitacions);
        limitacions.fill = GridBagConstraints.NONE;//restauram valor per defecte

        //Afegim l'etiqueta restriccions:
        limitacions.anchor = GridBagConstraints.SOUTHWEST;
        JLabel etRestriccions = new JLabel();
        etRestriccions.setText("Restriccions:   ");
        limitacions.gridx = 0;
        limitacions.gridy = 4;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.1;
        panellUsuari.add(etRestriccions, limitacions);

        //Afegim la llista d'opcions de cerca:
        limitacions.anchor = GridBagConstraints.NORTH;
        comboOpcio.addActionListener(gestor);
        comboOpcio.setActionCommand("ComboOpcio");
        comboOpcio.addItem("Sense cap restricció");
        comboOpcio.setSelectedIndex(0);//Per defecte estarà seleccionada la primera opció
        comboOpcio.addItem("Cercar una paraula a partir d'una URL origen");
        comboOpcio.addItem("Donada una paraula que no aparegui a les URLs intermitjes");
        limitacions.gridx = 0;
        limitacions.gridy = 5;
        limitacions.gridwidth = 3;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.1;
        panellUsuari.add(comboOpcio, limitacions);

        //Afegim el boto per a esborrar els camps de cerca
        limitacions.anchor = GridBagConstraints.SOUTHWEST;
        JButton esborrarCamps = new JButton("Esborrar camps");
        esborrarCamps.addActionListener(gestor);
        esborrarCamps.setActionCommand("esborrar camps");
        limitacions.gridx = 0;
        limitacions.gridy = 6;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.1;
        panellUsuari.add(esborrarCamps, limitacions);

        //Afegim el boto per a fer cerques
        limitacions.anchor = GridBagConstraints.SOUTHEAST;
        JButton cercar = new JButton("Cercar");
        cercar.addActionListener(gestor);
        cerca.setActionCommand("Cercar");
        limitacions.gridx = 2;
        limitacions.gridy = 6;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weighty = 0.1;
        panellUsuari.add(cercar, limitacions);

        //Afegim el panellUsuari a panellEsquerra:
        limitacions.fill = GridBagConstraints.BOTH;
        limitacions.gridx = 0;
        limitacions.gridy = 0;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weightx = 0.5;
        limitacions.weighty = 0.5;
        TitledBorder titol = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Paràmetres de cerca");
        panellUsuari.setBorder(titol);
        panellEsquerra.add(panellUsuari, limitacions);

        //Afegim la zona on hi aniran les notificacions dels processos que du a terme el programa:
        limitacions.gridx = 0;
        limitacions.gridy = 1;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        limitacions.weightx = 0.5;
        limitacions.weighty = 0.5;
        areaProcessos.setEditable(false);
        JScrollPane scrollProcessos = new JScrollPane(areaProcessos);
        scrollProcessos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panellEsquerra.add(scrollProcessos, limitacions);

        //Afegim la zona on es carregaran URLs:
        areaURL.setEditable(false);
        try {
            areaURL.setPage(this.getClass().getResource("/separaciourls/iniciHTML.html"));
        } catch (IOException ex) {
            Logger.getLogger(Interficie.class.getName()).log(Level.SEVERE, null, ex);
        }
        gestor.setAreaURL(areaURL);
        JScrollPane scrollURL = new JScrollPane(areaURL);
        scrollURL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panellDreta.setLayout(new GridBagLayout());
        limitacions.gridx = 0;
        limitacions.gridy = 0;
        limitacions.gridwidth = 1;
        limitacions.gridheight = 1;
        panellDreta.add(scrollURL, limitacions);

        //Ho integram tot dins la finestra principal:
        panellCentre.setLayout(new GridLayout(1,2,10,10));
        panellCentre.add(panellEsquerra);
        panellCentre.add(panellDreta);
        finestraPrincipal.add(barraPrincipal, BorderLayout.NORTH);
        finestraPrincipal.add(panellCentre, BorderLayout.CENTER);
    }
    
    /**
     * Mètode que escriu a l'àrea de processos l'String que rep per paràmetre.
     * @param missatge missatge que volem mostrar
     */
    static public void informarUsuari(String missatge) {
        areaProcessos.append(" > "+missatge+"\n");
    }
    
    /**
     * Mostra un JDialog amb informació del programa: versió , autors...
     */
    static public void mostrarMissatgeSobre() {
        String text = "AMPLIACIÓ DE PROGRAMACIÓ ORIENTADA A OBJECTES"
                + NEW_LINE
                + "Versió 2.0"
                + NEW_LINE
                + NEW_LINE
                + "Autors del programa:"
                + NEW_LINE
                + "     > Bernardo Miquel Riera"
                + NEW_LINE
                + "     > Juan Gabriel Florit Gomila";
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), text, "Sobre el programa...", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Mostra un JDialog d'alerta a l'usuari
     * @param missatge missatge que volem mostrar
     */
    static public void mostrarMissatgeAlerta(String missatge) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), missatge, "Alerta", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Mètode per habilitar/deshabilitar i permetre l'edició dels camps de text
     * del programa
     * @param enableTextURLOrigen si el camp de la URL origen s'habilita
     * @param enableTextURLDesti si el camp de la URL destí s'habilita
     * @param enableTextParaula si el camp de la paraula s'habilita
     */
    static public void habilitarCamps(boolean enableTextURLOrigen, boolean enableTextURLDesti, boolean enableTextParaula) {
        textURLOrigen.setEditable(enableTextURLOrigen);
        textURLOrigen.setEnabled(enableTextURLOrigen);
        textURLDesti.setEnabled(enableTextURLDesti);
        textURLDesti.setEditable(enableTextURLDesti);
        textParaula.setEnabled(enableTextParaula);
        textParaula.setEditable(enableTextParaula);
    }
    
    /**
     * Buida els camps de URL origen, URL destí i paraula
     */
    static public void buidarCampsDeText(){
        textURLOrigen.setText("");
        textURLDesti.setText("");
        textParaula.setText("");
    }
    
    /**
     * Selecciona l'opció del combo box pasada per paràmetre
     * @param numOpcio número d'opció que volem marcar
     */
    static public void setComboOpcio(int numOpcio) {
        comboOpcio.setSelectedIndex(numOpcio);
    }

    /**
     * Torna la referència al combo box de la opció de cerca
     * @return el combo box de la opció de cerca
     */
    static public JComboBox getComboOpcio() {
        return comboOpcio;
    }

    /**
     * Torna la referència al slider del nivell de profunditat de cerca
     * @return slider indicador de la profunditat màxima de cerca
     */
    static public JSlider getSliderNivell() {
        return sliderNivell;
    }

    /**
     * Repinta la interfície
     * @deprecated no usat en la versió final
     */
    public void actualitzaInterficie() {
        finestraPrincipal.repaint();
    }

}