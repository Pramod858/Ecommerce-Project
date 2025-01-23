import { Component } from '@angular/core';
import { Cart } from '../../models/cart.model';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { AddressService } from '../../services/address.service';
import { Address } from '../../models/address.model';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']  // Ensure this is `styleUrls` not `styleUrl`
})
export class CheckoutComponent {
  cart: Cart = { id: 0, cartItems: [], totalPrice: 0 };
  addresses: Address[] = [];
  selectedAddress: Address | null = null;
  token: string | null;
  selectedPaymentMethod: string = 'cashOnDelivery';  // Default to 'cashOnDelivery'

  constructor(
    private cartService: CartService,
    private addressService: AddressService,
    private orderService: OrderService,
    private router: Router
  ) {
    this.token = localStorage.getItem('token'); // Get token from localStorage
  }

  ngOnInit(): void {
    this.getCart();
    this.loadAddresses();
  }

  // Fetch the cart details
  getCart(): void {
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

  // Load the user's saved addresses
  loadAddresses(): void {
    if (this.token) {
      this.addressService.getAllAddressesForUser(this.token).subscribe({
        next: (data: Address[]) => {
          this.addresses = data;
        },
        error: (error: any) => {
          console.error("Error loading addresses:", error);
          alert('Failed to load addresses. Try again later.');
        }
      });
    } else {
      alert('Token is missing. Please log in again.');
      this.router.navigate(['/login']);
    }
  }

  // Handle the order confirmation
  onConfirmOrder(): void {
    if (!this.selectedAddress) {
      alert('Please select a shipping address.');
      return;
    }

    const addressId = this.selectedAddress.id;  // Get the selected address ID

    // Check for payment method (Cash on Delivery or other payment methods)
    if (this.selectedPaymentMethod === 'cashOnDelivery') {
      this.placeOrder(addressId);
    } else {
      alert('Payment method not supported yet.');
    }
  }

  // Call the placeOrder function from OrderService
  placeOrder(addressId: number): void {
    if (!this.token) {
      alert('User not authenticated. Please log in.');
      this.router.navigate(['/login']);
      return;
    }

    this.orderService.placeOrder(addressId, this.token).subscribe({
      next: (response) => {
        // Handle success
        alert('Order placed successfully!');
        this.router.navigate(['/orders']); // Redirect to the orders page or any other page
      },
      error: (error) => {
        // Handle error
        console.error('Error placing order:', error);
        alert('An error occurred while placing the order. Please try again later.');
      }
    });
  }
}
