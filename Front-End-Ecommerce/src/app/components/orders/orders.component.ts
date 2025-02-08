import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { Order } from '../../models/order.model';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { ApiResponse } from '../../models/api.response';

@Component({
  selector: 'app-orders',
  standalone: false,
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {
  orders: Order[] = [];
  token: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private orderService: OrderService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    // Only access localStorage in a browser environment.
    if (isPlatformBrowser(this.platformId)) {
      this.token = localStorage.getItem('token');
    }
  }

  ngOnInit(): void {
    if (!this.token) {
      if (isPlatformBrowser(this.platformId)) {
        alert('Please login first');
      }
      this.router.navigate(['/sign-in']);
      return;
    }
    this.getOrders();
  }

  getOrders(): void {
    if (!this.token) {
      if (isPlatformBrowser(this.platformId)) {
        alert('Please login first');
      }
      this.router.navigate(['/sign-in']);
      return;
    }
    
    this.orderService.getOrders(this.token).subscribe({
      next: (response: ApiResponse<Order[]>) => {
        if (response.status === true && response.data) {
          this.orders = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to fetch orders';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'Failed to fetch orders';
        console.error('Error fetching orders:', error);
      }
    });
  }
}
