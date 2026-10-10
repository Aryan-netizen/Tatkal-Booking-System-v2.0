package io.bootify.my_app.quota;

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
@RequestMapping(value = "/api/quotas", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuotaResource {

    private final QuotaService quotaService;

    public QuotaResource(final QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @GetMapping
    public ResponseEntity<List<QuotaDTO>> getAllQuotas() {
        return ResponseEntity.ok(quotaService.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<QuotaDTO> getQuota(@PathVariable(name = "code") final String code) {
        return ResponseEntity.ok(quotaService.get(code));
    }

    @PostMapping
    public ResponseEntity<String> createQuota(@RequestBody @Valid final QuotaDTO quotaDTO) {
        final String createdCode = quotaService.create(quotaDTO);
        return new ResponseEntity<>('"' + createdCode + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<String> updateQuota(@PathVariable(name = "code") final String code,
            @RequestBody @Valid final QuotaDTO quotaDTO) {
        quotaService.update(code, quotaDTO);
        return ResponseEntity.ok('"' + code + '"');
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteQuota(@PathVariable(name = "code") final String code) {
        quotaService.delete(code);
        return ResponseEntity.noContent().build();
    }

}
