package com.pagination.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pagination.dto.EmployeeRequest;
import com.pagination.dto.EmployeeResponse;
import com.pagination.entity.Employee;
import com.pagination.exception.EmployeeNotFoundException;
import com.pagination.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
	
	public EmployeeResponse createEmployee(EmployeeRequest request) {
		
		Employee  employee = Employee.builder()
				                     .name(request.getName())
				                     .email(request.getEmail())
				                     .department(request.getDepartment())
				                     .Salary(request.getSalary())
				                     .build();
		
	 Employee savedEmployee = employeeRepository.save(employee);
	 
	 return mapToResponse(savedEmployee);
	}
	
	// GET BY ID
	public EmployeeResponse getEmployeeById(Long id) {

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with Id: " + id));

		return mapToResponse(employee);
	}
	
	// GET All with pagination
	public Page<EmployeeResponse> getEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable).map(this::mapToResponse);
	}
	
	// SEARCH + PAGINATION
	public Page<EmployeeResponse> searchEmployee(String name,Pageable pageable){
		return employeeRepository.findByNameContainingIgnoreCase(name, pageable)
				                 .map(this::mapToResponse);
	}
	
	//UPDATE
	public EmployeeResponse updateEmployee(Long id,EmployeeRequest request) {
	   Employee employee = employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("Employee not found with id:"+id));
	   
	   employee.setName(request.getName());
	   employee.setEmail(request.getEmail());
	   employee.setDepartment(request.getDepartment());
	   employee.setSalary(request.getSalary());
	   
	   Employee updatedEmployee = employeeRepository.save(employee);
	   
	   return mapToResponse(updatedEmployee);
	}
	
	// DELETE
	public void deleteEmployee(Long id) {
		Employee employee = employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("Employee not found with id: "+id));
		
		employeeRepository.delete(employee);
	}
	
	// Entity -> DTO
	private EmployeeResponse mapToResponse(Employee employee) {
		
		return EmployeeResponse.builder().id(employee.getId())
				                         .name(employee.getName())
				                         .email(employee.getEmail())
				                         .department(employee.getDepartment())
				                         .salary(employee.getSalary())
				                         .build();
	}
	
	public List<EmployeeResponse> getAllEmployeeDetails(){
		
		return employeeRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}
	

}
