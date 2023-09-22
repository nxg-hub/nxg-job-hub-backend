package core.nxg.service;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;

public interface TechTalentAgentService {
    TechTalentAgent createAgents(TechTalentAgentDto agentDto);
}
