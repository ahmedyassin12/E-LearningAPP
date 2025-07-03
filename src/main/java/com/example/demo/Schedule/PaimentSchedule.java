

/*

Basicly this used to track when paiment date is expired ( if student pass 30 dayspayment_status will
  change to unpaid ) ;
 */
package com.example.demo.Schedule;
import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.PaymentDAO;
import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.service.EnrollementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class PaimentSchedule {
    @Autowired
    private EnrollementService enrollementService ;
    @Autowired
    private PaymentDAO paymentDAO;

    private LocalDate lastPayment_date;
    @Scheduled(cron = "10 * * * * *")
    public void update_Payment(){

        Iterable<EnrollementDto>  enrollementDtos= enrollementService.getAllEnrollements() ;

        if( ! enrollementDtos.iterator().hasNext()){
            throw new RuntimeException("No enrollement found ");

        }
        for (EnrollementDto enrollement: enrollementDtos) {


            lastPayment_date = paymentDAO.findLastPaymentDateByEnrollement(enrollement.getEnrollementId());


             if(lastPayment_date !=null) {

                 System.out.println("paimentdate = "+ lastPayment_date);

                 Date expired_Payment_date =createExpiredDate(lastPayment_date);

                 if (expired_Payment_date.before( new Date() ) ){
                     enrollement.setPaymentStatus(PaymentStatus.UnPaid);

                 }
                 else  {

                 enrollement.setPaymentStatus(PaymentStatus.Paid);

                 }
                 System.out.println(enrollement.getPaymentStatus());
                 enrollementService.updateEnrollement(enrollement);
             }

        }




    }

    private Date createExpiredDate(LocalDate lastPaimentDate) {

        LocalDate TransformToExpiredDate=lastPaimentDate ;

        TransformToExpiredDate=TransformToExpiredDate.plusDays(30) ;

        return Date.from(TransformToExpiredDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) ;




    }


}
