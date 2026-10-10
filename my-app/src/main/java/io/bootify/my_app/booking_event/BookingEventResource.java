package io.bootify.my_app.booking_event;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/bookingEvents", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingEventResource {

    private final BookingEventService bookingEventService;

    public BookingEventResource(final BookingEventService bookingEventService) {
        this.bookingEventService = bookingEventService;
    }

    @GetMapping
    public ResponseEntity<List<BookingEventDTO>> getAllBookingEvents() {
        return ResponseEntity.ok(bookingEventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingEventDTO> getBookingEvent(
            @PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(bookingEventService.get(id));
    }

    @PostMapping
    public ResponseEntity<String> createBookingEvent(
            @RequestBody @Valid final BookingEventDTO bookingEventDTO) {
        final String createdId = bookingEventService.create(bookingEventDTO);
        return new ResponseEntity<>('"' + createdId + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBookingEvent(@PathVariable(name = "id") final String id,
            @RequestBody @Valid final BookingEventDTO bookingEventDTO) {
        bookingEventService.update(id, bookingEventDTO);
        return ResponseEntity.ok('"' + id + '"');
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingEvent(@PathVariable(name = "id") final String id) {
        bookingEventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
