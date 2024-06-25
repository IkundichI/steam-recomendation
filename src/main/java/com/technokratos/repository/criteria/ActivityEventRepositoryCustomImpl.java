package com.technokratos.repository.criteria;

import com.technokratos.entity.ActivityEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityEventRepositoryCustomImpl implements ActivityEventRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ActivityEntity> findAllSortedByTimestampDesc() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityEntity> cq = cb.createQuery(ActivityEntity.class);
        Root<ActivityEntity> activityEventRoot = cq.from(ActivityEntity.class);
        cq.select(activityEventRoot).orderBy(cb.desc(activityEventRoot.get("timestamp")));

        return entityManager.createQuery(cq).getResultList();
    }
}
