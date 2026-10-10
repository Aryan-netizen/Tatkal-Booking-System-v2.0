package io.bootify.my_app.coach;

import io.bootify.my_app.events.BeforeDeleteCoach;
import io.bootify.my_app.events.BeforeDeleteTrain;
import io.bootify.my_app.events.BeforeDeleteTravelClass;
import io.bootify.my_app.train.Train;
import io.bootify.my_app.train.TrainRepository;
import io.bootify.my_app.travel_class.TravelClass;
import io.bootify.my_app.travel_class.TravelClassRepository;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CoachService {

    private final CoachRepository coachRepository;
    private final TrainRepository trainRepository;
    private final TravelClassRepository travelClassRepository;
    private final ApplicationEventPublisher publisher;

    public CoachService(final CoachRepository coachRepository,
            final TrainRepository trainRepository,
            final TravelClassRepository travelClassRepository,
            final ApplicationEventPublisher publisher) {
        this.coachRepository = coachRepository;
        this.trainRepository = trainRepository;
        this.travelClassRepository = travelClassRepository;
        this.publisher = publisher;
    }

    public List<CoachDTO> findAll() {
        final List<Coach> coaches = coachRepository.findAll(Sort.by("id"));
        return coaches.stream()
                .map(coach -> mapToDTO(coach, new CoachDTO()))
                .toList();
    }

    public CoachDTO get(final String id) {
        return coachRepository.findById(id)
                .map(coach -> mapToDTO(coach, new CoachDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final CoachDTO coachDTO) {
        final Coach coach = new Coach();
        mapToEntity(coachDTO, coach);
        coach.setId(coachDTO.getId());
        return coachRepository.save(coach).getId();
    }

    public void update(final String id, final CoachDTO coachDTO) {
        final Coach coach = coachRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(coachDTO, coach);
        coachRepository.save(coach);
    }

    public void delete(final String id) {
        final Coach coach = coachRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteCoach(id));
        coachRepository.delete(coach);
    }

    private CoachDTO mapToDTO(final Coach coach, final CoachDTO coachDTO) {
        coachDTO.setId(coach.getId());
        coachDTO.setCode(coach.getCode());
        coachDTO.setTrainNumber(coach.getTrainNumber() == null ? null : coach.getTrainNumber().getNumber());
        coachDTO.setClassCode(coach.getClassCode() == null ? null : coach.getClassCode().getCode());
        return coachDTO;
    }

    private Coach mapToEntity(final CoachDTO coachDTO, final Coach coach) {
        coach.setCode(coachDTO.getCode());
        final Train trainNumber = coachDTO.getTrainNumber() == null ? null : trainRepository.findById(coachDTO.getTrainNumber())
                .orElseThrow(() -> new NotFoundException("trainNumber not found"));
        coach.setTrainNumber(trainNumber);
        final TravelClass classCode = coachDTO.getClassCode() == null ? null : travelClassRepository.findById(coachDTO.getClassCode())
                .orElseThrow(() -> new NotFoundException("classCode not found"));
        coach.setClassCode(classCode);
        return coach;
    }

    public boolean idExists(final String id) {
        return coachRepository.existsByIdIgnoreCase(id);
    }

    @EventListener(BeforeDeleteTrain.class)
    public void on(final BeforeDeleteTrain event) {
        final ReferencedException referencedException = new ReferencedException();
        final Coach trainNumberCoach = coachRepository.findFirstByTrainNumberNumber(event.getNumber());
        if (trainNumberCoach != null) {
            referencedException.setKey("train.coach.trainNumber.referenced");
            referencedException.addParam(trainNumberCoach.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTravelClass.class)
    public void on(final BeforeDeleteTravelClass event) {
        final ReferencedException referencedException = new ReferencedException();
        final Coach classCodeCoach = coachRepository.findFirstByClassCodeCode(event.getCode());
        if (classCodeCoach != null) {
            referencedException.setKey("travelClass.coach.classCode.referenced");
            referencedException.addParam(classCodeCoach.getId());
            throw referencedException;
        }
    }

}
