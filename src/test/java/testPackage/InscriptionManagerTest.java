package testPackage;

import businessPackage.InscriptionManager;
import dataAccessPackage.InscriptionDataAccess;
import exceptionPackage.AddInscriptionException;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.BusinessTaskException;
import exceptionPackage.DeleteInscriptionException;
import exceptionPackage.InscriptionNotFoundException;
import exceptionPackage.SearchInscriptionException;
import exceptionPackage.UpdateInscriptionException;
import modelPackage.Inscription;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InscriptionManagerTest {

    private InscriptionManager manager;

    @BeforeEach
    void setUp() {
        manager = new InscriptionManager(new FakeDao());
    }

    @Test
    void computeTotalRevenueShouldOnlyAddConfirmedAndPaid() throws BusinessTaskException {
        List<InscriptionDetail> details = new ArrayList<>();
        details.add(buildDetail(Inscription.STATUS_CONFIRMED, true, new BigDecimal("100")));
        details.add(buildDetail(Inscription.STATUS_CONFIRMED, false, new BigDecimal("50")));
        details.add(buildDetail(Inscription.STATUS_PENDING, true, new BigDecimal("75")));
        details.add(buildDetail(Inscription.STATUS_CONFIRMED, true, new BigDecimal("25")));

        BigDecimal revenue = manager.computeTotalRevenueFromConfirmedInscriptions(details);
        assertEquals(new BigDecimal("125"), revenue);
    }

    @Test
    void computeTotalRevenueWithNullListShouldThrow() {
        assertThrows(BusinessTaskException.class,
                () -> manager.computeTotalRevenueFromConfirmedInscriptions(null));
    }

    @Test
    void computeTotalRevenueOnEmptyListShouldReturnZero() throws BusinessTaskException {
        BigDecimal revenue = manager.computeTotalRevenueFromConfirmedInscriptions(
                Collections.emptyList());
        assertEquals(BigDecimal.ZERO, revenue);
    }

    @Test
    void computeConfirmationRateShouldExcludeCancelled() throws BusinessTaskException {
        List<InscriptionDetail> details = new ArrayList<>();
        details.add(buildDetail(Inscription.STATUS_CONFIRMED, true, new BigDecimal("100")));
        details.add(buildDetail(Inscription.STATUS_PENDING, true, new BigDecimal("100")));
        details.add(buildDetail(Inscription.STATUS_CANCELLED, false, new BigDecimal("100")));
        details.add(buildDetail(Inscription.STATUS_CONFIRMED, true, new BigDecimal("100")));

        double rate = manager.computeConfirmationRate(details);
        assertEquals(2.0 / 3.0 * 100.0, rate, 0.0001);
    }

    @Test
    void computeConfirmationRateOnEmptyListShouldReturnZero() throws BusinessTaskException {
        assertEquals(0.0, manager.computeConfirmationRate(Collections.emptyList()), 0.0001);
    }

    @Test
    void countIncompleteInscriptionsShouldDetectMissingRequirements()
            throws BusinessTaskException {
        List<InscriptionDetail> details = new ArrayList<>();
        details.add(buildDetail(Inscription.STATUS_CONFIRMED, true, BigDecimal.ONE));
        InscriptionDetail incomplete = new InscriptionDetail();
        incomplete.setStatus(Inscription.STATUS_PENDING);
        incomplete.setParentAuthorization(true);
        incomplete.setMedicalCertificate(false);
        incomplete.setPaymentFinish(true);
        details.add(incomplete);

        assertEquals(1, manager.countIncompleteInscriptions(details));
    }

    @Test
    void addInscriptionWithFinishedPaymentButPendingShouldThrow() {
        Inscription inscription = buildValidInscription();
        inscription.setPaymentFinish(true);
        inscription.setStatus(Inscription.STATUS_PENDING);
        assertThrows(AddInscriptionException.class,
                () -> manager.addInscription(inscription));
    }

    @Test
    void addInscriptionConfirmedWithoutMedicalCertificateShouldThrow() {
        Inscription inscription = buildValidInscription();
        inscription.setStatus(Inscription.STATUS_CONFIRMED);
        inscription.setMedicalCertificate(false);
        assertThrows(AddInscriptionException.class,
                () -> manager.addInscription(inscription));
    }

    @Test
    void countConfirmedInscriptionsInPeriodShouldRejectInvertedDates() {
        Date from = Date.valueOf(LocalDate.of(2026, 6, 1));
        Date to = Date.valueOf(LocalDate.of(2026, 1, 1));
        assertThrows(BusinessTaskException.class,
                () -> manager.countConfirmedInscriptionsInPeriod(from, to));
    }

    private InscriptionDetail buildDetail(String status, boolean paid, BigDecimal amount) {
        InscriptionDetail detail = new InscriptionDetail();
        detail.setStatus(status);
        detail.setPaymentFinish(paid);
        detail.setParentAuthorization(true);
        detail.setMedicalCertificate(true);
        detail.setAmount(amount);
        return detail;
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
        return inscription;
    }

    private static class FakeDao implements InscriptionDataAccess {
        @Override
        public int addInscription(Inscription inscription) throws AddInscriptionException {
            return 1;
        }

        @Override
        public void updateInscription(Inscription inscription)
                throws UpdateInscriptionException, InscriptionNotFoundException {
        }

        @Override
        public void deleteInscription(int identifier)
                throws DeleteInscriptionException, InscriptionNotFoundException {
        }

        @Override
        public List<InscriptionDetail> getAllInscriptions() throws AllInscriptionsException {
            return Collections.emptyList();
        }

        @Override
        public Inscription getInscriptionById(int identifier)
                throws AllInscriptionsException, InscriptionNotFoundException {
            throw new InscriptionNotFoundException("Not used in tests.");
        }

        @Override
        public List<InscriptionDetail> searchInscriptionsByUnitAndDate(
                InscriptionSearchCriteria criteria) throws SearchInscriptionException {
            return Collections.emptyList();
        }

        @Override
        public List<InscriptionDetail> searchInscriptionsByRoleAndCity(
                InscriptionSearchCriteria criteria) throws SearchInscriptionException {
            return Collections.emptyList();
        }

        @Override
        public List<InscriptionDetail> searchPendingInscriptionsByStatus(
                InscriptionSearchCriteria criteria) throws SearchInscriptionException {
            return Collections.emptyList();
        }

        @Override
        public int countConfirmedInscriptionsInPeriod(Date from, Date to)
                throws SearchInscriptionException {
            return 0;
        }
    }
}
