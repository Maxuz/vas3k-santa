package dev.maxuz.vas3ksanta.db;

import dev.maxuz.vas3ksanta.model.CountryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<CountryEntity, Long> {
}
