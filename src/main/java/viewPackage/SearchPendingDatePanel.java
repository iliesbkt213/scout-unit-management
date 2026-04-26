package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.SearchInscriptionException;
import modelPackage.Inscription;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;

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

public class SearchPendingDatePanel extends JPanel {

    private ApplicationController controller;
    private JComboBox<String> statusCombo;
    private JSpinner fromDateSpinner;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public SearchPendingDatePanel(ApplicationController controller) {
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
        JLabel title = new JLabel("Recherche d'inscriptions par statut et date d'inscription",
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

        statusCombo = new JComboBox<>(new String[]{
                Inscription.STATUS_PENDING,
                Inscription.STATUS_CONFIRMED,
                Inscription.STATUS_REJECTED,
                Inscription.STATUS_CANCELLED
        });

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        fromDateSpinner = new JSpinner(new SpinnerDateModel(calendar.getTime(), null, null, Calendar.DAY_OF_MONTH));
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> handleSearch());

        constraints.gridx = 0;
        constraints.gridy = 0;
        criteria.add(new JLabel("Statut :"), constraints);
        constraints.gridx = 1;
        criteria.add(statusCombo, constraints);

        constraints.gridx = 2;
        criteria.add(new JLabel("Inscrit après le :"), constraints);
        constraints.gridx = 3;
        criteria.add(fromDateSpinner, constraints);

        constraints.gridx = 4;
        criteria.add(searchButton, constraints);

        add(criteria, BorderLayout.PAGE_START);
    }

    private void buildTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Id", "Personne", "Rôle", "Unité", "Date d'inscription", "Montant", "Cert. méd.", "Payé"
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

    private void handleSearch() {
        statusLabel.setText(" ");
        try {
            InscriptionSearchCriteria criteria = new InscriptionSearchCriteria();
            criteria.setStatus((String) statusCombo.getSelectedItem());
            java.util.Date fromValue = (java.util.Date) fromDateSpinner.getValue();
            if (fromValue != null) {
                criteria.setRegistrationDateFrom(toSqlDate(fromValue));
            }
            List<InscriptionDetail> results = controller.searchPendingInscriptionsByStatus(criteria);
            tableModel.setRowCount(0);
            for (InscriptionDetail detail : results) {
                tableModel.addRow(new Object[]{
                        detail.getInscriptionId(),
                        detail.getFullName(),
                        detail.getRoleLabel(),
                        detail.getUnitName(),
                        detail.getRegistrationDate(),
                        detail.getAmount(),
                        detail.getMedicalCertificate(),
                        detail.getPaymentFinish()
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
