package com.project.service;

import com.project.dto.UserDTO;
import com.project.entity.User;
import com.project.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

	void saveUser(UserDTO userDTO);

	Optional<User> findByEmail(String email);

	List<UserDTO> getAllUsers();

	UserDTO getUserById(Long userId) throws UserNotFoundException;

	void updateUser(UserDTO userDTO, Long userId) throws UserNotFoundException;

	void deleteUser(Long userId) throws UserNotFoundException;

	boolean emailExists(String email);

	boolean phoneExists(String phone);
}
