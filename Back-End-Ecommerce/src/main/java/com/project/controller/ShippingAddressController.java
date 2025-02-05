package com.project.controller;

import com.project.dto.ShippingAddressDTO;
import com.project.entity.ApiResponse;
import com.project.entity.User;
import com.project.exception.ResourceConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.service.AuthService;
import com.project.service.ShippingAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;
    private final AuthService authService;

    public ShippingAddressController(ShippingAddressService shippingAddressService, AuthService authService) {
        this.shippingAddressService = shippingAddressService;
        this.authService = authService;
    }

    @PostMapping("/add-address")
    public ResponseEntity<ApiResponse<Void>> addAddress(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid ShippingAddressDTO shippingAddressDTO) {
        try {
            User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            shippingAddressService.addAddress(user.getId(), shippingAddressDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Address added successfully!"));
        } catch (ResourceConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to add address: " + e.getMessage()));
        }
    }

    @PutMapping("/update-address/{addressId}")
    public ResponseEntity<ApiResponse<Void>> updateAddress(
            @RequestHeader("Authorization") String token,
            @PathVariable Long addressId,
            @RequestBody @Valid ShippingAddressDTO shippingAddressDTO) {
        try {
            User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            if (!shippingAddressService.isAddressOwnedByUser(addressId, user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "You are not authorized to update this address"));
            }
            shippingAddressService.updateAddress(addressId, shippingAddressDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Address updated successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to update address: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete-address/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @RequestHeader("Authorization") String token,
            @PathVariable Long addressId) {
        try {
            User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            if (!shippingAddressService.isAddressOwnedByUser(addressId, user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "You are not authorized to delete this address"));
            }
            shippingAddressService.deleteAddress(addressId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Address deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to delete address: " + e.getMessage()));
        }
    }

    @GetMapping("/my-addresses")
    public ResponseEntity<ApiResponse<List<ShippingAddressDTO>>> getAllAddressForUser(
            @RequestHeader("Authorization") String token) {
        try {
            User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token", null));
            }
            List<ShippingAddressDTO> addresses = shippingAddressService.getAllAddressForUser(user.getId());
            return ResponseEntity.ok(new ApiResponse<>(true, "Addresses retrieved successfully!", addresses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to retrieve addresses: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<ShippingAddressDTO>> getAddressById(
            @PathVariable Long addressId) {
        try {
            ShippingAddressDTO address = shippingAddressService.getAddressById(addressId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Address retrieved successfully!", address));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to get address: " + e.getMessage()));
        }
    }
}