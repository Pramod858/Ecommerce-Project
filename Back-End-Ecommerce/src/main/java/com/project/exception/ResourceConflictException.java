package com.project.exception;

public class ResourceConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceConflictException(String message) {
        super(message);
    }

    public ResourceConflictException(String resourceName, Long id) {
        super(String.format("%s with ID %d already exists", resourceName, id));
    }
    
    public ResourceConflictException(String resourceName, String resource) {
        super(String.format("%s with ID %s already exists", resourceName, resource));
    }
}
