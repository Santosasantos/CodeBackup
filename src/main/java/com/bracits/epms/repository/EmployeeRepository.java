package com.bracits.epms.repository;

import com.bracits.epms.domain.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    default Optional<Employee> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Employee> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select employee from Employee employee left join fetch employee.supervisor",
        countQuery = "select count(employee) from Employee employee"
    )
    Page<Employee> findAllWithToOneRelationships(Pageable pageable);

    @Query("select employee from Employee employee left join fetch employee.supervisor")
    List<Employee> findAllWithToOneRelationships();

    @Query("select employee from Employee employee left join fetch employee.supervisor where employee.id =:id")
    Optional<Employee> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select employee from Employee employee where employee.pin =:pin")
    Optional<Employee> findByPin(@Param("pin") String pin);

    @Query(
        "SELECT e FROM Employee e WHERE e.id != :employeeId AND ((e.grade BETWEEN :grade - 1 AND :grade + 1 AND e.grade != :grade) OR e.functionalDesignation = :functionaldesignation)"
    )
    List<Employee> findPeers(
        @Param("employeeId") Long employeeId,
        @Param("grade") Integer grade,
        @Param("functionaldesignation") String functionaldesignation
    );

    @Query("select employee.supervisor from Employee employee where employee.pin = :pin")
    Optional<Employee> findSupervisor(@Param("pin") String pin);

    @Query("select employee from Employee employee where employee.supervisor.pin = :supervisorpin")
    List<Employee> findBySupervisor(@Param("supervisorpin") String supervisorpin);

    @Query(
        "select employee from Employee employee where" +
        " lower(employee.firstname) like lower(concat('%', :searchTerm, '%')) or " +
        " lower(employee.lastname) like lower(concat('%', :searchTerm, '%')) or " +
        " lower(employee.pin) like lower(concat('%', :searchTerm, '%')) or " +
        " lower(employee.project) like lower(concat('%', :searchTerm, '%'))"
    )
    List<Employee> searchRequester(@Param("searchTerm") String searchTerm);
}
