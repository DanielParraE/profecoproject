package com.profeco.consumer.repositories;

import com.profeco.consumer.entities.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
    public List<Consumer> findAll();
    public Consumer findByRfc(String rfc);
}
