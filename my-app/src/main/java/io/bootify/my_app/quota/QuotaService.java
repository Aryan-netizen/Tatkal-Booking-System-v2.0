package io.bootify.my_app.quota;

import io.bootify.my_app.events.BeforeDeleteQuota;
import io.bootify.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class QuotaService {

    private final QuotaRepository quotaRepository;
    private final ApplicationEventPublisher publisher;

    public QuotaService(final QuotaRepository quotaRepository,
            final ApplicationEventPublisher publisher) {
        this.quotaRepository = quotaRepository;
        this.publisher = publisher;
    }

    public List<QuotaDTO> findAll() {
        final List<Quota> quotas = quotaRepository.findAll(Sort.by("code"));
        return quotas.stream()
                .map(quota -> mapToDTO(quota, new QuotaDTO()))
                .toList();
    }

    public QuotaDTO get(final String code) {
        return quotaRepository.findById(code)
                .map(quota -> mapToDTO(quota, new QuotaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final QuotaDTO quotaDTO) {
        final Quota quota = new Quota();
        mapToEntity(quotaDTO, quota);
        quota.setCode(quotaDTO.getCode());
        return quotaRepository.save(quota).getCode();
    }

    public void update(final String code, final QuotaDTO quotaDTO) {
        final Quota quota = quotaRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        mapToEntity(quotaDTO, quota);
        quotaRepository.save(quota);
    }

    public void delete(final String code) {
        final Quota quota = quotaRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteQuota(code));
        quotaRepository.delete(quota);
    }

    private QuotaDTO mapToDTO(final Quota quota, final QuotaDTO quotaDTO) {
        quotaDTO.setCode(quota.getCode());
        quotaDTO.setName(quota.getName());
        return quotaDTO;
    }

    private Quota mapToEntity(final QuotaDTO quotaDTO, final Quota quota) {
        quota.setName(quotaDTO.getName());
        return quota;
    }

    public boolean codeExists(final String code) {
        return quotaRepository.existsByCodeIgnoreCase(code);
    }

}
