package com.vf.exception;
public class ASMEdgeNotFoundException extends Exception {
private String id;
public ASMEdgeNotFoundException(Long id) {
        super(String.format("Master order not found with id : '%s'", id));
        }
}