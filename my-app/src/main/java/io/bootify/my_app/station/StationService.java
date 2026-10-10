package io.bootify.my_app.station;

import io.bootify.my_app.events.BeforeDeleteStation;
import io.bootify.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StationService {

    private final StationRepository stationRepository;
    private final ApplicationEventPublisher publisher;

    public StationService(final StationRepository stationRepository,
            final ApplicationEventPublisher publisher) {
        this.stationRepository = stationRepository;
        this.publisher = publisher;
    }

    public List<StationDTO> findAll() {
        final List<Station> stations = stationRepository.findAll(Sort.by("code"));
        return stations.stream()
                .map(station -> mapToDTO(station, new StationDTO()))
                .toList();
    }

    public StationDTO get(final String code) {
        return stationRepository.findById(code)
                .map(station -> mapToDTO(station, new StationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final StationDTO stationDTO) {
        final Station station = new Station();
        mapToEntity(stationDTO, station);
        station.setCode(stationDTO.getCode());
        return stationRepository.save(station).getCode();
    }

    public void update(final String code, final StationDTO stationDTO) {
        final Station station = stationRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        mapToEntity(stationDTO, station);
        stationRepository.save(station);
    }

    public void delete(final String code) {
        final Station station = stationRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteStation(code));
        stationRepository.delete(station);
    }

    private StationDTO mapToDTO(final Station station, final StationDTO stationDTO) {
        stationDTO.setCode(station.getCode());
        stationDTO.setName(station.getName());
        return stationDTO;
    }

    private Station mapToEntity(final StationDTO stationDTO, final Station station) {
        station.setName(stationDTO.getName());
        return station;
    }

    public boolean codeExists(final String code) {
        return stationRepository.existsByCodeIgnoreCase(code);
    }

}
