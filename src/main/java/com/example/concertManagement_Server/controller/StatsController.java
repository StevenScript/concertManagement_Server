/**
 * StatsController.java
 *
 * Exposes read-only statistics endpoints for dynamic or public-facing views.
 * Currently includes:
 * - Landing page stats (used for animated homepage elements)
 *
 * Works closely with:
 * - StatsService.java (handles the actual data aggregation logic)
 * - StatsResponse.java (DTO for delivering grouped metrics)
 */
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
     * Returns a limited set of stat metrics to be displayed on the animated landing page.
     * Applies a defensive clamp: the `limit` parameter is forced to stay between 1 and 20.
     *
     * Example: {@code /stats/landing?limit=8}
     *
     * @param limit number of records to return (default 8, max 20)
     * @return grouped landing page statistics
     */
    @GetMapping("/landing")
    public StatsResponse landing(@RequestParam(defaultValue = "8") int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 20));
        return statsService.getLandingStats(safeLimit);
    }
}
