package com.project.service;

import com.project.dto.UserDTO;
import com.project.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

	void saveUser(UserDTO userDTO);

	Optional<User> findByEmail(String email);

	List<UserDTO> getAllUsers();

	UserDTO getUserById(Long userId);

	void updateUser(UserDTO userDTO, Long userId);

	void deleteUser(Long userId);

}
