package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.LoadReferenceDataException;
import exceptionPackage.SearchInscriptionException;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;
import modelPackage.UnitScout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class SearchUnitDatePanel extends JPanel {

    private ApplicationController controller;
    private JComboBox<UnitScout> unitCombo;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public SearchUnitDatePanel(ApplicationController controller) {
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
        JLabel title = new JLabel("Recherche d'inscriptions par unité et période d'inscription",
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

        unitCombo = new JComboBox<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -6);
        SpinnerDateModel fromModel = new SpinnerDateModel(calendar.getTime(), null, null, Calendar.DAY_OF_MONTH);
        fromDateSpinner = new JSpinner(fromModel);
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));

        SpinnerDateModel toModel = new SpinnerDateModel();
        toDateSpinner = new JSpinner(toModel);
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> handleSearch());

        constraints.gridx = 0;
        constraints.gridy = 0;
        criteria.add(new JLabel("Unité :"), constraints);
        constraints.gridx = 1;
        criteria.add(unitCombo, constraints);

        constraints.gridx = 2;
        criteria.add(new JLabel("Du :"), constraints);
        constraints.gridx = 3;
        criteria.add(fromDateSpinner, constraints);

        constraints.gridx = 4;
        criteria.add(new JLabel("Au :"), constraints);
        constraints.gridx = 5;
        criteria.add(toDateSpinner, constraints);

        constraints.gridx = 6;
        criteria.add(searchButton, constraints);

        add(criteria, BorderLayout.PAGE_START);
    }

    private void buildTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Id", "Personne", "Rôle", "Unité", "Ville", "Date d'inscription", "Statut", "Montant"
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
            unitCombo.removeAllItems();
            unitCombo.addItem(null);
            for (UnitScout unitScout : controller.getAllUnitScouts()) {
                unitCombo.addItem(unitScout);
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
            UnitScout selectedUnit = (UnitScout) unitCombo.getSelectedItem();
            if (selectedUnit != null) {
                criteria.setUnitName(selectedUnit.getUnitName());
            }
            java.util.Date fromValue = (java.util.Date) fromDateSpinner.getValue();
            java.util.Date toValue = (java.util.Date) toDateSpinner.getValue();
            if (fromValue != null && toValue != null && fromValue.after(toValue)) {
                JOptionPane.showMessageDialog(this,
                        "La date de début doit être antérieure à la date de fin.",
                        "Erreur de validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fromValue != null) {
                criteria.setRegistrationDateFrom(toSqlDate(fromValue));
            }
            if (toValue != null) {
                criteria.setRegistrationDateTo(toSqlDate(toValue));
            }
            List<InscriptionDetail> results = controller.searchInscriptionsByUnitAndDate(criteria);
            tableModel.setRowCount(0);
            for (InscriptionDetail detail : results) {
                tableModel.addRow(new Object[]{
                        detail.getInscriptionId(),
                        detail.getFullName(),
                        detail.getRoleLabel(),
                        detail.getUnitName(),
                        detail.getPostalCode() + " " + detail.getCityName(),
                        detail.getRegistrationDate(),
                        detail.getStatus(),
                        detail.getAmount()
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

    private Date toSqlDate(java.util.Date utilDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(utilDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }
}
