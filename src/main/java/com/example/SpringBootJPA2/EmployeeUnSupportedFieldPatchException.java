/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.SpringBootJPA2;

import java.util.Set;

/**
 *
 * @author WayneHu
 */
public class EmployeeUnSupportedFieldPatchException extends RuntimeException {

    public EmployeeUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}
