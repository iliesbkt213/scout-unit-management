package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.BusinessTaskException;
import exceptionPackage.SearchInscriptionException;
import modelPackage.InscriptionDetail;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class BusinessTaskPanel extends JPanel {

    private ApplicationController controller;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private JTextArea reportArea;
    private JLabel statusLabel;

    public BusinessTaskPanel(ApplicationController controller) {
        setController(controller);
        setLayout(new BorderLayout(10, 10));
        buildHeader();
        buildCriteriaPanel();
        buildReportArea();
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
        JLabel title = new JLabel("Statistiques métier sur les inscriptions",
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

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.YEAR, -1);
        fromDateSpinner = new JSpinner(new SpinnerDateModel(fromCalendar.getTime(), null, null, Calendar.DAY_OF_MONTH));
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));

        toDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));

        JButton runButton = new JButton("Calculer les statistiques");
        runButton.addActionListener(e -> handleRun());

        constraints.gridx = 0;
        constraints.gridy = 0;
        criteria.add(new JLabel("Période du :"), constraints);
        constraints.gridx = 1;
        criteria.add(fromDateSpinner, constraints);
        constraints.gridx = 2;
        criteria.add(new JLabel("Au :"), constraints);
        constraints.gridx = 3;
        criteria.add(toDateSpinner, constraints);
        constraints.gridx = 4;
        criteria.add(runButton, constraints);

        add(criteria, BorderLayout.PAGE_START);
    }

    private void buildReportArea() {
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }

    private void buildFooter() {
        statusLabel = new JLabel(" ");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void handleRun() {
        statusLabel.setText(" ");
        try {
            java.util.Date fromValue = (java.util.Date) fromDateSpinner.getValue();
            java.util.Date toValue = (java.util.Date) toDateSpinner.getValue();
            if (fromValue == null || toValue == null) {
                JOptionPane.showMessageDialog(this,
                        "Les deux dates doivent être renseignées.",
                        "Erreur de validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fromValue.after(toValue)) {
                JOptionPane.showMessageDialog(this,
                        "La date de début doit être antérieure à la date de fin.",
                        "Erreur de validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Date from = toSqlDate(fromValue);
            Date to = toSqlDate(toValue);

            List<InscriptionDetail> all = controller.getAllInscriptions();
            BigDecimal totalRevenue = controller.computeTotalRevenueFromConfirmedInscriptions(all);
            double rate = controller.computeConfirmationRate(all);
            int incomplete = controller.countIncompleteInscriptions(all);
            int confirmedInPeriod = controller.countConfirmedInscriptionsInPeriod(from, to);

            StringBuilder report = new StringBuilder();
            report.append("Rapport de statistiques\n");
            report.append("=======================\n");
            report.append("Total des inscriptions en base : ").append(all.size()).append("\n");
            report.append("Inscriptions confirmées entre ").append(from).append(" et ").append(to)
                    .append(" : ").append(confirmedInPeriod).append("\n");
            report.append(String.format("Taux de confirmation (hors annulées) : %.2f %%%n", rate));
            report.append("Inscriptions incomplètes (autorisation, certificat ou paiement manquant) : ")
                    .append(incomplete).append("\n");
            report.append("Recettes totales des inscriptions confirmées et payées : ")
                    .append(totalRevenue).append(" EUR\n");

            reportArea.setText(report.toString());
            statusLabel.setText("Rapport généré avec succès.");
        } catch (AllInscriptionsException allInscriptionsException) {
            statusLabel.setText("Impossible de charger les inscriptions.");
            JOptionPane.showMessageDialog(this,
                    allInscriptionsException.getMessage(),
                    "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
        } catch (BusinessTaskException businessTaskException) {
            statusLabel.setText("La tâche métier a échoué.");
            JOptionPane.showMessageDialog(this,
                    businessTaskException.getMessage(),
                    "Erreur de tâche métier", JOptionPane.ERROR_MESSAGE);
        } catch (SearchInscriptionException searchInscriptionException) {
            statusLabel.setText("La recherche a échoué.");
            JOptionPane.showMessageDialog(this,
                    searchInscriptionException.getMessage(),
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
