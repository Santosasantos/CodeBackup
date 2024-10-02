package com.bracits.epms.service;

import com.bracits.epms.domain.Employee;
import com.bracits.epms.domain.enumeration.ResponderCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.Employee}.
 */
public interface EmployeeService {
    /**
     * Save a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    Employee save(Employee employee);

    /**
     * Updates a employee.
     *
     * @param employee the entity to update.
     * @return the persisted entity.
     */
    Employee update(Employee employee);

    /**
     * Partially updates a employee.
     *
     * @param employee the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Employee> partialUpdate(Employee employee);

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Employee> findAll(Pageable pageable);

    /**
     * Get all the employees with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Employee> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" employee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Employee> findOne(Long id);

    /**
     * Delete the "id" employee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Employee> findByPin(String pin);

    Optional<Employee> findSupervisor(String pin);

    public List<Employee> getEligibleEmployees(ResponderCategory category, String currentUserPin);

    public List<Employee> searchEmployeess(String term, ResponderCategory category, String currentUserPin);

    public List<Employee> searchRequester(String term);
}
