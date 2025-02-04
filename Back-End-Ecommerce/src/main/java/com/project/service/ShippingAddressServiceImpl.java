package com.project.service;

import com.project.dto.ShippingAddressDTO;
import com.project.entity.ShippingAddress;
import com.project.entity.User;
import com.project.exception.ResourceConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.ShippingAddressRepository;
import com.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserRepository userRepository;

    public ShippingAddressServiceImpl(ShippingAddressRepository shippingAddressRepository, UserRepository userRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addAddress(Long userId, ShippingAddressDTO shippingAddressDTO) throws ResourceConflictException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        ShippingAddress existingAddress = shippingAddressRepository.findByUserAndStreetAddress(user, shippingAddressDTO.getStreetAddress());
        if (existingAddress != null) {
            throw new ResourceConflictException("ShippingAddress", shippingAddressDTO.getStreetAddress());
        }

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
    public void updateAddress(Long addressId, ShippingAddressDTO shippingAddressDTO) throws ResourceNotFoundException {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingAddress", addressId));

        shippingAddress.setStreetAddress(shippingAddressDTO.getStreetAddress());
        shippingAddress.setCity(shippingAddressDTO.getCity());
        shippingAddress.setState(shippingAddressDTO.getState());
        shippingAddress.setZipCode(shippingAddressDTO.getZipCode());
        shippingAddress.setCountry(shippingAddressDTO.getCountry());

        shippingAddressRepository.save(shippingAddress);
    }

    @Override
    public void deleteAddress(Long addressId) throws ResourceNotFoundException {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingAddress", addressId));
        shippingAddressRepository.delete(shippingAddress);
    }

    @Override
    public List<ShippingAddressDTO> getAllAddressForUser(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        List<ShippingAddress> shippingAddresses = shippingAddressRepository.findByUser(user);

        return shippingAddresses.stream()
                .map(this::convertToDTO)
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
    public ShippingAddressDTO getAddressById(Long addressId) throws ResourceNotFoundException {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingAddress", addressId));
        return convertToDTO(shippingAddress);
    }

    @Override
    public boolean isAddressOwnedByUser(Long addressId, Long userId) {
        return shippingAddressRepository.existsByIdAndUserId(addressId, userId);
    }
}