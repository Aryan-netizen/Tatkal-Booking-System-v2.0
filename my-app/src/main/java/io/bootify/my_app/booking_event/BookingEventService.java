package io.bootify.my_app.booking_event;

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
public class BookingEventService {

    private final BookingEventRepository bookingEventRepository;
    private final BookingRepository bookingRepository;

    public BookingEventService(final BookingEventRepository bookingEventRepository,
            final BookingRepository bookingRepository) {
        this.bookingEventRepository = bookingEventRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<BookingEventDTO> findAll() {
        final List<BookingEvent> bookingEvents = bookingEventRepository.findAll(Sort.by("id"));
        return bookingEvents.stream()
                .map(bookingEvent -> mapToDTO(bookingEvent, new BookingEventDTO()))
                .toList();
    }

    public BookingEventDTO get(final String id) {
        return bookingEventRepository.findById(id)
                .map(bookingEvent -> mapToDTO(bookingEvent, new BookingEventDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final BookingEventDTO bookingEventDTO) {
        final BookingEvent bookingEvent = new BookingEvent();
        mapToEntity(bookingEventDTO, bookingEvent);
        bookingEvent.setId(bookingEventDTO.getId());
        return bookingEventRepository.save(bookingEvent).getId();
    }

    public void update(final String id, final BookingEventDTO bookingEventDTO) {
        final BookingEvent bookingEvent = bookingEventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingEventDTO, bookingEvent);
        bookingEventRepository.save(bookingEvent);
    }

    public void delete(final String id) {
        final BookingEvent bookingEvent = bookingEventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        bookingEventRepository.delete(bookingEvent);
    }

    private BookingEventDTO mapToDTO(final BookingEvent bookingEvent,
            final BookingEventDTO bookingEventDTO) {
        bookingEventDTO.setId(bookingEvent.getId());
        bookingEventDTO.setFromStatus(bookingEvent.getFromStatus());
        bookingEventDTO.setToStatus(bookingEvent.getToStatus());
        bookingEventDTO.setNote(bookingEvent.getNote());
        bookingEventDTO.setAt(bookingEvent.getAt());
        bookingEventDTO.setBooking(bookingEvent.getBooking() == null ? null : bookingEvent.getBooking().getId());
        return bookingEventDTO;
    }

    private BookingEvent mapToEntity(final BookingEventDTO bookingEventDTO,
            final BookingEvent bookingEvent) {
        bookingEvent.setFromStatus(bookingEventDTO.getFromStatus());
        bookingEvent.setToStatus(bookingEventDTO.getToStatus());
        bookingEvent.setNote(bookingEventDTO.getNote());
        bookingEvent.setAt(bookingEventDTO.getAt());
        final Booking booking = bookingEventDTO.getBooking() == null ? null : bookingRepository.findById(bookingEventDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        bookingEvent.setBooking(booking);
        return bookingEvent;
    }

    public boolean idExists(final String id) {
        return bookingEventRepository.existsByIdIgnoreCase(id);
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final BookingEvent bookingBookingEvent = bookingEventRepository.findFirstByBookingId(event.getId());
        if (bookingBookingEvent != null) {
            referencedException.setKey("booking.bookingEvent.booking.referenced");
            referencedException.addParam(bookingBookingEvent.getId());
            throw referencedException;
        }
    }

}
