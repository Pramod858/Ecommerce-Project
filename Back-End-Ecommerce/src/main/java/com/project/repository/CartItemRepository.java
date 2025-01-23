package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.entity.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	CartItem findByCartAndProduct(Cart cart, Product product);

}
