package core.nxg.serviceImpl;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;
import core.nxg.repository.TechTalentAgentRepository;
import core.nxg.service.TechTalentAgentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TechTalentAgentServiceImpl implements TechTalentAgentService {

    private final TechTalentAgentRepository techTalentAgentRepository;

    @Override
    public TechTalentAgent createAgents(TechTalentAgentDto agentDto) {
        TechTalentAgent techTalentAgent = new TechTalentAgent();
        techTalentAgent.setAgentID(agentDto.getAgentID());
        //Userid does not currently exist in the user class as at the time of pushing this code.
        //techTalentAgent.setUserID(agentDto.getUserID());
        techTalentAgent.setGender(agentDto.getGender());
        techTalentAgent.setJobType(agentDto.getJobType());
        techTalentAgent.setIndustryType(agentDto.getIndustryType());
        techTalentAgent.setAddress(agentDto.getAddress());
        techTalentAgent.setZipCode(agentDto.getZipCode());
        return techTalentAgentRepository.save(techTalentAgent);
    }

}



