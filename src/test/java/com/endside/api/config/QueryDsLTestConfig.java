package com.endside.api.config;

import com.endside.api.stuff.repository.StuffQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class QueryDsLTestConfig {
    @PersistenceContext
    EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public StuffQueryRepository stuffQueryRepository() {
        return new StuffQueryRepository(jpaQueryFactory());
    }
}
