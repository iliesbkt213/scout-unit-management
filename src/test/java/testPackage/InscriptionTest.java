package testPackage;

import modelPackage.Inscription;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InscriptionTest {

    @Test
    void setStatusValidValuesShouldBeAccepted() {
        Inscription inscription = new Inscription();
        inscription.setStatus(Inscription.STATUS_PENDING);
        assertEquals(Inscription.STATUS_PENDING, inscription.getStatus());
        inscription.setStatus(Inscription.STATUS_CONFIRMED);
        assertEquals(Inscription.STATUS_CONFIRMED, inscription.getStatus());
        inscription.setStatus(Inscription.STATUS_REJECTED);
        assertEquals(Inscription.STATUS_REJECTED, inscription.getStatus());
        inscription.setStatus(Inscription.STATUS_CANCELLED);
        assertEquals(Inscription.STATUS_CANCELLED, inscription.getStatus());
    }

    @Test
    void setStatusUnknownValueShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setStatus("UNKNOWN"));
    }

    @Test
    void setStatusNullShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setStatus(null));
    }

    @Test
    void setStatusEmptyShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setStatus("   "));
    }

    @Test
    void setPersonRoleIdNullShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setPersonRoleId(null));
    }

    @Test
    void setPersonRoleIdNegativeShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setPersonRoleId(-1));
    }

    @Test
    void setPersonRoleIdZeroShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setPersonRoleId(0));
    }

    @Test
    void setLegalGuardianIdNullShouldBeAccepted() {
        Inscription inscription = new Inscription();
        inscription.setLegalGuardianId(null);
        assertNull(inscription.getLegalGuardianId());
    }

    @Test
    void setLegalGuardianIdNegativeShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setLegalGuardianId(-5));
    }

    @Test
    void setRegistrationDateNullShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setRegistrationDate(null));
    }

    @Test
    void setParentAuthorizationNullShouldThrow() {
        Inscription inscription = new Inscription();
        assertThrows(IllegalArgumentException.class,
                () -> inscription.setParentAuthorization(null));
    }

    @Test
    void completeInscriptionShouldBeReportedAsComplete() {
        Inscription inscription = buildValidInscription();
        inscription.setMedicalCertificate(true);
        inscription.setParentAuthorization(true);
        inscription.setPaymentFinish(true);
        assertTrue(inscription.isComplete());
    }

    @Test
    void incompleteInscriptionShouldNotBeReportedAsComplete() {
        Inscription inscription = buildValidInscription();
        inscription.setMedicalCertificate(false);
        inscription.setParentAuthorization(true);
        inscription.setPaymentFinish(true);
        assertEquals(false, inscription.isComplete());
    }

    private Inscription buildValidInscription() {
        Inscription inscription = new Inscription();
        inscription.setPersonRoleId(1);
        inscription.setUnitScoutId(1);
        inscription.setInvitationToPayId(1);
        inscription.setStatus(Inscription.STATUS_PENDING);
        inscription.setParentAuthorization(true);
        inscription.setRegistrationDate(Date.valueOf(LocalDate.now()));
        inscription.setMedicalCertificate(true);
        inscription.setPaymentFinish(false);
        assertNotNull(inscription);
        return inscription;
    }
}
