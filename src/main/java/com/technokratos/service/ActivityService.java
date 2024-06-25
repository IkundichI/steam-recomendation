package com.technokratos.service;

import com.technokratos.entity.ActivityEntity;
import com.technokratos.repository.ActivityEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityEventRepository activityEventRepository;

    @SneakyThrows
    public List<ActivityEntity> getAllActivityEvents() {
        log.info("Fetching all activity events sorted by timestamp in descending order.");
        List<ActivityEntity> activityEntities = activityEventRepository.findAllSortedByTimestampDesc();
        log.info("Successfully fetched {} activity events.", activityEntities.size());
        return activityEntities;
    }
}
