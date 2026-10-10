package io.bootify.my_app.train;

import io.bootify.my_app.events.BeforeDeleteTrain;
import io.bootify.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TrainService {

    private final TrainRepository trainRepository;
    private final ApplicationEventPublisher publisher;

    public TrainService(final TrainRepository trainRepository,
            final ApplicationEventPublisher publisher) {
        this.trainRepository = trainRepository;
        this.publisher = publisher;
    }

    public List<TrainDTO> findAll() {
        final List<Train> trains = trainRepository.findAll(Sort.by("number"));
        return trains.stream()
                .map(train -> mapToDTO(train, new TrainDTO()))
                .toList();
    }

    public TrainDTO get(final String number) {
        return trainRepository.findById(number)
                .map(train -> mapToDTO(train, new TrainDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final TrainDTO trainDTO) {
        final Train train = new Train();
        mapToEntity(trainDTO, train);
        train.setNumber(trainDTO.getNumber());
        return trainRepository.save(train).getNumber();
    }

    public void update(final String number, final TrainDTO trainDTO) {
        final Train train = trainRepository.findById(number)
                .orElseThrow(NotFoundException::new);
        mapToEntity(trainDTO, train);
        trainRepository.save(train);
    }

    public void delete(final String number) {
        final Train train = trainRepository.findById(number)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTrain(number));
        trainRepository.delete(train);
    }

    private TrainDTO mapToDTO(final Train train, final TrainDTO trainDTO) {
        trainDTO.setNumber(train.getNumber());
        trainDTO.setName(train.getName());
        return trainDTO;
    }

    private Train mapToEntity(final TrainDTO trainDTO, final Train train) {
        train.setName(trainDTO.getName());
        return train;
    }

    public boolean numberExists(final String number) {
        return trainRepository.existsByNumberIgnoreCase(number);
    }

}
