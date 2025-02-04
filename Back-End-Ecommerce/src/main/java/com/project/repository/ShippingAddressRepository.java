package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entity.ShippingAddress;
import com.project.entity.User;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {

	ShippingAddress findByUserAndStreetAddress(User user, String streetAddress);

	List<ShippingAddress> findByUser(User user);

	boolean existsByIdAndUserId(Long addressId, Long userId);
}
