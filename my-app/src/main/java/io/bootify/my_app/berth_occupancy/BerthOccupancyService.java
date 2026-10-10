package io.bootify.my_app.berth_occupancy;

import io.bootify.my_app.berth_allocation.BerthAllocation;
import io.bootify.my_app.berth_allocation.BerthAllocationRepository;
import io.bootify.my_app.events.BeforeDeleteBerthAllocation;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BerthOccupancyService {

    private final BerthOccupancyRepository berthOccupancyRepository;
    private final BerthAllocationRepository berthAllocationRepository;

    public BerthOccupancyService(final BerthOccupancyRepository berthOccupancyRepository,
            final BerthAllocationRepository berthAllocationRepository) {
        this.berthOccupancyRepository = berthOccupancyRepository;
        this.berthAllocationRepository = berthAllocationRepository;
    }

    public List<BerthOccupancyDTO> findAll() {
        final List<BerthOccupancy> berthOccupancies = berthOccupancyRepository.findAll(Sort.by("tripId"));
        return berthOccupancies.stream()
                .map(berthOccupancy -> mapToDTO(berthOccupancy, new BerthOccupancyDTO()))
                .toList();
    }

    public BerthOccupancyDTO get(final String tripId) {
        return berthOccupancyRepository.findById(tripId)
                .map(berthOccupancy -> mapToDTO(berthOccupancy, new BerthOccupancyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final BerthOccupancyDTO berthOccupancyDTO) {
        final BerthOccupancy berthOccupancy = new BerthOccupancy();
        mapToEntity(berthOccupancyDTO, berthOccupancy);
        berthOccupancy.setTripId(berthOccupancyDTO.getTripId());
        return berthOccupancyRepository.save(berthOccupancy).getTripId();
    }

    public void update(final String tripId, final BerthOccupancyDTO berthOccupancyDTO) {
        final BerthOccupancy berthOccupancy = berthOccupancyRepository.findById(tripId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(berthOccupancyDTO, berthOccupancy);
        berthOccupancyRepository.save(berthOccupancy);
    }

    public void delete(final String tripId) {
        final BerthOccupancy berthOccupancy = berthOccupancyRepository.findById(tripId)
                .orElseThrow(NotFoundException::new);
        berthOccupancyRepository.delete(berthOccupancy);
    }

    private BerthOccupancyDTO mapToDTO(final BerthOccupancy berthOccupancy,
            final BerthOccupancyDTO berthOccupancyDTO) {
        berthOccupancyDTO.setTripId(berthOccupancy.getTripId());
        berthOccupancyDTO.setBerthId(berthOccupancy.getBerthId());
        berthOccupancyDTO.setSegmentIdx(berthOccupancy.getSegmentIdx());
        berthOccupancyDTO.setAllocation(berthOccupancy.getAllocation() == null ? null : berthOccupancy.getAllocation().getId());
        return berthOccupancyDTO;
    }

    private BerthOccupancy mapToEntity(final BerthOccupancyDTO berthOccupancyDTO,
            final BerthOccupancy berthOccupancy) {
        berthOccupancy.setBerthId(berthOccupancyDTO.getBerthId());
        berthOccupancy.setSegmentIdx(berthOccupancyDTO.getSegmentIdx());
        final BerthAllocation allocation = berthOccupancyDTO.getAllocation() == null ? null : berthAllocationRepository.findById(berthOccupancyDTO.getAllocation())
                .orElseThrow(() -> new NotFoundException("allocation not found"));
        berthOccupancy.setAllocation(allocation);
        return berthOccupancy;
    }

    public boolean tripIdExists(final String tripId) {
        return berthOccupancyRepository.existsByTripIdIgnoreCase(tripId);
    }

    @EventListener(BeforeDeleteBerthAllocation.class)
    public void on(final BeforeDeleteBerthAllocation event) {
        final ReferencedException referencedException = new ReferencedException();
        final BerthOccupancy allocationBerthOccupancy = berthOccupancyRepository.findFirstByAllocationId(event.getId());
        if (allocationBerthOccupancy != null) {
            referencedException.setKey("berthAllocation.berthOccupancy.allocation.referenced");
            referencedException.addParam(allocationBerthOccupancy.getTripId());
            throw referencedException;
        }
    }

}
