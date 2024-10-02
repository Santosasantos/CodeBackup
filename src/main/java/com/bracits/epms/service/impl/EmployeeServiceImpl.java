package com.bracits.epms.service.impl;

import com.bracits.epms.domain.Employee;
import com.bracits.epms.domain.enumeration.ResponderCategory;
import com.bracits.epms.repository.EmployeeRepository;
import com.bracits.epms.service.EmployeeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        log.debug("Request to update Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                if (employee.getFirstname() != null) {
                    existingEmployee.setFirstname(employee.getFirstname());
                }
                if (employee.getLastname() != null) {
                    existingEmployee.setLastname(employee.getLastname());
                }
                if (employee.getPin() != null) {
                    existingEmployee.setPin(employee.getPin());
                }
                if (employee.getProject() != null) {
                    existingEmployee.setProject(employee.getProject());
                }
                if (employee.getEmployeeCategory() != null) {
                    existingEmployee.setEmployeeCategory(employee.getEmployeeCategory());
                }
                if (employee.getDesignation() != null) {
                    existingEmployee.setDesignation(employee.getDesignation());
                }
                if (employee.getFunctionalDesignation() != null) {
                    existingEmployee.setFunctionalDesignation(employee.getFunctionalDesignation());
                }
                if (employee.getJoiningDate() != null) {
                    existingEmployee.setJoiningDate(employee.getJoiningDate());
                }
                if (employee.getCurrentOffice() != null) {
                    existingEmployee.setCurrentOffice(employee.getCurrentOffice());
                }
                if (employee.getJobStatus() != null) {
                    existingEmployee.setJobStatus(employee.getJobStatus());
                }
                if (employee.getEmployeeStatus() != null) {
                    existingEmployee.setEmployeeStatus(employee.getEmployeeStatus());
                }
                if (employee.getDateOfBirth() != null) {
                    existingEmployee.setDateOfBirth(employee.getDateOfBirth());
                }
                if (employee.getGender() != null) {
                    existingEmployee.setGender(employee.getGender());
                }
                if (employee.getMobile() != null) {
                    existingEmployee.setMobile(employee.getMobile());
                }
                if (employee.getEmail() != null) {
                    existingEmployee.setEmail(employee.getEmail());
                }
                if (employee.getGrade() != null) {
                    existingEmployee.setGrade(employee.getGrade());
                }
                if (employee.getProfile() != null) {
                    existingEmployee.setProfile(employee.getProfile());
                }
                if (employee.getProfileContentType() != null) {
                    existingEmployee.setProfileContentType(employee.getProfileContentType());
                }

                return existingEmployee;
            })
            .map(employeeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable);
    }

    public Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return employeeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> findByPin(String pin) {
        log.debug("Request to get Employee by pin : {}", pin);
        return employeeRepository.findByPin(pin);
    }

    @Override
    public Optional<Employee> findSupervisor(String pin) {
        log.debug("Request to get Supervisor by pin : {}", pin);
        return employeeRepository.findSupervisor(pin);
    }

    @Override
    public List<Employee> getEligibleEmployees(ResponderCategory category, String currentUserPin) {
        Employee currentEmployee = employeeRepository
            .findByPin(currentUserPin)
            .orElseThrow(() -> new RuntimeException("Current employee not found"));

        switch (category) {
            case PEER:
                return employeeRepository.findPeers(
                    currentEmployee.getId(),
                    currentEmployee.getGrade(),
                    currentEmployee.getFunctionalDesignation()
                );
            case SUPERVISEE:
                return employeeRepository.findBySupervisor(currentUserPin);
            default:
                throw new IllegalArgumentException("Invalid category");
        }
    }

    @Override
    public List<Employee> searchEmployeess(String term, ResponderCategory category, String currentUserPin) {
        List<Employee> eligibleEmployees = getEligibleEmployees(category, currentUserPin);
        return eligibleEmployees
            .stream()
            .filter(
                e ->
                    e.getPin().contains(term) ||
                    e.getFirstname().toLowerCase().contains(term.toLowerCase()) ||
                    e.getLastname().toLowerCase().contains(term.toLowerCase()) ||
                    e.getProject().toLowerCase().contains(term.toLowerCase())
            )
            .collect(Collectors.toList());
    }

    @Override
    public List<Employee> searchRequester(String searchTerm) {
        return employeeRepository.searchRequester(searchTerm);
    }
}
