package com.bracits.epms.domain;

import static com.bracits.epms.domain.EmployeeTestSamples.*;
import static com.bracits.epms.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void supervisorTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employee.setSupervisor(employeeBack);
        assertThat(employee.getSupervisor()).isEqualTo(employeeBack);

        employee.supervisor(null);
        assertThat(employee.getSupervisor()).isNull();
    }
}
