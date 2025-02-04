import { Component, OnInit } from '@angular/core';
import { Cart } from '../../models/cart.model';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { AddressService } from '../../services/address.service';
import { Address } from '../../models/address.model';
import { OrderService } from '../../services/order.service';
import { Payment } from '../../models/payment.model';
import { PaymentStatus } from '../../models/payment-status.enum';
import { User } from '../../models/user.model';
import { ApiResponse } from '../../models/api.response';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  cart: Cart = { id: 0, cartItems: [], totalPrice: 0 };
  addresses: Address[] = [];
  selectedAddress: Address | null = null;
  token: string | null;
  selectedPaymentMethod: string = 'cashOnDelivery'; // Default payment method
  user: User | null = null;  // Store user data
  errorMessage: string | null = null;

  cardNumber: string = '';
  cardCVV: string = '';
  cardExpiry: string = '';
  cardholderName: string = '';

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
    this.getUser(); // Load user info
  }

  redirectToAddAddress() {
    this.router.navigate(['/addresses']); // Modify the path as per your route for adding address
  }

  // Fetch user details
  getUser(): void {
    // Assume user details are stored in localStorage (Modify if needed)
    const userData = localStorage.getItem('user');
    if (userData) {
      this.user = JSON.parse(userData);
    }
  }

  // Fetch cart details
  getCart(): void {
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
        console.error("Error fetching cart:", error);
      }
    });
  }

  // Load user's saved addresses
  loadAddresses(): void {
    if (!this.token) {
      alert('Please log in to view the cart.');
      this.router.navigate(['/login']);
      return;
    }
    this.addressService.getAllAddressesForUser(this.token).subscribe({
      next: (response: ApiResponse<Address[]>) => {
        if (response.status === true && response.data) {
          this.addresses = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching addresses.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching addresses.';
        console.error("Error fetching addresses:", error);
      }
    });
  }

  onConfirmOrder(): void {
    if (!this.selectedAddress) {
      alert('Please select a shipping address.');
      return;
    }
  
    const addressId = this.selectedAddress.id;
  
    // Prepare the simplified payment data
    const paymentData = {
      paymentMethod: this.selectedPaymentMethod,
      paymentStatus: PaymentStatus.SUCCESS, 
      cardNumber: this.cardNumber,
      cardCVV: this.cardCVV,
      cardExpiry: this.cardExpiry,
      cardholderName: this.cardholderName
    };
  
    // Place the order
    this.placeOrder(addressId, paymentData);
  }
  

  // Place order
  placeOrder(addressId: number, paymentData: any): void {
    if (!this.token) {
      alert('User not authenticated. Please log in.');
      this.router.navigate(['/login']);
      return;
    }

    this.orderService.createOrder(this.token, addressId, paymentData).subscribe({
      next: () => {
        alert('🎉 Order placed successfully!');
        localStorage.removeItem('cart'); // Clear cart after order
        this.router.navigate(['/orders']);
      },
      error: (error) => {
        console.error('❌ Error placing order:', error);
        alert('An error occurred while placing the order. Please try again later.');
      }
    });
  }
}
