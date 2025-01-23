import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { CartItem } from '../../models/cartItem.model';
import { Cart } from '../../models/cart.model';

@Component({
  selector: 'app-cart',
  standalone: false,
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: Cart = { id: 0, cartItems: [], totalPrice: 0 };
  token: string | null;

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
      next: (data) => {
        this.cart = data;
      },
      error: (error) => {
        console.error('Error fetching cart:', error);
        if (error.status === 401) {
          // Token may be invalid or expired, clear it and redirect
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
        } else {
          alert('Failed to fetch cart. Try again later.');
        }
      }
    });
  }
  

  removeProductFromCart(productId: number) {
    if (!this.token) return;
    this.cartService.removeProductFromCart(productId, this.token).subscribe({
      next: (respons: any) => {
        alert("Product removed from the cart");
        this.getCart()
      },
      error: (error) => {
        console.error('Error removing product:', error);
        if (error.status === 401) {
          alert('Session expired. Please log in again.');
          this.logout();
        } else {
          alert('Failed to remove product. Try again.');
        }
      }
    });
  }

  clearCart() {
    if (!this.token) return;
    this.cartService.clearCart(this.token).subscribe({
      next: (response: any) => { 
        alert("Cart cleared");
        this.getCart()
      },
      error: (error) => {
        console.error('Error clearing cart:', error);
        if (error.status === 401) {
          alert('Session expired. Please log in again.');
          this.logout();
        } else {
          alert('Failed to clear cart. Try again.');
        }
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }  
}
