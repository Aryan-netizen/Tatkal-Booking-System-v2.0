package io.bootify.my_app.berth;

import io.bootify.my_app.coach.Coach;
import io.bootify.my_app.coach.CoachRepository;
import io.bootify.my_app.events.BeforeDeleteBerth;
import io.bootify.my_app.events.BeforeDeleteCoach;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BerthService {

    private final BerthRepository berthRepository;
    private final CoachRepository coachRepository;
    private final ApplicationEventPublisher publisher;

    public BerthService(final BerthRepository berthRepository,
            final CoachRepository coachRepository, final ApplicationEventPublisher publisher) {
        this.berthRepository = berthRepository;
        this.coachRepository = coachRepository;
        this.publisher = publisher;
    }

    public List<BerthDTO> findAll() {
        final List<Berth> berths = berthRepository.findAll(Sort.by("id"));
        return berths.stream()
                .map(berth -> mapToDTO(berth, new BerthDTO()))
                .toList();
    }

    public BerthDTO get(final String id) {
        return berthRepository.findById(id)
                .map(berth -> mapToDTO(berth, new BerthDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final BerthDTO berthDTO) {
        final Berth berth = new Berth();
        mapToEntity(berthDTO, berth);
        berth.setId(berthDTO.getId());
        return berthRepository.save(berth).getId();
    }

    public void update(final String id, final BerthDTO berthDTO) {
        final Berth berth = berthRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(berthDTO, berth);
        berthRepository.save(berth);
    }

    public void delete(final String id) {
        final Berth berth = berthRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteBerth(id));
        berthRepository.delete(berth);
    }

    private BerthDTO mapToDTO(final Berth berth, final BerthDTO berthDTO) {
        berthDTO.setId(berth.getId());
        berthDTO.setNumber(berth.getNumber());
        berthDTO.setKind(berth.getKind());
        berthDTO.setCoach(berth.getCoach() == null ? null : berth.getCoach().getId());
        return berthDTO;
    }

    private Berth mapToEntity(final BerthDTO berthDTO, final Berth berth) {
        berth.setNumber(berthDTO.getNumber());
        berth.setKind(berthDTO.getKind());
        final Coach coach = berthDTO.getCoach() == null ? null : coachRepository.findById(berthDTO.getCoach())
                .orElseThrow(() -> new NotFoundException("coach not found"));
        berth.setCoach(coach);
        return berth;
    }

    public boolean idExists(final String id) {
        return berthRepository.existsByIdIgnoreCase(id);
    }

    @EventListener(BeforeDeleteCoach.class)
    public void on(final BeforeDeleteCoach event) {
        final ReferencedException referencedException = new ReferencedException();
        final Berth coachBerth = berthRepository.findFirstByCoachId(event.getId());
        if (coachBerth != null) {
            referencedException.setKey("coach.berth.coach.referenced");
            referencedException.addParam(coachBerth.getId());
            throw referencedException;
        }
    }

}
