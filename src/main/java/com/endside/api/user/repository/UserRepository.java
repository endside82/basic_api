package com.endside.api.user.repository;

import com.endside.api.user.constants.UserStatus;
import com.endside.api.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByUserIdAndStatusLessThanEqual(long receiver, UserStatus status);
}
