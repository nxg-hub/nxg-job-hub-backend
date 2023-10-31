package core.nxg.serviceImpl;

import core.nxg.configs.JwtService;
import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;
import core.nxg.entity.User;
import core.nxg.exceptions.AlreadyExistException;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.TechTalentAgentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.response.PaginatedResponse;
import core.nxg.service.TechTalentAgentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TechTalentAgentServiceImpl implements TechTalentAgentService {


    private final TechTalentAgentRepository techTalentAgentRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Override
    public String verifyTechTalentAgent(String email, HttpServletRequest request) {
        try {
            User user = extractLoggedInUser(request, email);
            if (user != null) {
                TechTalentAgent techTalentAgent = new TechTalentAgent();
                techTalentAgent.setEmail(email);
                techTalentAgentRepository.save(techTalentAgent);
                return "Tech Talent Agent verified and created";
            } else {
                throw new NotFoundException("Email not found in the user repository");
            }
        } catch (Exception e) {
            return "Error verifying Tech Talent Agent: " + e.getMessage();
        }
    }

    @Override
    public String createAgent(TechTalentAgentDto agentDTO) {
            TechTalentAgent techTalentAgent = techTalentAgentRepository.findByEmail(agentDTO.getEmail())
                    .orElseThrow(() -> new AlreadyExistException("Tech Talent Agent already exists"));

            TechTalentAgent agent = new TechTalentAgent();
            agent.setZipCode(agentDTO.getZipCode());
            agent.setJobType(agentDTO.getJobType());
            agent.setIndustryType(agentDTO.getIndustryType());
            agent.setAddress(agentDTO.getAddress());

            User user = userRepository.findByEmail(techTalentAgent.getEmail())
                    .orElseThrow(() -> new NotFoundException("Tech Talent Agent not verified"));
            agent.setUser(user);
            techTalentAgentRepository.save(agent);
            return "Agent successfully registered";
    }

    private User extractLoggedInUser(HttpServletRequest request, String email) {
        // ... (extractLoggedInUser logic)
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String extractedEmail = jwtService.extractUsername(jwt);
        if (email.equals(extractedEmail)) {
            return userRepository.findByEmail(email).orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public TechTalentAgentDto updateTechTalentAgent(TechTalentAgentDto techTalentAgentDto) {
        TechTalentAgent techTalentAgent = techTalentAgentRepository.findById(techTalentAgentDto.getId())
                .orElseThrow(() -> new NotFoundException("TechTalentAgent with ID " + techTalentAgentDto.getId() + " not found"));
        techTalentAgent.setIndustryType(techTalentAgentDto.getIndustryType());
        techTalentAgent.setAddress(techTalentAgentDto.getAddress());
        techTalentAgent.setZipCode(techTalentAgentDto.getZipCode());
        techTalentAgent.setIndustryType(techTalentAgentDto.getIndustryType());
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
    public TechTalentAgentDto getTechTalentAgentById(Long Id) {
        TechTalentAgent techTalentAgent = techTalentAgentRepository.findById(Id)
                .orElseThrow(() -> new NotFoundException("TechTalentAgent with ID " + Id + " not found"));
        return mapToDto(techTalentAgent);
    }

    @Override
    public PaginatedResponse<TechTalentAgentDto> getAllTechTalentAgent(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TechTalentAgent> agentPages = techTalentAgentRepository.findAll(pageable);
        return PaginatedResponse.<TechTalentAgentDto>builder()
                .content(agentPages.getContent().stream().map(this::mapToDto).collect(Collectors.toList()))
                .currentPage(agentPages.getNumber()+1)
                .pageSize(agentPages.getSize())
                .totalPages(agentPages.getTotalPages())
                .totalItems(agentPages.getTotalElements())
                .isFirstPage(agentPages.isFirst())
                .isLastPage(agentPages.isLast())
                .build();
    }

    private TechTalentAgentDto mapToDto(TechTalentAgent techTalentAgent) {
        TechTalentAgentDto techTalentAgentDto = new TechTalentAgentDto();
        techTalentAgentDto.setAddress(techTalentAgent.getAddress());
        techTalentAgentDto.setEmail(techTalentAgent.getUser().getEmail());
        techTalentAgentDto.setIndustryType(techTalentAgent.getIndustryType());
        techTalentAgentDto.setJobType(techTalentAgent.getJobType());
        techTalentAgentDto.setZipCode(techTalentAgent.getZipCode());
        return techTalentAgentDto;
    }

    }



