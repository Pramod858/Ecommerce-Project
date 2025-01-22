package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.ProductDTO;
import com.project.dto.UserDTO;
import com.project.exception.UserNotFoundException;
import com.project.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> userResponse = userService.getAllUsers();
		return ResponseEntity.ok(userResponse);
	}
	
	@GetMapping("/check-email")
	public ResponseEntity<?> checkEmail(@RequestParam String email) {
	    if (userService.emailExists(email)) {
	        return ResponseEntity.badRequest().body("Email already exists"); 
	    } else {
	        return ResponseEntity.ok("Email is available");
	    }
	}
	
	@GetMapping("/check-phone")
    public ResponseEntity<?> checkPhone(@RequestParam String phone) {
        if (userService.phoneExists(phone)) {
            return ResponseEntity.badRequest().body("Phone already exists");
        }
        return ResponseEntity.ok("Phone is avaialable");
    }
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable Long userId) {
		try {
			UserDTO userResponse = userService.getUserById(userId);
			return new ResponseEntity<>(userResponse, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("User not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO userDTO, @PathVariable Long userId) {
		try {
			userService.updateUser(userDTO, userId);
			return new ResponseEntity<>("User updated successfully.", HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("User not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An uexpected error occured: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 @DeleteMapping("/{userId}")
	    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
	        try {
	            userService.deleteUser(userId);
	            return new ResponseEntity<>("User with ID " + userId + " deleted successfully", HttpStatus.OK);
	        } catch (UserNotFoundException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	        }
	    }
}