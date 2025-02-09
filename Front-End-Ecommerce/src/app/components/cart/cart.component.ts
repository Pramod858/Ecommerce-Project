import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { Cart } from '../../models/cart.model';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { ApiResponse } from '../../models/api.response';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-cart',
  standalone: false,
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']  // Corrected property name
})
export class CartComponent {
  cart: Cart = { id: 0, cartItems: [], totalPrice: 0 };
  token: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private cartService: CartService, 
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    // Only access localStorage if running in the browser
    if (isPlatformBrowser(this.platformId)) {
      this.token = localStorage.getItem('token');
    }
  }

  ngOnInit(): void {
    if (!this.token) {
      if (isPlatformBrowser(this.platformId)) {
        alert('Please log in to view the cart.');
      }
      this.router.navigate(['/sign-in']);
      return;
    }
    this.getCart();
  }

  getCart() {
    if (!this.token) {
      if (isPlatformBrowser(this.platformId)) {
        alert('Please log in to view the cart.');
      }
      this.router.navigate(['/sign-in']);
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
    // Guard the localStorage access in logout as well
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
    }
    this.router.navigate(['/sign-in']);
  }
}
