package com.vf.exception;
public class ASMResourceNotFoundException extends Exception {
private String id;
public ASMResourceNotFoundException(Long id) {
        super(String.format("Child order not found with id : '%s'", id));
        }
}