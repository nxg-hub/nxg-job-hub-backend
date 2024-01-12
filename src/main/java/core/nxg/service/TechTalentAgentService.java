package core.nxg.service;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.response.PaginatedResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TechTalentAgentService {



    String createAgent(TechTalentAgentDto agentDTO, HttpServletRequest request) throws ExpiredJWTException;

    TechTalentAgent patchAgent(String agentId, Map<Object, Object> fields);

    void deleteTechTalentAgent(Long techTalentAgentId);

    TechTalentAgentDto getTechTalentAgentById(Long techTalentAgentId);

//    PaginatedResponse<TechTalentAgentDto> getAllTechTalentAgent(int page, int size);

}
