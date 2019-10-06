package com.insurance.repository;

import com.insurance.domain.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Insurance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    @Query("select insurance from Insurance insurance where insurance.user.login = ?#{principal.username}")
    List<Insurance> findByUserIsCurrentUser();

    List<Insurance> findByUserId(long userId);
}
