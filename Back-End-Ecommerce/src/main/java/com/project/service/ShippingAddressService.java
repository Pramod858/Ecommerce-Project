package com.project.service;

import java.util.List;

import com.project.dto.ShippingAddressDTO;

import jakarta.validation.Valid;

public interface ShippingAddressService {

	void addAddress(Long id, @Valid ShippingAddressDTO shippingAddressDTO);

	boolean isAddressOwnedByUser(Long addressId, Long id);

	void updateAddress(Long addressId, @Valid ShippingAddressDTO shippingAddressDTO);

	void deleteAddress(Long addressId);

	List<ShippingAddressDTO> getAllAddressForUser(Long id);

	ShippingAddressDTO getAddressById(Long addressId);

}
