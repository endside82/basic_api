package com.endside.api.config.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends CrudRepository<LoginAttempt,String > {

}
