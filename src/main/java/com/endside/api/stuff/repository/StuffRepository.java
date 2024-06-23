package com.endside.api.stuff.repository;

import com.endside.api.stuff.modle.Stuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StuffRepository extends JpaRepository<Stuff,Long> {
}
