package io.bootify.my_app.payment;

import io.bootify.my_app.booking.Booking;
import io.bootify.my_app.booking.BookingRepository;
import io.bootify.my_app.events.BeforeDeleteBooking;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(final PaymentRepository paymentRepository,
            final BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("id"));
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final String id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        payment.setId(paymentDTO.getId());
        return paymentRepository.save(payment).getId();
    }

    public void update(final String id, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final String id) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        paymentRepository.delete(payment);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setId(payment.getId());
        paymentDTO.setGatewayRef(payment.getGatewayRef());
        paymentDTO.setAmountPaise(payment.getAmountPaise());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setCreatedAt(payment.getCreatedAt());
        paymentDTO.setUpdatedAt(payment.getUpdatedAt());
        paymentDTO.setBooking(payment.getBooking() == null ? null : payment.getBooking().getId());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setGatewayRef(paymentDTO.getGatewayRef());
        payment.setAmountPaise(paymentDTO.getAmountPaise());
        payment.setStatus(paymentDTO.getStatus());
        payment.setCreatedAt(paymentDTO.getCreatedAt());
        payment.setUpdatedAt(paymentDTO.getUpdatedAt());
        final Booking booking = paymentDTO.getBooking() == null ? null : bookingRepository.findById(paymentDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        payment.setBooking(booking);
        return payment;
    }

    public boolean idExists(final String id) {
        return paymentRepository.existsByIdIgnoreCase(id);
    }

    public boolean gatewayRefExists(final String gatewayRef) {
        return paymentRepository.existsByGatewayRefIgnoreCase(gatewayRef);
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final Payment bookingPayment = paymentRepository.findFirstByBookingId(event.getId());
        if (bookingPayment != null) {
            referencedException.setKey("booking.payment.booking.referenced");
            referencedException.addParam(bookingPayment.getId());
            throw referencedException;
        }
    }

}
