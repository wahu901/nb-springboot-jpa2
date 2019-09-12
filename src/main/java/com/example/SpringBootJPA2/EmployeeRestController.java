/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.SpringBootJPA2;

import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author WayneHu
 */
@RestController
//@RequestMapping("/url")
public class EmployeeRestController {
    @Autowired EmployeeRepository repository;
    
    @GetMapping()
    public List<Employee> list() {
        Iterable<Employee> iterable = repository.findAll();
        ArrayList<Employee> list = new ArrayList<Employee>();

        if(iterable != null) {
            for(Employee e: iterable) {
                list.add(e);
            }
        }    
        return list;        
    }
    
    
    
    // Save
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        System.out.println("post");
        return repository.save(newEmployee);
    }

    // Find
    @GetMapping("/employees/{id}")
    Employee findOne(@PathVariable Long id) {
         System.out.println("find "+id);
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // Save or update
    @PutMapping("/employees/{id}")
    Employee saveOrUpdate(@RequestBody Employee newEmployee, @PathVariable Long id) {
        System.out.println("put "+id);
        return repository.findById(id)
                .map(x -> {
                    x.setLastname(newEmployee.getLastname());
                    x.setFirstname(newEmployee.getFirstname());
                    x.setUserid(newEmployee.getUserid());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    // update author only
    @PatchMapping("/employees/{id}")
    Employee patch(@RequestBody Map<String, String> update, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {

                    String userid = update.get("userid");
                    if (!StringUtils.isEmpty(userid)) {
                        x.setUserid(userid);

                        // better create a custom method to update a value = :newValue where id = :id
                        return repository.save(x);
                    } else {
                        throw new EmployeeUnSupportedFieldPatchException(update.keySet());
                    }

                })
                .orElseGet(() -> {
                    throw new EmployeeNotFoundException(id);
                });

    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }

    
    
}
