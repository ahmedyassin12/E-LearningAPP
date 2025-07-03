package com.example.demo.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.management.relation.InvalidRoleInfoException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//manager
public class PaymentService {


    @Autowired
    private PaymentDAO paymentRepository;

    @Autowired
    private EnrollementService enrollementService ;

    private PaymentMapper paymentMapper =new PaymentMapper() ;
    @Autowired
    private EnrollementDAO enrollementDAO;

    @Autowired
    private ObjectValidator<CreatePaymentDto> paymentValidator ;


    public List<PaymentDto> getAllPayments() {


        Iterable<Payment>payments= paymentRepository.findAll() ;

        if(payments.iterator().hasNext()){
            List<PaymentDto>paymentDtos=new ArrayList<>();

            payments.forEach(payment ->
                    paymentDtos.add(
                            paymentMapper.returnPaymentDto(payment)
                    ) );


            return paymentDtos ;

        }


throw new EntityNotFoundException("No payment found") ;



    }


    //student manager
    public Iterable<PaymentDto> getPaymentsOfEnrollement
    (Long enrollementId , Principal connectedUser)
            throws InvalidRoleInfoException
    {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser ).getPrincipal();

        Iterable<Payment>payments ;


            if(user.getRole().equals(Role.STUDENT)){

                payments= paymentRepository.getPaiementsOfEnrollementOfStudent(enrollementId,user.getId());

            } else if (user.getRole().equals(Role.MANAGER)) {

                payments= paymentRepository.getPaiementsOfEnrollement(enrollementId);



            }

            else {

                throw new InvalidRoleInfoException("Invalid role");


            }

        List<PaymentDto>paymentDtos=new ArrayList<>();

        if (payments.iterator().hasNext()){
            payments.forEach(payment ->
                    paymentDtos.add(
                            paymentMapper.returnPaymentDto(payment)
                    ) );


            return paymentDtos ;

        }

        throw new EntityNotFoundException("payment not found");


    }








    //manager
    public PaymentDto getpaymentById(long id ){
        Optional<Payment> optional= paymentRepository.findById(id) ;

        PaymentDto paymentDto ;
        if(optional.isPresent()){
            paymentDto=paymentMapper.returnPaymentDto(optional.get()) ;
            return paymentDto ;

        }


        throw new RuntimeException("payment not found for id  ::  "+id  )  ;


    }











    //manager
    public List<PaymentDto> getPaymentByStudent_id(long student_Id) {

        List<Payment> payments = paymentRepository.findByStudentId(student_Id);


        if (payments.isEmpty()) {
            throw new RuntimeException("payments not found for student_id :: " + student_Id);
        }

        List<PaymentDto> paymentDtos = new ArrayList<>();
        payments.forEach(payment ->
                paymentDtos.add(
                        paymentMapper.returnPaymentDto(payment)
                ) );

        return paymentDtos;


    }












    //manager
    public PaymentDto createPayment(CreatePaymentDto createPaymentDto ) {



        paymentValidator.validate(createPaymentDto);

        Enrollement enrollement=enrollementDAO.findById(createPaymentDto.getEnrollement_id())
                .orElseThrow(()->new EntityNotFoundException("No enrollement found to assert into payment"));

        Payment payment = paymentMapper.returnPayment(createPaymentDto,enrollement) ;

         paymentRepository.save(payment);

         return paymentMapper.returnPaymentDto(payment);



    }










    public PaymentDto updatePayment(CreatePaymentDto updatePaymentDto) {

        paymentValidator.validate(updatePaymentDto);

        Optional<Payment> payment = paymentRepository.findById(updatePaymentDto.getPaymentDto().getPayment_id()) ;

        if(payment.isPresent()){

          Enrollement enrollement=  enrollementDAO.findById(updatePaymentDto.getEnrollement_id())
                  .orElseThrow(()->new EntityNotFoundException("No enrollement found to assert into payment"));
            ;
            Payment updatedPayment=paymentMapper.returnPayment(updatePaymentDto,enrollement);

            paymentRepository.save(updatedPayment) ;
            return paymentMapper.returnPaymentDto(updatedPayment);

        }

        throw new EntityNotFoundException("payment not found for update ");

    }

    public String deletePayment(long payment_id) {

        Payment payment=paymentRepository.findById(payment_id).orElseThrow(()->new EntityNotFoundException(
                "payment not found for delete"
        ));
        paymentRepository.deleteById(payment_id);

        return "payment deleted successfully";
    }







}
