package com.endside.api.stuff.repository;

import com.endside.api.config.QueryDsLTestConfig;
import com.endside.api.stuff.constants.StuffSearchDateType;
import com.endside.api.stuff.constants.StuffSearchType;
import com.endside.api.stuff.constants.StuffStatus;
import com.endside.api.stuff.param.StuffListParam;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@Import(QueryDsLTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StuffQueryRepositoryTest {

    @Autowired
    private StuffQueryRepository stuffQueryRepository;

    @Test
    void FIND_STUFF_LIST_BY_CONDITION() {
        StuffListParam stuffListParam = new StuffListParam();
        List<StuffStatus> statuses = new ArrayList<>();
        statuses.add(StuffStatus.ACTIVE);
        statuses.add(StuffStatus.STANDBY);
        stuffListParam.setStatuses(statuses);

        stuffListParam.setSearchType(StuffSearchType.NAME);
        stuffListParam.setSearchValue("name");
        stuffListParam.setSearchDateType(StuffSearchDateType.MAKE_DATETIME);
        stuffListParam.setStartDate(LocalDate.now());
        stuffListParam.setEndDate(LocalDate.now());

        stuffListParam.setPage(1);

        stuffQueryRepository.getStuffListByCondition(stuffListParam,stuffListParam.of());
    }
}