package testPackage;

import modelPackage.InvitationToPay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvitationToPayTest {

    @Test
    void setAmountPositiveShouldBeAccepted() {
        InvitationToPay invitation = new InvitationToPay();
        invitation.setAmount(new BigDecimal("120.50"));
        assertEquals(new BigDecimal("120.50"), invitation.getAmount());
    }

    @Test
    void setAmountZeroShouldBeAccepted() {
        InvitationToPay invitation = new InvitationToPay();
        invitation.setAmount(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, invitation.getAmount());
    }

    @Test
    void setAmountNegativeShouldThrow() {
        InvitationToPay invitation = new InvitationToPay();
        assertThrows(IllegalArgumentException.class,
                () -> invitation.setAmount(new BigDecimal("-1.00")));
    }

    @Test
    void setAmountNullShouldThrow() {
        InvitationToPay invitation = new InvitationToPay();
        assertThrows(IllegalArgumentException.class,
                () -> invitation.setAmount(null));
    }

    @Test
    void setAmountTooBigShouldThrow() {
        InvitationToPay invitation = new InvitationToPay();
        assertThrows(IllegalArgumentException.class,
                () -> invitation.setAmount(new BigDecimal("100000000.00")));
    }

    @Test
    void setCommunicationEmptyShouldThrow() {
        InvitationToPay invitation = new InvitationToPay();
        assertThrows(IllegalArgumentException.class,
                () -> invitation.setCommunication("   "));
    }
}
