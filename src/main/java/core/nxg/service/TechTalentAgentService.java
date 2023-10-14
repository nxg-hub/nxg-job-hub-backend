package core.nxg.service;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;

import java.util.List;

public interface TechTalentAgentService {
    TechTalentAgent createAgents(TechTalentAgentDto agentDto);

    TechTalentAgentDto updateTechTalentAgent(Long agentId, TechTalentAgentDto techTalentAgentDto);

    void deleteTechTalentAgent(Long techTalentAgentId);

    TechTalentAgentDto getTechTalentAgentById(Long techTalentAgentId);

    List<TechTalentAgentDto> getAllTechTalentAgent();
}
