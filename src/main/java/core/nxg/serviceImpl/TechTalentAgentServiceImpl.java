package core.nxg.serviceImpl;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.TechTalentAgentRepository;
import core.nxg.service.TechTalentAgentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TechTalentAgentServiceImpl implements TechTalentAgentService {

    private final TechTalentAgentRepository techTalentAgentRepository;

//    @Autowired
//    public TechTalentAgentServiceImpl(TechTalentAgentRepository techTalentAgentRepository);

    @Override
    public TechTalentAgent createAgents(TechTalentAgentDto agentDto) {
        TechTalentAgent techTalentAgent = new TechTalentAgent();
        //techTalentAgent.setAgentID(agentDto.getAgentID());
        //Userid does not currently exist in the user class as at the time of pushing this code.
        //techTalentAgent.setUserID(agentDto.getUserID());
        //techTalentAgent.setGender(agentDto.getGender());
        techTalentAgent.setJobType(JobType.valueOf(String.valueOf(agentDto.getJobType())));
        techTalentAgent.setIndustryType(IndustryType.valueOf(String.valueOf(agentDto.getIndustryType())));
        techTalentAgent.setAddress(agentDto.getAddress());
        techTalentAgent.setZipCode(agentDto.getZipCode());
        return techTalentAgentRepository.save(techTalentAgent);
    }

    @Override
    public TechTalentAgentDto updateTechTalentAgent(Long agentId, TechTalentAgentDto techTalentAgentDto) {
        TechTalentAgent techTalentAgent = techTalentAgentRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("TechTalentAgent with ID " + agentId + " not found"));
        techTalentAgent.setIndustryType(techTalentAgentDto.getIndustryType());
        techTalentAgent.setAddress(techTalentAgentDto.getAddress());
        techTalentAgent.setZipCode(techTalentAgentDto.getZipCode());
        //techTalentAgent.setIndustryType(techTalentAgentDto.getIndustryType());
        techTalentAgent = techTalentAgentRepository.save(techTalentAgent);
        return mapToDto(techTalentAgent);
    }



    @Override
    public void deleteTechTalentAgent(Long techTalentAgentId) {
        TechTalentAgent techTalentAgent = techTalentAgentRepository.findById(techTalentAgentId)
                .orElseThrow(() -> new NotFoundException("TechTalentAgent with ID " + techTalentAgentId + " not found"));
        techTalentAgentRepository.delete(techTalentAgent);
    }

    @Override
    public TechTalentAgentDto getTechTalentAgentById(Long techTalentAgentId) {
        TechTalentAgent techTalentAgent = techTalentAgentRepository.findById(techTalentAgentId)
                .orElseThrow(() -> new NotFoundException("TechTalentAgent with ID " + techTalentAgentId + " not found"));
        return mapToDto(techTalentAgent);
    }

    @Override
    public List<TechTalentAgentDto> getAllTechTalentAgent() {
        return techTalentAgentRepository.findAll()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private TechTalentAgentDto mapToDto(TechTalentAgent techTalentAgent) {
        TechTalentAgentDto techTalentAgentDto = new TechTalentAgentDto();
        BeanUtils.copyProperties(techTalentAgent, techTalentAgentDto);
        return techTalentAgentDto;
    }




}



