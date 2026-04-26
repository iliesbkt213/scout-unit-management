package viewPackage;

import controllerPackage.ApplicationController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class MainFrame extends JFrame {

    private ApplicationController controller;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private ScoutAnimationPanel animationPanel;

    private InscriptionListPanel listPanel;
    private AddInscriptionPanel addPanel;
    private UpdateInscriptionPanel updatePanel;
    private SearchUnitDatePanel searchUnitDatePanel;
    private SearchRoleCityPanel searchRoleCityPanel;
    private SearchPendingDatePanel searchPendingDatePanel;
    private BusinessTaskPanel businessTaskPanel;

    private static final String CARD_HOME = "HOME";
    private static final String CARD_LIST = "LIST";
    private static final String CARD_ADD = "ADD";
    private static final String CARD_UPDATE = "UPDATE";
    private static final String CARD_SEARCH_UNIT = "SEARCH_UNIT";
    private static final String CARD_SEARCH_ROLE = "SEARCH_ROLE";
    private static final String CARD_SEARCH_PENDING = "SEARCH_PENDING";
    private static final String CARD_BUSINESS_TASK = "BUSINESS_TASK";

    public MainFrame() {
        setController(new ApplicationController());
        buildFrame();
        buildMenuBar();
        buildCardsPanel();
        showCard(CARD_HOME);
    }

    private void buildFrame() {
        setTitle("Gestion d'unité scoute - Bloc 2 - Renilde de Smedt & Ilies Boukhatem");
        setSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu homeMenu = new JMenu("Accueil");
        JMenuItem homeItem = new JMenuItem("Écran d'accueil");
        homeItem.addActionListener(e -> showCard(CARD_HOME));
        homeMenu.add(homeItem);

        JMenu inscriptionMenu = new JMenu("Inscriptions");
        JMenuItem listItem = new JMenuItem("Lister toutes");
        JMenuItem addItem = new JMenuItem("Ajouter");
        JMenuItem updateItem = new JMenuItem("Modifier / Supprimer");
        listItem.addActionListener(e -> showCard(CARD_LIST));
        addItem.addActionListener(e -> showCard(CARD_ADD));
        updateItem.addActionListener(e -> showCard(CARD_UPDATE));
        inscriptionMenu.add(listItem);
        inscriptionMenu.add(addItem);
        inscriptionMenu.add(updateItem);

        JMenu searchMenu = new JMenu("Recherches");
        JMenuItem searchUnitItem = new JMenuItem("Par unité et date");
        JMenuItem searchRoleItem = new JMenuItem("Par rôle et ville");
        JMenuItem searchPendingItem = new JMenuItem("Par statut et date");
        searchUnitItem.addActionListener(e -> showCard(CARD_SEARCH_UNIT));
        searchRoleItem.addActionListener(e -> showCard(CARD_SEARCH_ROLE));
        searchPendingItem.addActionListener(e -> showCard(CARD_SEARCH_PENDING));
        searchMenu.add(searchUnitItem);
        searchMenu.add(searchRoleItem);
        searchMenu.add(searchPendingItem);

        JMenu toolsMenu = new JMenu("Outils");
        JMenuItem businessTaskItem = new JMenuItem("Statistiques métier");
        businessTaskItem.addActionListener(e -> showCard(CARD_BUSINESS_TASK));
        toolsMenu.add(businessTaskItem);

        JMenu helpMenu = new JMenu("Aide");
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Gestion d'unité scoute\nProjet - Programmation orientée objet avancée\n"
                        + "Renilde de Smedt & Ilies Boukhatem - Bloc 2",
                "À propos", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(homeMenu);
        menuBar.add(inscriptionMenu);
        menuBar.add(searchMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void buildCardsPanel() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        listPanel = new InscriptionListPanel(controller);
        addPanel = new AddInscriptionPanel(controller);
        updatePanel = new UpdateInscriptionPanel(controller);
        searchUnitDatePanel = new SearchUnitDatePanel(controller);
        searchRoleCityPanel = new SearchRoleCityPanel(controller);
        searchPendingDatePanel = new SearchPendingDatePanel(controller);
        businessTaskPanel = new BusinessTaskPanel(controller);

        cardsPanel.add(buildHomePanel(), CARD_HOME);
        cardsPanel.add(listPanel, CARD_LIST);
        cardsPanel.add(addPanel, CARD_ADD);
        cardsPanel.add(updatePanel, CARD_UPDATE);
        cardsPanel.add(searchUnitDatePanel, CARD_SEARCH_UNIT);
        cardsPanel.add(searchRoleCityPanel, CARD_SEARCH_ROLE);
        cardsPanel.add(searchPendingDatePanel, CARD_SEARCH_PENDING);
        cardsPanel.add(businessTaskPanel, CARD_BUSINESS_TASK);

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel buildHomePanel() {
        JPanel home = new JPanel(new BorderLayout());
        home.setBackground(new Color(255, 248, 220));

        JLabel welcome = new JLabel("Bienvenue dans l'application de gestion d'unité scoute",
                SwingConstants.CENTER);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        home.add(welcome, BorderLayout.NORTH);

        JLabel hint = new JLabel(
                "Utilisez la barre de menus ci-dessus pour gérer les inscriptions, lancer des recherches et consulter les statistiques.",
                SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 14));
        home.add(hint, BorderLayout.SOUTH);

        animationPanel = new ScoutAnimationPanel();
        home.add(animationPanel, BorderLayout.CENTER);

        return home;
    }

    private void showCard(String cardName) {
        if (CARD_HOME.equals(cardName)) {
            animationPanel.startAnimation();
        } else {
            animationPanel.stopAnimation();
        }
        if (CARD_LIST.equals(cardName)) {
            listPanel.refresh();
        } else if (CARD_ADD.equals(cardName)) {
            addPanel.reloadReferenceData();
        } else if (CARD_UPDATE.equals(cardName)) {
            updatePanel.reloadAll();
        } else if (CARD_SEARCH_UNIT.equals(cardName)) {
            searchUnitDatePanel.reloadReferenceData();
        } else if (CARD_SEARCH_ROLE.equals(cardName)) {
            searchRoleCityPanel.reloadReferenceData();
        }
        cardLayout.show(cardsPanel, cardName);
    }

    public ApplicationController getController() {
        return controller;
    }

    public void setController(ApplicationController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller must not be null.");
        }
        this.controller = controller;
    }
}
