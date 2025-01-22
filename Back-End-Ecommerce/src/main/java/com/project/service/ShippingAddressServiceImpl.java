package com.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ShippingAddressDTO;
import com.project.entity.Order;
import com.project.entity.ShippingAddress;
import com.project.entity.User;
import com.project.exception.OrderNotFoundException;
import com.project.exception.ShippingAddressNotFoundException;
import com.project.exception.UserNotFoundException;
import com.project.repository.OrderRepository;
import com.project.repository.ShippingAddressRepository;
import com.project.repository.UserRepository;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {
    
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void addAddress(Long userId, ShippingAddressDTO shippingAddressDTO)
            throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));


        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setUser(user);
        shippingAddress.setStreetAddress(shippingAddressDTO.getStreetAddress());
        shippingAddress.setCity(shippingAddressDTO.getCity());
        shippingAddress.setState(shippingAddressDTO.getState());
        shippingAddress.setZipCode(shippingAddressDTO.getZipCode());
        shippingAddress.setCountry(shippingAddressDTO.getCountry());

        shippingAddressRepository.save(shippingAddress);
    }

    @Override
    public void updateAddress(Long addressId, ShippingAddressDTO shippingAddressDTO) throws ShippingAddressNotFoundException {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new ShippingAddressNotFoundException("Shipping address not found with id: " + addressId));

        if (shippingAddressDTO.getStreetAddress() != null) {
            shippingAddress.setStreetAddress(shippingAddressDTO.getStreetAddress());
        }
        if (shippingAddressDTO.getCity() != null) {
            shippingAddress.setCity(shippingAddressDTO.getCity());
        }
        if (shippingAddressDTO.getState() != null) {
            shippingAddress.setState(shippingAddressDTO.getState());
        }
        if (shippingAddressDTO.getZipCode() != null) {
            shippingAddress.setZipCode(shippingAddressDTO.getZipCode());
        }
        if (shippingAddressDTO.getCountry() != null) {
            shippingAddress.setCountry(shippingAddressDTO.getCountry());
        }

        shippingAddressRepository.save(shippingAddress);
    }

    @Override
    public void deleteAddress(Long addressId) throws ShippingAddressNotFoundException {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new ShippingAddressNotFoundException("Shipping address not found with id: " + addressId));
        shippingAddressRepository.delete(shippingAddress);
    }

    @Override
    public List<ShippingAddressDTO> getAllAddressForUser(Long userId) throws UserNotFoundException {
        // Validate if the user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Retrieve and map addresses to DTOs
        return shippingAddressRepository.findByUser(user).stream()
                .map(this::convertToDTO)  // Ensure this line passes ShippingAddress, not User
                .collect(Collectors.toList());
    }


    private ShippingAddressDTO convertToDTO(ShippingAddress shippingAddress) {
        ShippingAddressDTO dto = new ShippingAddressDTO();
        dto.setId(shippingAddress.getId());
        dto.setStreetAddress(shippingAddress.getStreetAddress());
        dto.setCity(shippingAddress.getCity());
        dto.setState(shippingAddress.getState());
        dto.setZipCode(shippingAddress.getZipCode());
        dto.setCountry(shippingAddress.getCountry());
        return dto;
    }

    @Override
    public ShippingAddressDTO getAddressById(Long addressId) throws ShippingAddressNotFoundException {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new ShippingAddressNotFoundException("Address not found with id: " + addressId));
        return convertToDTO(shippingAddress);
    }

	@Override
	public boolean isAddressOwnedByUser(Long addressId, Long id) {
		// TODO Auto-generated method stub
		return shippingAddressRepository.existsByIdAndUserId(addressId, id);
	}


}
