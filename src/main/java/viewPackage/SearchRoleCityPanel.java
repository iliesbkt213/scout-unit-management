package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.LoadReferenceDataException;
import exceptionPackage.SearchInscriptionException;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;
import modelPackage.Role;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

public class SearchRoleCityPanel extends JPanel {

    private ApplicationController controller;
    private JComboBox<Role> roleCombo;
    private JTextField cityField;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public SearchRoleCityPanel(ApplicationController controller) {
        setController(controller);
        setLayout(new BorderLayout(10, 10));
        buildHeader();
        buildCriteriaPanel();
        buildTable();
        buildFooter();
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

    private void buildHeader() {
        JLabel title = new JLabel("Recherche d'inscriptions par rôle et ville",
                SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);
    }

    private void buildCriteriaPanel() {
        JPanel criteria = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        roleCombo = new JComboBox<>();
        cityField = new JTextField(15);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> handleSearch());

        constraints.gridx = 0;
        constraints.gridy = 0;
        criteria.add(new JLabel("Rôle :"), constraints);
        constraints.gridx = 1;
        criteria.add(roleCombo, constraints);

        constraints.gridx = 2;
        criteria.add(new JLabel("Ville :"), constraints);
        constraints.gridx = 3;
        criteria.add(cityField, constraints);

        constraints.gridx = 4;
        criteria.add(searchButton, constraints);

        add(criteria, BorderLayout.PAGE_START);
    }

    private void buildTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Id", "Personne", "Email", "Rôle", "Unité", "Ville", "Statut"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void buildFooter() {
        statusLabel = new JLabel(" ");
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void reloadReferenceData() {
        try {
            roleCombo.removeAllItems();
            roleCombo.addItem(null);
            for (Role role : controller.getAllRoles()) {
                roleCombo.addItem(role);
            }
        } catch (LoadReferenceDataException exception) {
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Erreur de chargement des données", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearch() {
        statusLabel.setText(" ");
        try {
            InscriptionSearchCriteria criteria = new InscriptionSearchCriteria();
            Role selectedRole = (Role) roleCombo.getSelectedItem();
            if (selectedRole != null) {
                criteria.setRoleLabel(selectedRole.getLabel());
            }
            String city = cityField.getText();
            if (city != null && !city.trim().isEmpty()) {
                criteria.setCityName(city.trim());
            }
            List<InscriptionDetail> results = controller.searchInscriptionsByRoleAndCity(criteria);
            tableModel.setRowCount(0);
            for (InscriptionDetail detail : results) {
                tableModel.addRow(new Object[]{
                        detail.getInscriptionId(),
                        detail.getFullName(),
                        detail.getPersonEmail(),
                        detail.getRoleLabel(),
                        detail.getUnitName(),
                        detail.getPostalCode() + " " + detail.getCityName(),
                        detail.getStatus()
                });
            }
            statusLabel.setText(results.size() + " inscription(s) trouvée(s).");
        } catch (SearchInscriptionException exception) {
            statusLabel.setText("La recherche a échoué.");
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Erreur de recherche", JOptionPane.ERROR_MESSAGE);
        }
    }
}
