package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.AddInscriptionException;
import exceptionPackage.LoadReferenceDataException;
import modelPackage.Inscription;
import modelPackage.InvitationToPay;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.UnitScout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddInscriptionPanel extends JPanel {

    private ApplicationController controller;

    private JComboBox<PersonRole> personRoleCombo;
    private JComboBox<UnitScout> unitScoutCombo;
    private JComboBox<InvitationToPay> invitationToPayCombo;
    private JComboBox<Person> legalGuardianCombo;
    private JComboBox<String> statusCombo;
    private JCheckBox parentAuthorizationCheck;
    private JCheckBox medicalCertificateCheck;
    private JCheckBox paymentFinishCheck;
    private JSpinner registrationDateSpinner;
    private JCheckBox preRegistrationDateCheck;
    private JSpinner preRegistrationDateSpinner;
    private JCheckBox confirmationDateCheck;
    private JSpinner confirmationDateSpinner;
    private JLabel statusLabel;

    public AddInscriptionPanel(ApplicationController controller) {
        setController(controller);
        setLayout(new BorderLayout(10, 10));
        buildHeader();
        buildForm();
        buildFooter();
        reloadReferenceData();
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
        JLabel title = new JLabel("Ajouter une nouvelle inscription", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);
    }

    private void buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        personRoleCombo = new JComboBox<>();
        addRow(form, constraints, row++, "Personne inscrite (rôle) :", personRoleCombo);

        unitScoutCombo = new JComboBox<>();
        addRow(form, constraints, row++, "Unité :", unitScoutCombo);

        invitationToPayCombo = new JComboBox<>();
        addRow(form, constraints, row++, "Invitation à payer :", invitationToPayCombo);

        legalGuardianCombo = new JComboBox<>();
        addRow(form, constraints, row++, "Tuteur légal (facultatif) :", legalGuardianCombo);

        statusCombo = new JComboBox<>(new String[]{
                Inscription.STATUS_PENDING, Inscription.STATUS_CONFIRMED,
                Inscription.STATUS_REJECTED, Inscription.STATUS_CANCELLED});
        addRow(form, constraints, row++, "Statut :", statusCombo);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        registrationDateSpinner = new JSpinner(dateModel);
        registrationDateSpinner.setEditor(new JSpinner.DateEditor(registrationDateSpinner, "yyyy-MM-dd"));
        addRow(form, constraints, row++, "Date d'inscription :", registrationDateSpinner);

        preRegistrationDateSpinner = new JSpinner(new SpinnerDateModel());
        preRegistrationDateSpinner.setEditor(new JSpinner.DateEditor(preRegistrationDateSpinner, "yyyy-MM-dd"));
        preRegistrationDateSpinner.setEnabled(false);
        preRegistrationDateCheck = new JCheckBox("Renseigner");
        preRegistrationDateCheck.addActionListener(e -> preRegistrationDateSpinner.setEnabled(preRegistrationDateCheck.isSelected()));
        addRow(form, constraints, row++, "Date de pré-inscription (facultatif) :",
                buildOptionalDateRow(preRegistrationDateCheck, preRegistrationDateSpinner));

        confirmationDateSpinner = new JSpinner(new SpinnerDateModel());
        confirmationDateSpinner.setEditor(new JSpinner.DateEditor(confirmationDateSpinner, "yyyy-MM-dd"));
        confirmationDateSpinner.setEnabled(false);
        confirmationDateCheck = new JCheckBox("Renseigner");
        confirmationDateCheck.addActionListener(e -> confirmationDateSpinner.setEnabled(confirmationDateCheck.isSelected()));
        addRow(form, constraints, row++, "Date de confirmation (facultatif) :",
                buildOptionalDateRow(confirmationDateCheck, confirmationDateSpinner));

        parentAuthorizationCheck = new JCheckBox();
        addRow(form, constraints, row++, "Autorisation parentale :", parentAuthorizationCheck);

        medicalCertificateCheck = new JCheckBox();
        addRow(form, constraints, row++, "Certificat médical :", medicalCertificateCheck);

        paymentFinishCheck = new JCheckBox();
        addRow(form, constraints, row++, "Paiement effectué :", paymentFinishCheck);

        add(new JScrollPane(form), BorderLayout.CENTER);
    }

    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Ajouter");
        JButton clearButton = new JButton("Réinitialiser");
        JButton newPersonButton = new JButton("Nouvelle personne...");
        addButton.addActionListener(e -> handleAdd());
        clearButton.addActionListener(e -> clearForm());
        newPersonButton.addActionListener(e -> handleNewPerson());
        statusLabel = new JLabel(" ");
        footer.add(addButton);
        footer.add(clearButton);
        footer.add(newPersonButton);
        footer.add(statusLabel);
        add(footer, BorderLayout.SOUTH);
    }

    private void handleNewPerson() {
        java.awt.Window owner = javax.swing.SwingUtilities.getWindowAncestor(this);
        NewPersonDialog dialog = new NewPersonDialog(owner, controller);
        dialog.setVisible(true);
        if (dialog.isCreated()) {
            reloadReferenceData();
            Integer newPersonRoleId = dialog.getCreatedPersonRoleId();
            if (newPersonRoleId != null) {
                for (int i = 0; i < personRoleCombo.getItemCount(); i++) {
                    PersonRole pr = personRoleCombo.getItemAt(i);
                    if (pr != null && newPersonRoleId.equals(pr.getIdentifier())) {
                        personRoleCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            statusLabel.setText("Nouvelle personne créée et sélectionnée.");
        }
    }

    private void addRow(JPanel form, GridBagConstraints constraints, int row,
                        String label, java.awt.Component component) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0;
        form.add(new JLabel(label), constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        form.add(component, constraints);
    }

    private JPanel buildOptionalDateRow(JCheckBox toggle, JSpinner spinner) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        container.add(toggle);
        container.add(spinner);
        return container;
    }

    public void reloadReferenceData() {
        try {
            personRoleCombo.removeAllItems();
            for (PersonRole personRole : controller.getAllPersonRoles()) {
                personRoleCombo.addItem(personRole);
            }
            unitScoutCombo.removeAllItems();
            for (UnitScout unitScout : controller.getAllUnitScouts()) {
                unitScoutCombo.addItem(unitScout);
            }
            invitationToPayCombo.removeAllItems();
            for (InvitationToPay invitation : controller.getAllInvitationsToPay()) {
                invitationToPayCombo.addItem(invitation);
            }
            legalGuardianCombo.removeAllItems();
            legalGuardianCombo.addItem(null);
            for (Person person : controller.getAllPersons()) {
                legalGuardianCombo.addItem(person);
            }
        } catch (LoadReferenceDataException exception) {
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Erreur de chargement des données", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        statusLabel.setText(" ");
        List<String> errors = validateForm();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errors),
                    "Erreur de validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            PersonRole selectedPersonRole = (PersonRole) personRoleCombo.getSelectedItem();
            UnitScout selectedUnit = (UnitScout) unitScoutCombo.getSelectedItem();
            InvitationToPay selectedInvitation = (InvitationToPay) invitationToPayCombo.getSelectedItem();
            Person selectedGuardian = (Person) legalGuardianCombo.getSelectedItem();

            Inscription inscription = new Inscription(
                    selectedPersonRole.getIdentifier(),
                    selectedUnit.getIdentifier(),
                    selectedInvitation.getIdentifier(),
                    (String) statusCombo.getSelectedItem(),
                    parentAuthorizationCheck.isSelected(),
                    toSqlDate((java.util.Date) registrationDateSpinner.getValue()),
                    medicalCertificateCheck.isSelected(),
                    paymentFinishCheck.isSelected(),
                    selectedGuardian == null ? null : selectedGuardian.getPersonId());
            if (preRegistrationDateCheck.isSelected()) {
                inscription.setPreRegistrationDate(
                        toSqlDate((java.util.Date) preRegistrationDateSpinner.getValue()));
            }
            if (confirmationDateCheck.isSelected()) {
                inscription.setConfirmationDate(
                        toSqlDate((java.util.Date) confirmationDateSpinner.getValue()));
            }

            int generatedId = controller.addInscription(inscription);
            statusLabel.setText("Inscription ajoutée avec l'identifiant " + generatedId + ".");
            JOptionPane.showMessageDialog(this,
                    "Inscription ajoutée avec l'identifiant " + generatedId + ".",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (AddInscriptionException exception) {
            statusLabel.setText("Impossible d'ajouter l'inscription.");
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException illegalArgumentException) {
            statusLabel.setText("Erreur de validation.");
            JOptionPane.showMessageDialog(this,
                    illegalArgumentException.getMessage(),
                    "Erreur de validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    private List<String> validateForm() {
        List<String> errors = new ArrayList<>();
        if (personRoleCombo.getSelectedItem() == null) {
            errors.add("- Une personne/rôle doit être sélectionné(e).");
        }
        if (unitScoutCombo.getSelectedItem() == null) {
            errors.add("- Une unité doit être sélectionnée.");
        }
        if (invitationToPayCombo.getSelectedItem() == null) {
            errors.add("- Une invitation à payer doit être sélectionnée.");
        }
        if (statusCombo.getSelectedItem() == null) {
            errors.add("- Un statut doit être choisi.");
        }
        if (registrationDateSpinner.getValue() == null) {
            errors.add("- La date d'inscription doit être renseignée.");
        }
        java.util.Date registrationDate = (java.util.Date) registrationDateSpinner.getValue();
        if (registrationDate != null && registrationDate.after(new java.util.Date())) {
            errors.add("- La date d'inscription ne peut pas être dans le futur.");
        }
        if (paymentFinishCheck.isSelected()
                && !Inscription.STATUS_CONFIRMED.equals(statusCombo.getSelectedItem())) {
            errors.add("- Un paiement effectué nécessite le statut CONFIRMED.");
        }
        return errors;
    }

    private void clearForm() {
        personRoleCombo.setSelectedIndex(personRoleCombo.getItemCount() > 0 ? 0 : -1);
        unitScoutCombo.setSelectedIndex(unitScoutCombo.getItemCount() > 0 ? 0 : -1);
        invitationToPayCombo.setSelectedIndex(invitationToPayCombo.getItemCount() > 0 ? 0 : -1);
        legalGuardianCombo.setSelectedIndex(0);
        statusCombo.setSelectedItem(Inscription.STATUS_PENDING);
        parentAuthorizationCheck.setSelected(false);
        medicalCertificateCheck.setSelected(false);
        paymentFinishCheck.setSelected(false);
        Calendar calendar = Calendar.getInstance();
        registrationDateSpinner.setValue(calendar.getTime());
        preRegistrationDateCheck.setSelected(false);
        preRegistrationDateSpinner.setValue(calendar.getTime());
        preRegistrationDateSpinner.setEnabled(false);
        confirmationDateCheck.setSelected(false);
        confirmationDateSpinner.setValue(calendar.getTime());
        confirmationDateSpinner.setEnabled(false);
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
