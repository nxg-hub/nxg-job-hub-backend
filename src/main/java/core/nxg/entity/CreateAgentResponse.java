package core.nxg.entity;


import core.nxg.dto.TechTalentAgentDto;
import core.nxg.dto.UserDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class CreateAgentResponse {
   // private UserDto user;
    private TechTalentAgentDto agent;

    private UserDTO user;
    private TechTalentAgent techTalentAgent;

    public CreateAgentResponse(User user, TechTalentAgent techTalentAgent) {
    }
    // Constructors, getters, and setters
}


