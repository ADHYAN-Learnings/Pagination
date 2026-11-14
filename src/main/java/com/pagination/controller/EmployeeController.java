package com.pagination.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pagination.dto.EmployeeRequest;
import com.pagination.dto.EmployeeResponse;
import com.pagination.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
   private final EmployeeService employeeService;

   public EmployeeController(EmployeeService employeeService) {
	this.employeeService = employeeService;
   }
   
   //CREATE
   @PostMapping
   public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
	   
	   EmployeeResponse response = employeeService.createEmployee(request);
	   
	   return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }
   
   // GET BY ID
   @GetMapping("/{id}")
   public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id){
	   
	   EmployeeResponse responseById = employeeService.getEmployeeById(id);
	   
	   return ResponseEntity.status(HttpStatus.OK).body(responseById);
   }
   
   @GetMapping("/getAllEmployee")
   public ResponseEntity<List<EmployeeResponse>> getAllEmployees(){
	  List<EmployeeResponse> response = employeeService.getAllEmployeeDetails();
	  
	  return ResponseEntity.status(HttpStatus.OK).body(response);
   }
   
   // PAGINATION
   @GetMapping
   public ResponseEntity<Page<EmployeeResponse>> getEmployees(
	   
	   @RequestParam(defaultValue = "0") int page,
	   @RequestParam(defaultValue = "10") int size,
	   @RequestParam(defaultValue = "id") String sortBy,
	   @RequestParam(defaultValue = "asc") String direction) {
	   
	   Sort sort;
	   
	   if(direction.equalsIgnoreCase("desc")) {
		   sort = Sort.by(sortBy).descending();
	   } else {
		   sort = Sort.by(sortBy).ascending();
	   }
	   
	   Pageable pageable = PageRequest.of(page, size, sort);
	   
	   Page<EmployeeResponse> pageableResponse = employeeService.getEmployees(pageable);
	   
	   return ResponseEntity.status(HttpStatus.OK).body(pageableResponse);
   }
   
   // Search + Pagination
   
   @GetMapping("/search")
   public ResponseEntity<Page<EmployeeResponse>> searchEmployees(
		   @RequestParam String name,
		   @RequestParam(defaultValue = "0") int page,
		   @RequestParam(defaultValue = "10") int size,
		   @RequestParam(defaultValue = "id") String sortBy,
		   @RequestParam(defaultValue = "asc") String direction) {
	   
	   Sort sort;
	   
	   if(direction.equalsIgnoreCase("desc")) {
		   sort = Sort.by(sortBy).descending();
	   } else {
		   sort = Sort.by(sortBy).ascending();
	   }
	   
	   Pageable pageable = PageRequest.of(page, size, sort);
	   
	   Page<EmployeeResponse> pageResponse = employeeService.searchEmployee(name, pageable);
	   
	   return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
	   
   }
   
   @PutMapping("/{id}")
   public ResponseEntity<EmployeeResponse> updateEmployee(
		   @PathVariable Long id,
		   @Valid @RequestBody EmployeeRequest request){

	   
	   EmployeeResponse employeeResponse = employeeService.updateEmployee(id, request);
	   
	   return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
}
   
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
	   
	   employeeService.deleteEmployee(id);
	   
	   return ResponseEntity.noContent().build();
   }
}