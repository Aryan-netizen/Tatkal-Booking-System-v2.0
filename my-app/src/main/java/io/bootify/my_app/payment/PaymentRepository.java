package io.bootify.my_app.payment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, String> {

    Payment findFirstByBookingId(String id);

    boolean existsByIdIgnoreCase(String id);

    boolean existsByGatewayRefIgnoreCase(String gatewayRef);

}
