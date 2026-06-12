package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.LoadReferenceDataException;
import modelPackage.Address;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.Role;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewPersonDialog extends JDialog {

    private final ApplicationController controller;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> genderCombo;
    private JSpinner birthDateSpinner;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<Address> addressCombo;
    private JComboBox<Role> roleCombo;
    private boolean created;
    private Integer createdPersonRoleId;

    public NewPersonDialog(java.awt.Window owner, ApplicationController controller) {
        super(owner, "Créer une nouvelle personne", ModalityType.APPLICATION_MODAL);
        if (controller == null) {
            throw new IllegalArgumentException("Le contrôleur ne peut pas être nul.");
        }
        this.controller = controller;
        this.created = false;
        this.createdPersonRoleId = null;
        setLayout(new BorderLayout(10, 10));
        buildHeader();
        buildForm();
        buildFooter();
        loadReferenceData();
        pack();
        setMinimumSize(new Dimension(520, 420));
        setLocationRelativeTo(owner);
    }

    public boolean isCreated() {
        return created;
    }

    public Integer getCreatedPersonRoleId() {
        return createdPersonRoleId;
    }

    private void buildHeader() {
        JLabel title = new JLabel("Créer une nouvelle personne et son rôle", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);
    }

    private void buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        firstNameField = new JTextField(20);
        addRow(form, c, row++, "Prénom :", firstNameField);

        lastNameField = new JTextField(20);
        addRow(form, c, row++, "Nom :", lastNameField);

        genderCombo = new JComboBox<>(new String[]{"M", "F", "X"});
        addRow(form, c, row++, "Genre :", genderCombo);

        birthDateSpinner = new JSpinner(new SpinnerDateModel());
        birthDateSpinner.setEditor(new JSpinner.DateEditor(birthDateSpinner, "yyyy-MM-dd"));
        Calendar defaultBirth = Calendar.getInstance();
        defaultBirth.add(Calendar.YEAR, -10);
        birthDateSpinner.setValue(defaultBirth.getTime());
        addRow(form, c, row++, "Date de naissance :", birthDateSpinner);

        emailField = new JTextField(20);
        addRow(form, c, row++, "Email :", emailField);

        phoneField = new JTextField(20);
        addRow(form, c, row++, "Téléphone :", phoneField);

        addressCombo = new JComboBox<>();
        addRow(form, c, row++, "Adresse :", addressCombo);

        roleCombo = new JComboBox<>();
        addRow(form, c, row++, "Rôle :", roleCombo);

        add(form, BorderLayout.CENTER);
    }

    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createButton = new JButton("Créer");
        JButton cancelButton = new JButton("Annuler");
        createButton.addActionListener(e -> handleCreate());
        cancelButton.addActionListener(e -> dispose());
        footer.add(createButton);
        footer.add(cancelButton);
        add(footer, BorderLayout.SOUTH);
    }

    private void addRow(JPanel form, GridBagConstraints c, int row, String label,
                        java.awt.Component component) {
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;
        form.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        form.add(component, c);
    }

    private void loadReferenceData() {
        try {
            addressCombo.removeAllItems();
            for (Address address : loadAddresses()) {
                addressCombo.addItem(address);
            }
            roleCombo.removeAllItems();
            for (Role role : controller.getAllRoles()) {
                roleCombo.addItem(role);
            }
        } catch (LoadReferenceDataException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Address> loadAddresses() throws LoadReferenceDataException {
        // On reconstruit la liste d'adresses depuis les personnes existantes
        // pour éviter d'ajouter une méthode supplémentaire au DAO.
        List<Address> addresses = new ArrayList<>();
        List<Integer> seenIds = new ArrayList<>();
        for (Person p : controller.getAllPersons()) {
            if (p.getAddressId() != null && !seenIds.contains(p.getAddressId())) {
                Address a = new Address();
                a.setAddressId(p.getAddressId());
                a.setCityId(1);
                a.setStreet("Adresse #" + p.getAddressId());
                a.setNumber("-");
                addresses.add(a);
                seenIds.add(p.getAddressId());
            }
        }
        return addresses;
    }

    private void handleCreate() {
        List<String> errors = new ArrayList<>();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Address selectedAddress = (Address) addressCombo.getSelectedItem();
        Role selectedRole = (Role) roleCombo.getSelectedItem();
        java.util.Date utilBirth = (java.util.Date) birthDateSpinner.getValue();

        if (firstName.isEmpty()) errors.add("- Le prénom est obligatoire.");
        if (lastName.isEmpty()) errors.add("- Le nom est obligatoire.");
        if (email.isEmpty()) errors.add("- L'email est obligatoire.");
        if (phone.isEmpty()) errors.add("- Le téléphone est obligatoire.");
        if (selectedAddress == null) errors.add("- Une adresse doit être sélectionnée.");
        if (selectedRole == null) errors.add("- Un rôle doit être sélectionné.");
        if (utilBirth == null) errors.add("- La date de naissance est obligatoire.");

        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errors),
                    "Erreur de validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Person person = new Person();
            person.setAddressId(selectedAddress.getAddressId());
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setGender(gender);
            person.setBirthDate(toSqlDate(utilBirth));
            person.setEmail(email);
            person.setPhone(phone);

            int newPersonId = controller.addPerson(person);

            PersonRole personRole = new PersonRole();
            personRole.setPersonId(newPersonId);
            personRole.setRoleLabel(selectedRole.getLabel());
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            personRole.setStartDate(new Date(today.getTimeInMillis()));

            int newPersonRoleId = controller.addPersonRole(personRole);

            createdPersonRoleId = newPersonRoleId;
            created = true;
            JOptionPane.showMessageDialog(this,
                    "Personne ajoutée : " + firstName + " " + lastName
                            + " (rôle " + selectedRole.getLabel() + ").",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (LoadReferenceDataException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException illegalArgumentException) {
            JOptionPane.showMessageDialog(this, illegalArgumentException.getMessage(),
                    "Erreur de validation", JOptionPane.WARNING_MESSAGE);
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
