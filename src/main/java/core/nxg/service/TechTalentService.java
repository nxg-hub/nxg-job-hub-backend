package core.nxg.service;

import core.nxg.dto.DashboardDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TechTalentService<T> {

    TechTalentDTO createTechTalent(HttpServletRequest request, TechTalentDTO techTalentDto) throws Exception;

    Page<TechTalentDTO> getAllTechTalent(Pageable pageable) throws Exception; //this should be  pageable and not list
    void  deleteTechTalentUser(Long techId) throws Exception;
    TechTalentDTO getTechTalentById(Long Id)  throws Exception;
    TechTalentDTO updateTechTalent(TechTalentDTO techTalentDto, Long techId) throws Exception;
    DashboardDTO getTechTalentDashboard(HttpServletRequest request, Pageable pageable) throws Exception;
    UserResponseDto getMe(HttpServletRequest request) throws Exception;

        
}
