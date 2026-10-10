package io.bootify.my_app.quota;

import org.springframework.data.jpa.repository.JpaRepository;


public interface QuotaRepository extends JpaRepository<Quota, String> {

    boolean existsByCodeIgnoreCase(String code);

}
