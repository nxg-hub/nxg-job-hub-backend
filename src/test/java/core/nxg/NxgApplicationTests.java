package core.nxg;

import core.nxg.entity.JobPosting;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.AdminService;
import core.nxg.serviceImpl.AdminServiceImpl;
import core.nxg.subscription.enums.JobStatus;
import core.nxg.subscription.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import core.nxg.enums.Roles;

import java.util.*;

import static core.nxg.enums.Roles.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


class NxgApplicationTests {


	@InjectMocks
	private User user;




	@Mock
	private AdminService adminService;


	@Mock
	private TransactionRepository transactionRepository;


	@Mock
	private UserRepository userRepository;


	@Mock
	 private JobPostingRepository jobPostingRepository;


	@Mock
	private JobPosting jobPosting;
	@BeforeEach
	public void setup() {

		MockitoAnnotations.openMocks(this);

		adminService = new AdminServiceImpl(transactionRepository, jobPostingRepository, userRepository);



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



	@Test
	public void test_accept_valid_job_id() {


	JobPosting job = new JobPosting();
        job.setJobStatus(JobStatus.PENDING);
        job.setActive(false);
	when(jobPostingRepository.findById(job.getJobID())).thenReturn(Optional.of(job));

	// Act
        adminService.acceptJob(job.getJobID());

	// Assert
	assertEquals(JobStatus.ACCEPTED, job.getJobStatus());
	assertTrue(job.isActive());
	verify(jobPostingRepository, times(1)).saveAndFlush(job);
}



	@Test
	public void rejectJobChangesJobStatusToRejected() {


		JobPosting job = new JobPosting();
		job.setJobStatus(JobStatus.PENDING);
		when(jobPostingRepository.findById(job.getJobID())).thenReturn(Optional.of(job));

		adminService.rejectJob(job.getJobID());

		verify(jobPostingRepository).save(job);
		assertEquals(JobStatus.REJECTED, job.getJobStatus());
	}

	@Test
	public void suspendJobChangesJobStatusToSuspended() {


		JobPosting job = new JobPosting();
		job.setJobStatus(JobStatus.ACCEPTED);
		when(jobPostingRepository.findById( job.getJobID())).thenReturn(Optional.of(job));

		adminService.suspendJob(job.getJobID());

		verify(jobPostingRepository).save(job);
		assertEquals(JobStatus.SUSPENDED, job.getJobStatus());
	}

	@Test
	public void suspendUserChangesUserStatusToDisabled() {
		User user = new User();
		user.setEnabled(true);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		adminService.suspendUser(user.getId());

		verify(userRepository).save(user);
		assertFalse(user.isEnabled());
	}

	@Test
	public void acceptJobThrowsExceptionWhenJobNotFound() {
		when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> adminService.acceptJob(1L));
	}

	@Test
	public void suspendUserThrowsExceptionWhenUserNotFound() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> adminService.suspendUser(1L));
	}


}
