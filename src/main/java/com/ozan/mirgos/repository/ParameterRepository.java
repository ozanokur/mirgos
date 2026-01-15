package com.ozan.mirgos.repository;

import com.ozan.mirgos.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, String> {
    Optional<Parameter> findByKey(String key);
}
