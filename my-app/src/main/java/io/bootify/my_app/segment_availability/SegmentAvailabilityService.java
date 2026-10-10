package io.bootify.my_app.segment_availability;

import io.bootify.my_app.events.BeforeDeleteQuota;
import io.bootify.my_app.events.BeforeDeleteTripClass;
import io.bootify.my_app.quota.Quota;
import io.bootify.my_app.quota.QuotaRepository;
import io.bootify.my_app.trip_class.TripClass;
import io.bootify.my_app.trip_class.TripClassRepository;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SegmentAvailabilityService {

    private final SegmentAvailabilityRepository segmentAvailabilityRepository;
    private final TripClassRepository tripClassRepository;
    private final QuotaRepository quotaRepository;

    public SegmentAvailabilityService(
            final SegmentAvailabilityRepository segmentAvailabilityRepository,
            final TripClassRepository tripClassRepository, final QuotaRepository quotaRepository) {
        this.segmentAvailabilityRepository = segmentAvailabilityRepository;
        this.tripClassRepository = tripClassRepository;
        this.quotaRepository = quotaRepository;
    }

    public List<SegmentAvailabilityDTO> findAll() {
        final List<SegmentAvailability> segmentAvailabilities = segmentAvailabilityRepository.findAll(Sort.by("classCode"));
        return segmentAvailabilities.stream()
                .map(segmentAvailability -> mapToDTO(segmentAvailability, new SegmentAvailabilityDTO()))
                .toList();
    }

    public SegmentAvailabilityDTO get(final String classCode) {
        return segmentAvailabilityRepository.findById(classCode)
                .map(segmentAvailability -> mapToDTO(segmentAvailability, new SegmentAvailabilityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final SegmentAvailabilityDTO segmentAvailabilityDTO) {
        final SegmentAvailability segmentAvailability = new SegmentAvailability();
        mapToEntity(segmentAvailabilityDTO, segmentAvailability);
        segmentAvailability.setClassCode(segmentAvailabilityDTO.getClassCode());
        return segmentAvailabilityRepository.save(segmentAvailability).getClassCode();
    }

    public void update(final String classCode,
            final SegmentAvailabilityDTO segmentAvailabilityDTO) {
        final SegmentAvailability segmentAvailability = segmentAvailabilityRepository.findById(classCode)
                .orElseThrow(NotFoundException::new);
        mapToEntity(segmentAvailabilityDTO, segmentAvailability);
        segmentAvailabilityRepository.save(segmentAvailability);
    }

    public void delete(final String classCode) {
        final SegmentAvailability segmentAvailability = segmentAvailabilityRepository.findById(classCode)
                .orElseThrow(NotFoundException::new);
        segmentAvailabilityRepository.delete(segmentAvailability);
    }

    private SegmentAvailabilityDTO mapToDTO(final SegmentAvailability segmentAvailability,
            final SegmentAvailabilityDTO segmentAvailabilityDTO) {
        segmentAvailabilityDTO.setClassCode(segmentAvailability.getClassCode());
        segmentAvailabilityDTO.setSegmentIdx(segmentAvailability.getSegmentIdx());
        segmentAvailabilityDTO.setSeatsLeft(segmentAvailability.getSeatsLeft());
        segmentAvailabilityDTO.setTrip(segmentAvailability.getTrip() == null ? null : segmentAvailability.getTrip().getId());
        segmentAvailabilityDTO.setQuota(segmentAvailability.getQuota() == null ? null : segmentAvailability.getQuota().getCode());
        return segmentAvailabilityDTO;
    }

    private SegmentAvailability mapToEntity(final SegmentAvailabilityDTO segmentAvailabilityDTO,
            final SegmentAvailability segmentAvailability) {
        segmentAvailability.setSegmentIdx(segmentAvailabilityDTO.getSegmentIdx());
        segmentAvailability.setSeatsLeft(segmentAvailabilityDTO.getSeatsLeft());
        final TripClass trip = segmentAvailabilityDTO.getTrip() == null ? null : tripClassRepository.findById(segmentAvailabilityDTO.getTrip())
                .orElseThrow(() -> new NotFoundException("trip not found"));
        segmentAvailability.setTrip(trip);
        final Quota quota = segmentAvailabilityDTO.getQuota() == null ? null : quotaRepository.findById(segmentAvailabilityDTO.getQuota())
                .orElseThrow(() -> new NotFoundException("quota not found"));
        segmentAvailability.setQuota(quota);
        return segmentAvailability;
    }

    public boolean classCodeExists(final String classCode) {
        return segmentAvailabilityRepository.existsByClassCodeIgnoreCase(classCode);
    }

    @EventListener(BeforeDeleteTripClass.class)
    public void on(final BeforeDeleteTripClass event) {
        final ReferencedException referencedException = new ReferencedException();
        final SegmentAvailability tripSegmentAvailability = segmentAvailabilityRepository.findFirstByTripId(event.getId());
        if (tripSegmentAvailability != null) {
            referencedException.setKey("tripClass.segmentAvailability.trip.referenced");
            referencedException.addParam(tripSegmentAvailability.getClassCode());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteQuota.class)
    public void on(final BeforeDeleteQuota event) {
        final ReferencedException referencedException = new ReferencedException();
        final SegmentAvailability quotaSegmentAvailability = segmentAvailabilityRepository.findFirstByQuotaCode(event.getCode());
        if (quotaSegmentAvailability != null) {
            referencedException.setKey("quota.segmentAvailability.quota.referenced");
            referencedException.addParam(quotaSegmentAvailability.getClassCode());
            throw referencedException;
        }
    }

}
