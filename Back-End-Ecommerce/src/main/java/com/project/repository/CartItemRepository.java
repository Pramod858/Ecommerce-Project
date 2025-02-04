package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.entity.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

	List<CartItem> findAllByCart(Cart cart);

}
