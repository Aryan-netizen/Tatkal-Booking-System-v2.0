package io.bootify.my_app.travel_class;

import io.bootify.my_app.events.BeforeDeleteTravelClass;
import io.bootify.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TravelClassService {

    private final TravelClassRepository travelClassRepository;
    private final ApplicationEventPublisher publisher;

    public TravelClassService(final TravelClassRepository travelClassRepository,
            final ApplicationEventPublisher publisher) {
        this.travelClassRepository = travelClassRepository;
        this.publisher = publisher;
    }

    public List<TravelClassDTO> findAll() {
        final List<TravelClass> travelClasses = travelClassRepository.findAll(Sort.by("code"));
        return travelClasses.stream()
                .map(travelClass -> mapToDTO(travelClass, new TravelClassDTO()))
                .toList();
    }

    public TravelClassDTO get(final String code) {
        return travelClassRepository.findById(code)
                .map(travelClass -> mapToDTO(travelClass, new TravelClassDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final TravelClassDTO travelClassDTO) {
        final TravelClass travelClass = new TravelClass();
        mapToEntity(travelClassDTO, travelClass);
        travelClass.setCode(travelClassDTO.getCode());
        return travelClassRepository.save(travelClass).getCode();
    }

    public void update(final String code, final TravelClassDTO travelClassDTO) {
        final TravelClass travelClass = travelClassRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        mapToEntity(travelClassDTO, travelClass);
        travelClassRepository.save(travelClass);
    }

    public void delete(final String code) {
        final TravelClass travelClass = travelClassRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTravelClass(code));
        travelClassRepository.delete(travelClass);
    }

    private TravelClassDTO mapToDTO(final TravelClass travelClass,
            final TravelClassDTO travelClassDTO) {
        travelClassDTO.setCode(travelClass.getCode());
        travelClassDTO.setName(travelClass.getName());
        return travelClassDTO;
    }

    private TravelClass mapToEntity(final TravelClassDTO travelClassDTO,
            final TravelClass travelClass) {
        travelClass.setName(travelClassDTO.getName());
        return travelClass;
    }

    public boolean codeExists(final String code) {
        return travelClassRepository.existsByCodeIgnoreCase(code);
    }

}
