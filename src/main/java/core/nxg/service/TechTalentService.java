package core.nxg.service;

import core.nxg.dto.DashboardDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.TechTalentUser;
import core.nxg.response.TechTalentResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface TechTalentService<T> {

    String createTechTalent(TechTalentDTO techTalentDto,HttpServletRequest request) throws Exception;

    void  deleteTechTalentUser(Long techId) throws Exception;
    TechTalentDTO getTechTalent(HttpServletRequest request)  throws Exception;
    TechTalentUser updateTechTalent(String techId, Map<Object, Object> fields) throws Exception;
    DashboardDTO getTechTalentDashboard(HttpServletRequest request, Pageable pageable) throws Exception;
    UserResponseDto getMe(HttpServletRequest request) throws Exception;

    void addNewSkills(HttpServletRequest request, List<String> skill) throws Exception;

        
}
