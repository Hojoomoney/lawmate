package site.lawmate.manage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.lawmate.manage.domain.dto.UserStatsDto;
import site.lawmate.manage.service.ManageService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user/stats")
public class ManageController {

    private final ManageService manageService;


    @Scheduled(cron = "0 0 0 * * ?")
    @GetMapping("/save")
    public void saveUserStats() {
        log.info("saveUserStats");
        manageService.saveUserStats();
    }

    @GetMapping("/date")
    public ResponseEntity<List<UserStatsDto>> findByDate() {
        log.info("findAll");
        return ResponseEntity.ok(manageService.findByDate());
    }

    @GetMapping("/month")
    public ResponseEntity<List<UserStatsDto>> findByMonth() {
        log.info("findByMonth");
        return ResponseEntity.ok(manageService.findByMonth());
    }

    @GetMapping("/year")
    public ResponseEntity<List<UserStatsDto>> findByYear() {
        log.info("findByYear");
        return ResponseEntity.ok(manageService.findByYear());
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String,Long>> getUserTotalStats() {
        log.info("getUserTotalStats");
        return ResponseEntity.ok(manageService.getUserTotalStats());
    }

}
