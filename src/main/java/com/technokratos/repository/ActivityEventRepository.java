package com.technokratos.repository;

import com.technokratos.entity.ActivityEntity;
import com.technokratos.repository.criteria.ActivityEventRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityEventRepository extends JpaRepository<ActivityEntity, Long>, ActivityEventRepositoryCustom {

}
