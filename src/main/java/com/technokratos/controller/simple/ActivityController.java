package com.technokratos.controller.simple;

import com.technokratos.entity.ActivityEntity;
import com.technokratos.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Activity API", description = "API для работы с лентой активности")
public class ActivityController {

    private final ActivityService activityService;

    @Operation(summary = "Получить страницу ленты активности")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лента активности успешно получена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/activity")
    public String getActivityFeed(Model model) {
        List<ActivityEntity> activityEntities = activityService.getAllActivityEvents();
        model.addAttribute("activityEvents", activityEntities);
        return "activity";
    }
}
