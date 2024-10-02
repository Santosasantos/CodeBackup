package com.bracits.epms.domain;

import com.bracits.epms.domain.enumeration.EmployeeCategory;
import com.bracits.epms.domain.enumeration.EmployeeStatus;
import com.bracits.epms.domain.enumeration.Gender;
import com.bracits.epms.domain.enumeration.JobStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "firstname", length = 20, nullable = false)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "lastname", length = 20, nullable = false)
    private String lastname;

    @NotNull
    @Size(min = 4, max = 8)
    @Column(name = "pin", length = 8, nullable = false, unique = true)
    private String pin;

    @Column(name = "project")
    private String project;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_category", nullable = false)
    private EmployeeCategory employeeCategory;

    @Column(name = "designation")
    private String designation;

    @Column(name = "functional_designation")
    private String functionalDesignation;

    @Column(name = "joining_date")
    private Instant joiningDate;

    @Column(name = "current_office")
    private String currentOffice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "job_status", nullable = false)
    private JobStatus jobStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_status", nullable = false)
    private EmployeeStatus employeeStatus;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "grade")
    private Integer grade;

    @Lob
    @Column(name = "profile")
    private byte[] profile;

    @Column(name = "profile_content_type")
    private String profileContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "supervisor" }, allowSetters = true)
    private Employee supervisor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Employee firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Employee lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPin() {
        return this.pin;
    }

    public Employee pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getProject() {
        return this.project;
    }

    public Employee project(String project) {
        this.setProject(project);
        return this;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public EmployeeCategory getEmployeeCategory() {
        return this.employeeCategory;
    }

    public Employee employeeCategory(EmployeeCategory employeeCategory) {
        this.setEmployeeCategory(employeeCategory);
        return this;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Employee designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFunctionalDesignation() {
        return this.functionalDesignation;
    }

    public Employee functionalDesignation(String functionalDesignation) {
        this.setFunctionalDesignation(functionalDesignation);
        return this;
    }

    public void setFunctionalDesignation(String functionalDesignation) {
        this.functionalDesignation = functionalDesignation;
    }

    public Instant getJoiningDate() {
        return this.joiningDate;
    }

    public Employee joiningDate(Instant joiningDate) {
        this.setJoiningDate(joiningDate);
        return this;
    }

    public void setJoiningDate(Instant joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getCurrentOffice() {
        return this.currentOffice;
    }

    public Employee currentOffice(String currentOffice) {
        this.setCurrentOffice(currentOffice);
        return this;
    }

    public void setCurrentOffice(String currentOffice) {
        this.currentOffice = currentOffice;
    }

    public JobStatus getJobStatus() {
        return this.jobStatus;
    }

    public Employee jobStatus(JobStatus jobStatus) {
        this.setJobStatus(jobStatus);
        return this;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public EmployeeStatus getEmployeeStatus() {
        return this.employeeStatus;
    }

    public Employee employeeStatus(EmployeeStatus employeeStatus) {
        this.setEmployeeStatus(employeeStatus);
        return this;
    }

    public void setEmployeeStatus(EmployeeStatus employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Employee dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Employee mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public Employee grade(Integer grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public byte[] getProfile() {
        return this.profile;
    }

    public Employee profile(byte[] profile) {
        this.setProfile(profile);
        return this;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public String getProfileContentType() {
        return this.profileContentType;
    }

    public Employee profileContentType(String profileContentType) {
        this.profileContentType = profileContentType;
        return this;
    }

    public void setProfileContentType(String profileContentType) {
        this.profileContentType = profileContentType;
    }

    public Employee getSupervisor() {
        return this.supervisor;
    }

    public void setSupervisor(Employee employee) {
        this.supervisor = employee;
    }

    public Employee supervisor(Employee employee) {
        this.setSupervisor(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", pin='" + getPin() + "'" +
            ", project='" + getProject() + "'" +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", functionalDesignation='" + getFunctionalDesignation() + "'" +
            ", joiningDate='" + getJoiningDate() + "'" +
            ", currentOffice='" + getCurrentOffice() + "'" +
            ", jobStatus='" + getJobStatus() + "'" +
            ", employeeStatus='" + getEmployeeStatus() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", email='" + getEmail() + "'" +
            ", grade=" + getGrade() +
            ", profile='" + getProfile() + "'" +
            ", profileContentType='" + getProfileContentType() + "'" +
            "}";
    }
}
