package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.StatsResponse;
import com.example.concertManagement_Server.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * Endpoint consumed by the new animated landing page.
     *
     * Example: <code>/stats/landing?limit=8</code>
     */
    @GetMapping("/landing")
    public StatsResponse landing(@RequestParam(defaultValue = "8") int limit) {
        // defensive clamp: keep between 1 and 20
        int safeLimit = Math.max(1, Math.min(limit, 20));
        return statsService.getLandingStats(safeLimit);
    }
}