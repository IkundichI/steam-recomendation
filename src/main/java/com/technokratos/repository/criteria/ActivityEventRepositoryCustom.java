package com.technokratos.repository.criteria;

import com.technokratos.entity.ActivityEntity;

import java.util.List;

public interface ActivityEventRepositoryCustom {
    List<ActivityEntity> findAllSortedByTimestampDesc();
}
