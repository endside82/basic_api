package com.endside.api.stuff.repository;

import com.endside.api.stuff.constants.StuffStatus;
import com.endside.api.stuff.modle.Stuff;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@Transactional
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StuffRepositoryTest {

    @Autowired
    private StuffRepository stuffRepository;

    @Test
    void save_stuff_then_stuff_saved(){
        Stuff stuff = stuffRepository.save(Stuff.builder()
                .name("name")
                .status(StuffStatus.STANDBY)
                .description("blah blah blah")
                .makeDatetime(LocalDateTime.now()).build());
        stuffRepository.save(stuff);
    }

}