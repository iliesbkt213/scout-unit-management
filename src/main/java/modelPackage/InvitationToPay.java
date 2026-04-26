package modelPackage;

import java.math.BigDecimal;
import java.sql.Date;

public class InvitationToPay {

    private Integer identifier;
    private BigDecimal amount;
    private Date sendingDate;
    private String communication;

    public InvitationToPay() {
    }

    public InvitationToPay(BigDecimal amount, Date sendingDate, String communication) {
        setAmount(amount);
        setSendingDate(sendingDate);
        setCommunication(communication);
    }

    public InvitationToPay(Integer identifier, BigDecimal amount, Date sendingDate, String communication) {
        setIdentifier(identifier);
        setAmount(amount);
        setSendingDate(sendingDate);
        setCommunication(communication);
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        if (identifier != null && identifier <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
        this.identifier = identifier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Le montant est obligatoire.");
        }
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif.");
        }
        if (amount.compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException("Le montant est trop grand.");
        }
        this.amount = amount;
    }

    public Date getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        if (sendingDate == null) {
            throw new IllegalArgumentException("La date d'envoi est obligatoire.");
        }
        this.sendingDate = sendingDate;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        if (communication == null) {
            throw new IllegalArgumentException("La communication est obligatoire.");
        }
        if (communication.trim().isEmpty()) {
            throw new IllegalArgumentException("La communication ne peut pas être vide.");
        }
        this.communication = communication;
    }

    @Override
    public String toString() {
        return communication + " (" + amount + " EUR)";
    }
}
