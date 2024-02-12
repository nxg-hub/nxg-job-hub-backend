package core.nxg.serviceImpl;

import core.nxg.configs.JwtService;
import core.nxg.dto.TechTalentAgentDto;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.Employer;
import core.nxg.entity.TechTalentAgent;
import core.nxg.entity.User;
import core.nxg.enums.UserType;
import core.nxg.exceptions.AlreadyExistException;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.exceptions.NotFoundException;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.TechTalentAgentRepository;
import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.response.EmployerResponse;
import core.nxg.response.PaginatedResponse;
import core.nxg.service.TechTalentAgentService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TechTalentAgentServiceImpl implements TechTalentAgentService {

    private final EmployerRepository employerRepository;
    private final TechTalentRepository techTalentRepository;
    private final TechTalentAgentRepository techTalentAgentRepository;
    private final UserRepository userRepository;
    private final Helper helper;

    private final ModelMapper mapper;




    @Override
    public String createAgent(TechTalentAgentDto agentDTO, HttpServletRequest request) throws ExpiredJWTException {

        User loggedInUser = helper.extractLoggedInUser(request);


        Optional<EmployerResponse> employer_account = employerRepository.findByEmail(loggedInUser.getEmail());
        if (employer_account.isPresent()) {            // an employer account does not exist
            throw new UserAlreadyExistException("An Employer account already exists!");
        }


        Optional<TechTalentDTO> techtalent_account = techTalentRepository.findByEmail(loggedInUser.getEmail());
        if (techtalent_account.isPresent()) // techtalent account does not exist
            throw new UserAlreadyExistException("Tech talent account already exists!");


        Optional<TechTalentAgent> agent_account = techTalentAgentRepository.findByUserEmail(loggedInUser.getEmail()); // confirm
        if (agent_account.isPresent()) {
            throw new AlreadyExistException("An Agent account already exists!");}


        TechTalentAgent techTalentAgent = new TechTalentAgent();
        techTalentAgent.setIndustryType(agentDTO.getIndustryType());
        techTalentAgent.setEmail(loggedInUser.getEmail());
        techTalentAgent.setAddress(agentDTO.getAddress());
        techTalentAgent.setZipCode(agentDTO.getZipCode());
        loggedInUser.setUserType(UserType.AGENT);
        loggedInUser.setTechTalentAgent(techTalentAgent);
        userRepository.save(loggedInUser);
        techTalentAgent.setUser(loggedInUser);

        techTalentAgentRepository.saveAndFlush(techTalentAgent);


        return "Agent successfully registered";

    }




    @Override
    public TechTalentAgent patchAgent(String agentId, Map<Object, Object> fields) {
        if (agentId == null) {
            throw new NotFoundException("Agent ID is required");}
        Optional<TechTalentAgent> agent = techTalentAgentRepository.findById(Long.valueOf(agentId));
        if (agent.isPresent()) {
            fields.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(TechTalentAgent.class, String.valueOf(key));
                        if (field == null)
                            throw new IllegalArgumentException("Null fields cannot be updated! Kindly check the fields you are trying to update");

                        field.setAccessible(true);
                        ReflectionUtils.setField(field, agent.get(), value);

                    }
            );
            return techTalentAgentRepository.save(agent.get());
        }else
        {
            throw new NotFoundException("Agent not found");
        }

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
        return mapper.map(techTalentAgent, TechTalentAgentDto.class);
    }

//    @Override
//    public PaginatedResponse<TechTalentAgentDto> getAllTechTalentAgent(int page, int size) {
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<TechTalentAgent> agentPages = techTalentAgentRepository.findAll(pageable);
//        return PaginatedResponse.<TechTalentAgentDto>builder()
//                .content(agentPages.getContent().stream().map(this::mapToDto).collect(Collectors.toList()))
//                .currentPage(agentPages.getNumber()+1)
//                .pageSize(agentPages.getSize())
//                .totalPages(agentPages.getTotalPages())
//                .totalItems(agentPages.getTotalElements())
//                .isFirstPage(agentPages.isFirst())
//                .isLastPage(agentPages.isLast())
//                .build();
//    }

//    private TechTalentAgentDto mapToDto(TechTalentAgent techTalentAgent) {
//        TechTalentAgentDto techTalentAgentDto = new TechTalentAgentDto();
//        techTalentAgentDto.setAddress(techTalentAgent.getAddress());
//        techTalentAgentDto.setEmail(.getEmail());
//        techTalentAgentDto.setIndustryType(techTalentAgent.getIndustryType());
//        techTalentAgentDto.setJobType(techTalentAgent.getJobType());
//        techTalentAgentDto.setZipCode(techTalentAgent.getZipCode());
//        return techTalentAgentDto;
//    }

    }



