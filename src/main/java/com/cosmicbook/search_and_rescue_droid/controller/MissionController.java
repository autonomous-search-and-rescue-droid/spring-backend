package com.cosmicbook.search_and_rescue_droid.controller;

import com.cosmicbook.search_and_rescue_droid.dto.MissionDTO;
import com.cosmicbook.search_and_rescue_droid.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'COMMON')")
    @GetMapping
    public ResponseEntity<List<MissionDTO>> getAllMissions() {
        try {
            List<MissionDTO> missions = missionService.getAllMissionDTOs();
            return ResponseEntity.ok(missions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addMission(@Valid @RequestBody MissionDTO missionDTO) {
        try {
            missionService.addMissionFromDTO(missionDTO);
            return ResponseEntity.ok("Mission added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Mission details are incomplete or invalid.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/update")
    public ResponseEntity<?> updateMission(@Valid @RequestBody MissionDTO missionDTO) {
        try {

            missionService.addMissionFromDTO(missionDTO);
            return ResponseEntity.ok("Mission updated successfully.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Mission details are incomplete or invalid.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMission(@PathVariable String id) {

        try {

            missionService.deleteMissionById(id);
            return ResponseEntity.ok("Mission deleted successfully.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting mission: " + e.getMessage());
        }

    }
}