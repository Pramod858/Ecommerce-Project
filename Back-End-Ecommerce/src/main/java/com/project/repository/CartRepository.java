package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

}
