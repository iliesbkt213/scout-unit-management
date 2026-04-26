package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.DeleteInscriptionException;
import exceptionPackage.InscriptionNotFoundException;
import exceptionPackage.LoadReferenceDataException;
import exceptionPackage.UpdateInscriptionException;
import modelPackage.Inscription;
import modelPackage.InscriptionDetail;
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

public class UpdateInscriptionPanel extends JPanel {

    private ApplicationController controller;

    private JComboBox<InscriptionDetailWrapper> inscriptionsCombo;
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

    public UpdateInscriptionPanel(ApplicationController controller) {
        setController(controller);
        setLayout(new BorderLayout(10, 10));
        buildHeader();
        buildForm();
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
        JLabel title = new JLabel("Modifier ou supprimer une inscription", SwingConstants.CENTER);
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

        inscriptionsCombo = new JComboBox<>();
        inscriptionsCombo.addActionListener(e -> populateFormFromSelection());
        addRow(form, constraints, row++, "Inscription :", inscriptionsCombo);

        personRoleCombo = new JComboBox<>();
        addRow(form, constraints, row++, "Personne/rôle :", personRoleCombo);

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
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Rafraîchir");
        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> reloadAll());
        statusLabel = new JLabel(" ");
        footer.add(updateButton);
        footer.add(deleteButton);
        footer.add(refreshButton);
        footer.add(statusLabel);
        add(footer, BorderLayout.SOUTH);
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

    public void reloadAll() {
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

            inscriptionsCombo.removeAllItems();
            for (InscriptionDetail detail : controller.getAllInscriptions()) {
                inscriptionsCombo.addItem(new InscriptionDetailWrapper(detail));
            }
            populateFormFromSelection();
        } catch (LoadReferenceDataException loadReferenceDataException) {
            JOptionPane.showMessageDialog(this,
                    loadReferenceDataException.getMessage(),
                    "Erreur de chargement des données", JOptionPane.ERROR_MESSAGE);
        } catch (AllInscriptionsException allInscriptionsException) {
            JOptionPane.showMessageDialog(this,
                    allInscriptionsException.getMessage(),
                    "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFormFromSelection() {
        Object selected = inscriptionsCombo.getSelectedItem();
        if (!(selected instanceof InscriptionDetailWrapper)) {
            return;
        }
        try {
            InscriptionDetailWrapper wrapper = (InscriptionDetailWrapper) selected;
            Inscription inscription = controller.getInscriptionById(
                    wrapper.detail.getInscriptionId());
            selectComboItem(personRoleCombo, inscription.getPersonRoleId());
            selectComboItem(unitScoutCombo, inscription.getUnitScoutId());
            selectComboItem(invitationToPayCombo, inscription.getInvitationToPayId());
            selectGuardian(inscription.getLegalGuardianId());
            statusCombo.setSelectedItem(inscription.getStatus());
            registrationDateSpinner.setValue(inscription.getRegistrationDate());
            if (inscription.getPreRegistrationDate() != null) {
                preRegistrationDateCheck.setSelected(true);
                preRegistrationDateSpinner.setEnabled(true);
                preRegistrationDateSpinner.setValue(inscription.getPreRegistrationDate());
            } else {
                preRegistrationDateCheck.setSelected(false);
                preRegistrationDateSpinner.setEnabled(false);
                preRegistrationDateSpinner.setValue(inscription.getRegistrationDate());
            }
            if (inscription.getConfirmationDate() != null) {
                confirmationDateCheck.setSelected(true);
                confirmationDateSpinner.setEnabled(true);
                confirmationDateSpinner.setValue(inscription.getConfirmationDate());
            } else {
                confirmationDateCheck.setSelected(false);
                confirmationDateSpinner.setEnabled(false);
                confirmationDateSpinner.setValue(inscription.getRegistrationDate());
            }
            parentAuthorizationCheck.setSelected(Boolean.TRUE.equals(inscription.getParentAuthorization()));
            medicalCertificateCheck.setSelected(Boolean.TRUE.equals(inscription.getMedicalCertificate()));
            paymentFinishCheck.setSelected(Boolean.TRUE.equals(inscription.getPaymentFinish()));
        } catch (AllInscriptionsException | InscriptionNotFoundException exception) {
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectComboItem(JComboBox<?> combo, Integer identifier) {
        if (identifier == null) {
            return;
        }
        for (int index = 0; index < combo.getItemCount(); index++) {
            Object item = combo.getItemAt(index);
            if (item == null) {
                continue;
            }
            Integer current = null;
            if (item instanceof PersonRole) {
                current = ((PersonRole) item).getIdentifier();
            } else if (item instanceof UnitScout) {
                current = ((UnitScout) item).getIdentifier();
            } else if (item instanceof InvitationToPay) {
                current = ((InvitationToPay) item).getIdentifier();
            }
            if (identifier.equals(current)) {
                combo.setSelectedIndex(index);
                return;
            }
        }
    }

    private void selectGuardian(Integer guardianId) {
        if (guardianId == null) {
            legalGuardianCombo.setSelectedIndex(0);
            return;
        }
        for (int index = 0; index < legalGuardianCombo.getItemCount(); index++) {
            Person person = legalGuardianCombo.getItemAt(index);
            if (person != null && guardianId.equals(person.getPersonId())) {
                legalGuardianCombo.setSelectedIndex(index);
                return;
            }
        }
    }

    private void handleUpdate() {
        statusLabel.setText(" ");
        Object selected = inscriptionsCombo.getSelectedItem();
        if (!(selected instanceof InscriptionDetailWrapper)) {
            statusLabel.setText("Veuillez sélectionner une inscription.");
            return;
        }
        List<String> errors = validateForm();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errors),
                    "Erreur de validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            InscriptionDetailWrapper wrapper = (InscriptionDetailWrapper) selected;
            PersonRole personRole = (PersonRole) personRoleCombo.getSelectedItem();
            UnitScout unitScout = (UnitScout) unitScoutCombo.getSelectedItem();
            InvitationToPay invitation = (InvitationToPay) invitationToPayCombo.getSelectedItem();
            Person guardian = (Person) legalGuardianCombo.getSelectedItem();

            Inscription inscription = new Inscription(
                    wrapper.detail.getInscriptionId(),
                    personRole.getIdentifier(),
                    unitScout.getIdentifier(),
                    invitation.getIdentifier(),
                    (String) statusCombo.getSelectedItem(),
                    parentAuthorizationCheck.isSelected(),
                    toSqlDate((java.util.Date) registrationDateSpinner.getValue()),
                    medicalCertificateCheck.isSelected(),
                    paymentFinishCheck.isSelected(),
                    guardian == null ? null : guardian.getPersonId());
            if (preRegistrationDateCheck.isSelected()) {
                inscription.setPreRegistrationDate(
                        toSqlDate((java.util.Date) preRegistrationDateSpinner.getValue()));
            }
            if (confirmationDateCheck.isSelected()) {
                inscription.setConfirmationDate(
                        toSqlDate((java.util.Date) confirmationDateSpinner.getValue()));
            }

            controller.updateInscription(inscription);
            statusLabel.setText("Inscription modifiée.");
            JOptionPane.showMessageDialog(this,
                    "Inscription modifiée.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            reloadAll();
        } catch (UpdateInscriptionException exception) {
            statusLabel.setText("Impossible de modifier.");
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Erreur de modification", JOptionPane.ERROR_MESSAGE);
        } catch (InscriptionNotFoundException notFoundException) {
            statusLabel.setText("Inscription introuvable.");
            JOptionPane.showMessageDialog(this,
                    notFoundException.getMessage(),
                    "Introuvable", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException illegalArgumentException) {
            statusLabel.setText("Erreur de validation.");
            JOptionPane.showMessageDialog(this,
                    illegalArgumentException.getMessage(),
                    "Erreur de validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete() {
        Object selected = inscriptionsCombo.getSelectedItem();
        if (!(selected instanceof InscriptionDetailWrapper)) {
            statusLabel.setText("Veuillez sélectionner une inscription.");
            return;
        }
        InscriptionDetailWrapper wrapper = (InscriptionDetailWrapper) selected;
        int answer = JOptionPane.showConfirmDialog(this,
                "Supprimer l'inscription #" + wrapper.detail.getInscriptionId() + " ?",
                "Confirmer la suppression", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            controller.deleteInscription(wrapper.detail.getInscriptionId());
            statusLabel.setText("Inscription supprimée.");
            JOptionPane.showMessageDialog(this,
                    "Inscription supprimée.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            reloadAll();
        } catch (DeleteInscriptionException deleteException) {
            statusLabel.setText("Impossible de supprimer.");
            JOptionPane.showMessageDialog(this,
                    deleteException.getMessage(),
                    "Erreur de suppression", JOptionPane.ERROR_MESSAGE);
        } catch (InscriptionNotFoundException notFoundException) {
            statusLabel.setText("Inscription introuvable.");
            JOptionPane.showMessageDialog(this,
                    notFoundException.getMessage(),
                    "Introuvable", JOptionPane.WARNING_MESSAGE);
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

    private Date toSqlDate(java.util.Date utilDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(utilDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    private static class InscriptionDetailWrapper {
        private final InscriptionDetail detail;

        InscriptionDetailWrapper(InscriptionDetail detail) {
            this.detail = detail;
        }

        @Override
        public String toString() {
            return "#" + detail.getInscriptionId() + " - "
                    + detail.getFullName() + " - " + detail.getStatus();
        }
    }
}
