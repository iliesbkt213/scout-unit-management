package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.DeleteInscriptionException;
import exceptionPackage.InscriptionNotFoundException;
import modelPackage.InscriptionDetail;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class InscriptionListPanel extends JPanel {

    private ApplicationController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    public InscriptionListPanel(ApplicationController controller) {
        setController(controller);
        setLayout(new BorderLayout(10, 10));
        buildHeader();
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
        JLabel title = new JLabel("Toutes les inscriptions", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);
    }

    private void buildTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Id", "Statut", "Date d'inscription", "Date pré-inscription",
                "Date confirmation", "Personne", "Rôle", "Unité", "Ville",
                "Tuteur légal", "Montant", "Aut. parent.", "Cert. méd.", "Payé"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel(" ");
        JButton refreshButton = new JButton("Rafraîchir");
        JButton deleteButton = new JButton("Supprimer la sélection");
        refreshButton.addActionListener(e -> refresh());
        deleteButton.addActionListener(e -> handleDeleteSelected());
        footer.add(refreshButton);
        footer.add(deleteButton);
        footer.add(statusLabel);
        add(footer, BorderLayout.SOUTH);
    }

    private void handleDeleteSelected() {
        int[] viewRows = table.getSelectedRows();
        if (viewRows.length == 0) {
            statusLabel.setText("Sélectionnez au moins une ligne à supprimer.");
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner au moins une ligne dans le tableau avant de cliquer sur supprimer.",
                    "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Integer> ids = new ArrayList<>();
        for (int viewRow : viewRows) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            ids.add((Integer) tableModel.getValueAt(modelRow, 0));
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Supprimer " + ids.size() + " inscription(s) ? Cette action est irréversible.",
                "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        int deleted = 0;
        List<String> failures = new ArrayList<>();
        for (Integer id : ids) {
            try {
                controller.deleteInscription(id);
                deleted++;
            } catch (DeleteInscriptionException | InscriptionNotFoundException exception) {
                failures.add("#" + id + " : " + exception.getMessage());
            }
        }
        statusLabel.setText(deleted + " inscription(s) supprimée(s).");
        if (!failures.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Certaines suppressions ont échoué :\n" + String.join("\n", failures),
                    "Suppression partielle", JOptionPane.WARNING_MESSAGE);
        }
        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        try {
            List<InscriptionDetail> details = controller.getAllInscriptions();
            for (InscriptionDetail detail : details) {
                tableModel.addRow(new Object[]{
                        detail.getInscriptionId(),
                        detail.getStatus(),
                        detail.getRegistrationDate(),
                        detail.getPreRegistrationDate(),
                        detail.getConfirmationDate(),
                        detail.getFullName(),
                        detail.getRoleLabel(),
                        detail.getUnitName(),
                        detail.getPostalCode() + " " + detail.getCityName(),
                        detail.getLegalGuardianName(),
                        detail.getAmount(),
                        detail.getParentAuthorization(),
                        detail.getMedicalCertificate(),
                        detail.getPaymentFinish()
                });
            }
            statusLabel.setText(details.size() + " inscription(s) chargée(s).");
        } catch (AllInscriptionsException exception) {
            statusLabel.setText("Impossible de charger les inscriptions.");
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
        }
    }
}
