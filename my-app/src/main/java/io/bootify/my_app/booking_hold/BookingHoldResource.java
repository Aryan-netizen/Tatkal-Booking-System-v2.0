package io.bootify.my_app.booking_hold;

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
@RequestMapping(value = "/api/bookingHolds", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingHoldResource {

    private final BookingHoldService bookingHoldService;

    public BookingHoldResource(final BookingHoldService bookingHoldService) {
        this.bookingHoldService = bookingHoldService;
    }

    @GetMapping
    public ResponseEntity<List<BookingHoldDTO>> getAllBookingHolds() {
        return ResponseEntity.ok(bookingHoldService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingHoldDTO> getBookingHold(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingHoldService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createBookingHold(
            @RequestBody @Valid final BookingHoldDTO bookingHoldDTO) {
        final Long createdId = bookingHoldService.create(bookingHoldDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBookingHold(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BookingHoldDTO bookingHoldDTO) {
        bookingHoldService.update(id, bookingHoldDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingHold(@PathVariable(name = "id") final Long id) {
        bookingHoldService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
