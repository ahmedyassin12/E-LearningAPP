package com.example.demo.service;

import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.Dtos.paymentDto.CreatePaymentDto;
import com.example.demo.Dtos.paymentDto.PaymentDto;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.PaymentDAO;
import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.management.relation.InvalidRoleInfoException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private PaymentDAO paymentRepository;
    @Mock private EnrollementService enrollementService;
    @Mock private EnrollementDAO enrollementDAO ;
    @Mock private PaymentMapper paymentMapper;

    @Mock
    private ObjectValidator<CreatePaymentDto> paymentValidator ;

    @InjectMocks private PaymentService paymentService;

    private User mockUser;
    private Principal connectedUser;
    private Payment testPayment;
    private PaymentDto paymentDto;
    private CreatePaymentDto createPaymentDto;
    private Enrollement testEnrollement;
    private EnrollementDto testEnrollementDto;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .username("john_doe")
                .role(Role.STUDENT)
                .build();

        connectedUser = new UsernamePasswordAuthenticationToken(mockUser, null);

        testEnrollement = Enrollement.builder()
                .enrollement_id(10L)
                .build();

        testEnrollementDto=EnrollementDto.builder()
                .enrollementId(testEnrollement.getEnrollement_id())
                .build();

        testPayment = Payment.builder()
                .payment_id(100L)
                .amount(500.0)
                .paymentDate(LocalDate.now())
                .enrollement(testEnrollement)
                .build();

        paymentDto = PaymentDto.builder()
                .payment_id(100L)
                .amount(500.0)
                .paymentDate(LocalDate.now())
                .build();

        createPaymentDto = CreatePaymentDto.builder()
                .enrollement_id(10L)
                .paymentDto(paymentDto)
                .build();
    }

    // getAllPayments tests
    @Test
    void getAllPayments_ReturnsPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of(testPayment));
        when(paymentMapper.returnPaymentDto(testPayment)).thenReturn(paymentDto);

        List<PaymentDto> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        assertEquals(paymentDto, result.get(0));
    }

    @Test
    void getAllPayments_NoPayments_ThrowsException() {
        when(paymentRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,
                () -> paymentService.getAllPayments());
    }

    // getPaymentsOfEnrollement tests
    @Test
    void getPaymentsOfEnrollement_StudentRole_ReturnsPayments() throws Exception {
        mockUser.setRole(Role.STUDENT);
        when(paymentRepository.getPaiementsOfEnrollementOfStudent(10L, 1L))
                .thenReturn(List.of(testPayment));
        when(paymentMapper.returnPaymentDto(testPayment)).thenReturn(paymentDto);

        Iterable<PaymentDto> result = paymentService.getPaymentsOfEnrollement(10L, connectedUser);

        assertTrue(result.iterator().hasNext());
        assertEquals(paymentDto, result.iterator().next());
    }

    @Test
    void getPaymentsOfEnrollement_ManagerRole_ReturnsPayments() throws Exception {
        mockUser.setRole(Role.MANAGER);
        when(paymentRepository.getPaiementsOfEnrollement(10L))
                .thenReturn(List.of(testPayment));
        when(paymentMapper.returnPaymentDto(testPayment)).thenReturn(paymentDto);

        Iterable<PaymentDto> result = paymentService.getPaymentsOfEnrollement(10L, connectedUser);

        assertTrue(result.iterator().hasNext());
        assertEquals(paymentDto, result.iterator().next());
    }

    @Test
    void getPaymentsOfEnrollement_InvalidRole_ThrowsException() {
        mockUser.setRole(Role.FORMATEUR);

        assertThrows(InvalidRoleInfoException.class,
                () -> paymentService.getPaymentsOfEnrollement(10L, connectedUser));
    }

    @Test
    void getPaymentsOfEnrollement_NoPayments_ThrowsException() {
        mockUser.setRole(Role.STUDENT);
        when(paymentRepository.getPaiementsOfEnrollementOfStudent(10L, 1L))
                .thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,
                () -> paymentService.getPaymentsOfEnrollement(10L, connectedUser));
    }

    // getpaymentById tests
    @Test
    void getpaymentById_ValidId_ReturnsPayment() {
        when(paymentRepository.findById(100L)).thenReturn(Optional.of(testPayment));
        when(paymentMapper.returnPaymentDto(testPayment)).thenReturn(paymentDto);

        PaymentDto result = paymentService.getpaymentById(100L);

        assertEquals(paymentDto, result);
    }

    @Test
    void getpaymentById_InvalidId_ThrowsException() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> paymentService.getpaymentById(999L));
    }

    // getPaymentByStudent_id tests
    @Test
    void getPaymentByStudent_id_ValidId_ReturnsPayments() {
        when(paymentRepository.findByStudentId(1L)).thenReturn(List.of(testPayment));
        when(paymentMapper.returnPaymentDto(testPayment)).thenReturn(paymentDto);

        List<PaymentDto> result = paymentService.getPaymentByStudent_id(1L);

        assertEquals(1, result.size());
        assertEquals(paymentDto, result.get(0));
    }

    @Test
    void getPaymentByStudent_id_NoPayments_ThrowsException() {
        when(paymentRepository.findByStudentId(1L)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class,
                () -> paymentService.getPaymentByStudent_id(1L));
    }

    // createPayment tests
    @Test
    void createPayment_ValidRequest_ReturnsPaymentDto() {
        when(enrollementDAO.findById(10L)).thenReturn(Optional.of(testEnrollement));
        when(paymentMapper.returnPayment(createPaymentDto, testEnrollement))
                .thenReturn(testPayment);
        when(paymentRepository.save(testPayment)).thenReturn(testPayment);
        when(paymentMapper.returnPaymentDto(testPayment)).thenReturn(paymentDto);

        PaymentDto result = paymentService.createPayment(createPaymentDto);

        assertNotNull(result);
        verify(paymentRepository).save(testPayment);
    }

    // updatePayment tests
    @Test
    void updatePayment_ValidRequest_ReturnsUpdatedPayment() {
        when(paymentRepository.findById(testPayment.getPayment_id())).thenReturn(Optional.of(testPayment));
        when(enrollementDAO.findById(testEnrollementDto.getEnrollementId())).thenReturn(Optional.of(testEnrollement));

        //  when(enrollementDAO.findById(10L)).thenReturn(Optional.of(testEnrollement));
        when(paymentMapper.returnPayment(createPaymentDto, testEnrollement))
                .thenReturn(testPayment);
        when(paymentRepository.save(testPayment)).thenReturn(testPayment);

        when(paymentMapper.returnPaymentDto(testPayment)).thenReturn(paymentDto);

        PaymentDto result = paymentService.updatePayment(createPaymentDto);

        assertNotNull(result);
        verify(paymentRepository).save(testPayment);
    }

    @Test
    void updatePayment_InvalidId_ThrowsException() {
        when(paymentRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> paymentService.updatePayment(createPaymentDto));
    }

    // deletePayment tests
    @Test
    void deletePayment_ValidId_DeletesPayment() {
        when(paymentRepository.findById(100L)).thenReturn(Optional.of(testPayment));

        paymentService.deletePayment(100L);

        verify(paymentRepository).deleteById(100L);
    }

    @Test
    void deletePayment_InvalidId_ThrowsException() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> paymentService.deletePayment(999L));
    }
}