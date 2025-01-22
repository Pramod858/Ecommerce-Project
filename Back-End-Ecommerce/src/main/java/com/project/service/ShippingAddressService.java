package com.project.service;

import java.util.List;

import com.project.dto.ShippingAddressDTO;
import com.project.exception.OrderNotFoundException;
import com.project.exception.ShippingAddressNotFoundException;
import com.project.exception.UserNotFoundException;

public interface ShippingAddressService {

	void addAddress(Long userId, ShippingAddressDTO shippingAddressDTO) throws UserNotFoundException;

	void updateAddress(Long addressId, ShippingAddressDTO shippingAddressDTO) throws ShippingAddressNotFoundException;

	void deleteAddress(Long addressId) throws ShippingAddressNotFoundException;

	List<ShippingAddressDTO> getAllAddressForUser(Long userId) throws UserNotFoundException;

	ShippingAddressDTO getAddressById(Long addressId) throws ShippingAddressNotFoundException;

	boolean isAddressOwnedByUser(Long addressId, Long id);

}
