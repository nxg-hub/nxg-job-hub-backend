package core.nxg.service;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import core.nxg.response.PaginatedResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TechTalentAgentService {

    String verifyTechTalentAgent(String email, HttpServletRequest request);

    String createAgent(TechTalentAgentDto agentDTO);

    TechTalentAgentDto updateTechTalentAgent(TechTalentAgentDto techTalentAgentDto);

    void deleteTechTalentAgent(Long techTalentAgentId);

    TechTalentAgentDto getTechTalentAgentById(Long techTalentAgentId);

   PaginatedResponse<TechTalentAgentDto> getAllTechTalentAgent(int page, int size);

}
