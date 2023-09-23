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
        techTalentAgent.setUsername(agentDto.getUsername());
        techTalentAgent.setEmail(agentDto.getEmail());
        techTalentAgent.setPhone(agentDto.getPhone());
        techTalentAgent.setPassword(agentDto.getPassword());
        return techTalentAgentRepository.save(techTalentAgent);
    }

}


