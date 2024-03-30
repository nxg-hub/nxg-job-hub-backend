package core.nxg;

import core.nxg.entity.JobPosting;
import core.nxg.entity.User;
import core.nxg.repository.JobPostingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import core.nxg.enums.Roles;

import java.util.*;

import static core.nxg.enums.Roles.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class NxgApplicationTests {

	@InjectMocks
	private User user;


	@Mock
	 JobPostingRepository jobPostingRepository;

	@Mock
	private JobPosting jobPosting;
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	@Test
	void contextLoads() {
	}


	@Test
	public void findByEmployerIDReturnsJobPostings() {
		List<JobPosting> jobPostings = List.of(jobPosting);
		when(jobPostingRepository.findByEmployerID("employer1")).thenReturn(Optional.of(jobPostings));
		Optional<List<JobPosting>> result = jobPostingRepository.findByEmployerID("employer1");
		assertEquals(Optional.of(jobPostings), result);
	}



	@Test
	public void getAuthoritiesReturnsCorrectAuthoritiesWhenRolesIsNotEmpty() {
		List<Roles> roles =  Arrays.asList( Roles.ADMIN, USER);
		user.setRoles(roles);

		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

		assertEquals(2, authorities.size());
		assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
		assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
	}

	@Test
	public void getAuthoritiesReturnsEmptyCollectionWhenRolesIsEmpty() {
		user.setRoles(new ArrayList<>());

		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

		assertTrue(authorities.isEmpty());
	}

}
