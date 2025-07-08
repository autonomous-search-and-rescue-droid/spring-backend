package com.cosmicbook.search_and_rescue_droid.service;

import com.cosmicbook.search_and_rescue_droid.dto.MissionDTO;
import com.cosmicbook.search_and_rescue_droid.model.Missions;
import com.cosmicbook.search_and_rescue_droid.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    public List<MissionDTO> getAllMissionDTOs() {
        return missionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void addMissionFromDTO(MissionDTO missionDTO) {
        Missions mission = convertToEntity(missionDTO);
        if (mission != null && mission.getSpotName() != null && !mission.getSpotName().isEmpty()) {
            missionRepository.save(mission);
        } else {
            throw new IllegalArgumentException("Mission details are incomplete or invalid.");
        }
    }

    public void deleteMissionById(String id) {

        if(id == null || id.isEmpty()){
            throw new IllegalArgumentException("Mission ID cannot be null or empty.");
        }
        try {
            missionRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private MissionDTO convertToDTO(Missions mission) {
        MissionDTO dto = new MissionDTO();
        dto.setId(mission.getId());
        dto.setSpotName(mission.getSpotName());
        dto.setNoOfPersons(mission.getNoOfPersons());
        dto.setContractTeamName(mission.getContractTeamName());
        dto.setHealthStatus(mission.getHealthStatus());
        dto.setTimeOfEvent(mission.getTimeOfEvent());
        dto.setLocation(mission.getLocation());
        return dto;
    }

    private Missions convertToEntity(MissionDTO dto) {
        Missions mission = new Missions();
        mission.setId(dto.getId());
        mission.setSpotName(dto.getSpotName());
        mission.setNoOfPersons(dto.getNoOfPersons());
        mission.setContractTeamName(dto.getContractTeamName());
        mission.setHealthStatus(dto.getHealthStatus());
        mission.setTimeOfEvent(dto.getTimeOfEvent());
        mission.setLocation(dto.getLocation());
        return mission;
    }
}