package com.example.demo.Schedule;

import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.dao.PaymentDAO;
import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.service.EnrollementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.threeten.bp.DateTimeUtils.toInstant;

@ExtendWith(MockitoExtension.class)
class PaimentScheduleTest {

    @Mock
    private EnrollementService enrollementService;

    @Mock
    private PaymentDAO paymentDAO;

    @InjectMocks
    private PaimentSchedule paimentSchedule;

    private EnrollementDto enrollment;
    private final Long enrollmentId = 1L;

    @BeforeEach
    void setUp() {
        enrollment = new EnrollementDto();
        enrollment.setEnrollementId(enrollmentId);
    }

    @Test
    void update_Payment_WhenLastPaymentExpired_SetsStatusToUnPaid() {
        // Arrange
        LocalDate expiredLastPaymentDate = getDateDaysAgo(31).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // Expired 1 day ago
        when(enrollementService.getAllEnrollements()).thenReturn(List.of(enrollment));
        when(paymentDAO.findLastPaymentDateByEnrollement(enrollmentId)).thenReturn(expiredLastPaymentDate);

        // Act
        paimentSchedule.update_Payment();

        // Assert
        verifyUpdateEnrollmentWithStatus(PaymentStatus.UnPaid);
    }

    @Test
    void update_Payment_WhenLastPaymentNotExpired_SetsStatusTopaid() {
        // Arrange
        LocalDate validLastPaymentDate = getDateDaysAgo(29).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // Expires in 1 day
        when(enrollementService.getAllEnrollements()).thenReturn(List.of(enrollment));
        when(paymentDAO.findLastPaymentDateByEnrollement(enrollmentId)).thenReturn(validLastPaymentDate);

        // Act
        paimentSchedule.update_Payment();

        // Assert
        verifyUpdateEnrollmentWithStatus(PaymentStatus.Paid);
    }

    @Test
    void update_Payment_WhenNoEnrollements_ThrowsException() {
        // Arrange
        when(enrollementService.getAllEnrollements()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThatThrownBy(() -> paimentSchedule.update_Payment())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No enrollement found");
    }

    @Test
    void update_Payment_WhenLastPaymentDateIsNull_DoesNotUpdate() {
        // Arrange
        when(enrollementService.getAllEnrollements()).thenReturn(List.of(enrollment));
        when(paymentDAO.findLastPaymentDateByEnrollement(enrollmentId)).thenReturn(null);

        // Act
        paimentSchedule.update_Payment();

        // Assert
        verify(enrollementService, never()).updateEnrollement(any());
    }

    // Helper method to verify enrollment status updates
    private void verifyUpdateEnrollmentWithStatus(PaymentStatus expectedStatus) {
        ArgumentCaptor<EnrollementDto> enrollmentCaptor = ArgumentCaptor.forClass(EnrollementDto.class);
        verify(enrollementService).updateEnrollement(enrollmentCaptor.capture());
        assertThat(enrollmentCaptor.getValue().getPaymentStatus()).isEqualTo(expectedStatus);
    }

    // Helper method to create test dates
    private Date getDateDaysAgo(int daysAgo) {
        return Date.from(
                LocalDate.now()
                        .minusDays(daysAgo)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
    }
}