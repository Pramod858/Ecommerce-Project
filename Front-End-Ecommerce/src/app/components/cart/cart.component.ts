import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { CartItem } from '../../models/cartItem.model';
import { Cart } from '../../models/cart.model';
import { ApiResponse } from '../../models/api.response';
import { error } from 'jquery';

@Component({
  selector: 'app-cart',
  standalone: false,
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: Cart = { id: 0, cartItems: [], totalPrice: 0 };
  token: string | null;
  errorMessage: string | null = null;

  constructor(private cartService: CartService, private router: Router) {
    this.token = localStorage.getItem('token');
  }

  ngOnInit(): void {
    if (!this.token) {
      alert('Please log in to view the cart.');
      this.router.navigate(['/login']);
      return;
    }
    this.getCart();
  }

  getCart() {
    if (!this.token) {
      alert('Please log in to view the cart.');
      this.router.navigate(['/login']);
      return;
    }
  
    this.cartService.getCart(this.token).subscribe({
      next: (response: ApiResponse<Cart>) => {
        if (response.status === true && response.data) {
          this.cart = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching the cart.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching the cart.';
        console.error('Error fetching cart:', error);
      }
    });
  }
  

  removeProductFromCart(productId: number) {
    if (!this.token) return;
    this.cartService.removeProductFromCart(productId, this.token).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status === true) {
          this.getCart();
        } else {
          this.errorMessage = response.message || 'An error occurred while removing the product from the cart.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while removing the product from the cart.';
        console.error('Error removing product from cart:', error);
      }
    });
  }

  clearCart() {
    if (!this.token) return;
    this.cartService.clearCart(this.token).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status === true) {
          this.getCart();
        } else {
          this.errorMessage = response.message || 'An error occurred while clearing the cart.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while clearing the cart.';
        console.error('Error clearing cart:', error);
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }  
}
