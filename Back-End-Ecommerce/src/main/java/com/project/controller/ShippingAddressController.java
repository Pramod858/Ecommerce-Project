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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.ShippingAddressDTO;
import com.project.entity.User;
import com.project.exception.ShippingAddressNotFoundException;
import com.project.service.AuthService;
import com.project.service.ShippingAddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class ShippingAddressController {
	
	@Autowired
	private ShippingAddressService shippingAddressService;
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/add-address")
	public ResponseEntity<?> addAddress(
			@RequestHeader("Authorization") String token,
			@RequestBody @Valid ShippingAddressDTO shippingAddressDTO) {
		try {
			User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
			shippingAddressService.addAddress(user.getId(), shippingAddressDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Address added successfully!"));
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to add address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/update-address/{addressId}")
	public ResponseEntity<?> updateAddress(
			@RequestHeader("Authorization") String token,
			@PathVariable Long addressId,
			@RequestBody @Valid ShippingAddressDTO shippingAddressDTO) {
		try {
			User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
            
            boolean isOwner = shippingAddressService.isAddressOwnedByUser(addressId, user.getId());
            if (!isOwner) {
            	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this address");
            }
			shippingAddressService.updateAddress(addressId, shippingAddressDTO);
			return ResponseEntity.ok("Address updated successfully!");
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to update address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/delete-address/{addressId}")
	public ResponseEntity<?> deleteAddress(
			@RequestHeader("Authorization") String token,
			@PathVariable Long addressId) {
		try {
			User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
            boolean isOwner = shippingAddressService.isAddressOwnedByUser(addressId, user.getId());
            if (!isOwner) {
            	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this address");
            }
			shippingAddressService.deleteAddress(addressId);
			return ResponseEntity.ok(new ResponseMessage("Address deleted successfully!"));
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to delete address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/my-addresses")
	public ResponseEntity<?> getAllAddressForUser(
			@RequestHeader("Authorization") String token) {
		try {
			User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
			List<ShippingAddressDTO> addresses = shippingAddressService.getAllAddressForUser(user.getId());
			return ResponseEntity.ok(addresses);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to get addresses for user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/my-address/{addressId}")
	public ResponseEntity<?> getAddressById(@PathVariable Long addressId) {
	    try {
	        ShippingAddressDTO address = shippingAddressService.getAddressById(addressId);
	        return ResponseEntity.ok(address);
	    } catch (ShippingAddressNotFoundException e) {
	        return new ResponseEntity<>("Address not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Failed to get address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}
}
