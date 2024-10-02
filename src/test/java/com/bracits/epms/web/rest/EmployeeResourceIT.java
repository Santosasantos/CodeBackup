package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.EmployeeAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.Employee;
import com.bracits.epms.domain.enumeration.EmployeeCategory;
import com.bracits.epms.domain.enumeration.EmployeeStatus;
import com.bracits.epms.domain.enumeration.Gender;
import com.bracits.epms.domain.enumeration.JobStatus;
import com.bracits.epms.repository.EmployeeRepository;
import com.bracits.epms.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PIN = "AAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBB";

    private static final String DEFAULT_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT = "BBBBBBBBBB";

    private static final EmployeeCategory DEFAULT_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR;
    private static final EmployeeCategory UPDATED_EMPLOYEE_CATEGORY = EmployeeCategory.CONTRACT;

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_FUNCTIONAL_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_FUNCTIONAL_DESIGNATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_JOINING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JOINING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CURRENT_OFFICE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_OFFICE = "BBBBBBBBBB";

    private static final JobStatus DEFAULT_JOB_STATUS = JobStatus.ACTIVE;
    private static final JobStatus UPDATED_JOB_STATUS = JobStatus.INACTIVE;

    private static final EmployeeStatus DEFAULT_EMPLOYEE_STATUS = EmployeeStatus.CONFIRM;
    private static final EmployeeStatus UPDATED_EMPLOYEE_STATUS = EmployeeStatus.NONCONFIRM;

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_GRADE = 1;
    private static final Integer UPDATED_GRADE = 2;

    private static final byte[] DEFAULT_PROFILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Mock
    private EmployeeService employeeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    private Employee insertedEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .pin(DEFAULT_PIN)
            .project(DEFAULT_PROJECT)
            .employeeCategory(DEFAULT_EMPLOYEE_CATEGORY)
            .designation(DEFAULT_DESIGNATION)
            .functionalDesignation(DEFAULT_FUNCTIONAL_DESIGNATION)
            .joiningDate(DEFAULT_JOINING_DATE)
            .currentOffice(DEFAULT_CURRENT_OFFICE)
            .jobStatus(DEFAULT_JOB_STATUS)
            .employeeStatus(DEFAULT_EMPLOYEE_STATUS)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .mobile(DEFAULT_MOBILE)
            .email(DEFAULT_EMAIL)
            .grade(DEFAULT_GRADE)
            .profile(DEFAULT_PROFILE)
            .profileContentType(DEFAULT_PROFILE_CONTENT_TYPE);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .pin(UPDATED_PIN)
            .project(UPDATED_PROJECT)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .designation(UPDATED_DESIGNATION)
            .functionalDesignation(UPDATED_FUNCTIONAL_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE)
            .currentOffice(UPDATED_CURRENT_OFFICE)
            .jobStatus(UPDATED_JOB_STATUS)
            .employeeStatus(UPDATED_EMPLOYEE_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .grade(UPDATED_GRADE)
            .profile(UPDATED_PROFILE)
            .profileContentType(UPDATED_PROFILE_CONTENT_TYPE);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployee != null) {
            employeeRepository.delete(insertedEmployee);
            insertedEmployee = null;
        }
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Employee
        var returnedEmployee = om.readValue(
            restEmployeeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Employee.class
        );

        // Validate the Employee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeUpdatableFieldsEquals(returnedEmployee, getPersistedEmployee(returnedEmployee));

        insertedEmployee = returnedEmployee;
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employee.setFirstname(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employee.setLastname(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employee.setPin(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmployeeCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employee.setEmployeeCategory(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJobStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employee.setJobStatus(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmployeeStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employee.setEmployeeStatus(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employee.setGender(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].project").value(hasItem(DEFAULT_PROJECT)))
            .andExpect(jsonPath("$.[*].employeeCategory").value(hasItem(DEFAULT_EMPLOYEE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].functionalDesignation").value(hasItem(DEFAULT_FUNCTIONAL_DESIGNATION)))
            .andExpect(jsonPath("$.[*].joiningDate").value(hasItem(DEFAULT_JOINING_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentOffice").value(hasItem(DEFAULT_CURRENT_OFFICE)))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].employeeStatus").value(hasItem(DEFAULT_EMPLOYEE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)))
            .andExpect(jsonPath("$.[*].profileContentType").value(hasItem(DEFAULT_PROFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.project").value(DEFAULT_PROJECT))
            .andExpect(jsonPath("$.employeeCategory").value(DEFAULT_EMPLOYEE_CATEGORY.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.functionalDesignation").value(DEFAULT_FUNCTIONAL_DESIGNATION))
            .andExpect(jsonPath("$.joiningDate").value(DEFAULT_JOINING_DATE.toString()))
            .andExpect(jsonPath("$.currentOffice").value(DEFAULT_CURRENT_OFFICE))
            .andExpect(jsonPath("$.jobStatus").value(DEFAULT_JOB_STATUS.toString()))
            .andExpect(jsonPath("$.employeeStatus").value(DEFAULT_EMPLOYEE_STATUS.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE))
            .andExpect(jsonPath("$.profileContentType").value(DEFAULT_PROFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profile").value(Base64.getEncoder().encodeToString(DEFAULT_PROFILE)));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployee() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .pin(UPDATED_PIN)
            .project(UPDATED_PROJECT)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .designation(UPDATED_DESIGNATION)
            .functionalDesignation(UPDATED_FUNCTIONAL_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE)
            .currentOffice(UPDATED_CURRENT_OFFICE)
            .jobStatus(UPDATED_JOB_STATUS)
            .employeeStatus(UPDATED_EMPLOYEE_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .grade(UPDATED_GRADE)
            .profile(UPDATED_PROFILE)
            .profileContentType(UPDATED_PROFILE_CONTENT_TYPE);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeToMatchAllProperties(updatedEmployee);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employee.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .lastname(UPDATED_LASTNAME)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .designation(UPDATED_DESIGNATION)
            .functionalDesignation(UPDATED_FUNCTIONAL_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE)
            .jobStatus(UPDATED_JOB_STATUS)
            .employeeStatus(UPDATED_EMPLOYEE_STATUS);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmployee, employee), getPersistedEmployee(employee));
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .pin(UPDATED_PIN)
            .project(UPDATED_PROJECT)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .designation(UPDATED_DESIGNATION)
            .functionalDesignation(UPDATED_FUNCTIONAL_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE)
            .currentOffice(UPDATED_CURRENT_OFFICE)
            .jobStatus(UPDATED_JOB_STATUS)
            .employeeStatus(UPDATED_EMPLOYEE_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .grade(UPDATED_GRADE)
            .profile(UPDATED_PROFILE)
            .profileContentType(UPDATED_PROFILE_CONTENT_TYPE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeUpdatableFieldsEquals(partialUpdatedEmployee, getPersistedEmployee(partialUpdatedEmployee));
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Employee getPersistedEmployee(Employee employee) {
        return employeeRepository.findById(employee.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeToMatchAllProperties(Employee expectedEmployee) {
        assertEmployeeAllPropertiesEquals(expectedEmployee, getPersistedEmployee(expectedEmployee));
    }

    protected void assertPersistedEmployeeToMatchUpdatableProperties(Employee expectedEmployee) {
        assertEmployeeAllUpdatablePropertiesEquals(expectedEmployee, getPersistedEmployee(expectedEmployee));
    }
}
