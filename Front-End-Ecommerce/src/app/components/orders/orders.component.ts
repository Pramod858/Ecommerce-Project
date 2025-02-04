import { Component } from '@angular/core';
import { Order } from '../../models/order.model';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';
import { ApiResponse } from '../../models/api.response';

@Component({
  selector: 'app-orders',
  standalone: false,
  
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {
  orders: Order[] = []
  token: string | null;
  errorMessage: string | null = null;

  constructor(private orderService: OrderService, private router: Router) {
    this.token = localStorage.getItem('token');
  }

  ngOnInit(): void {
    if (!this.token) {
      alert('Please login first');
      this.router.navigate(['/login']);
      return
    }
    this.getOrders();
  }

  getOrders() {
    if (!this.token) {
      alert('Please login first');
      this.router.navigate(['/login']);
      return
    }
    this.orderService.getOrders(this.token).subscribe({
      next: (response: ApiResponse<Order[]>) => {
        if (response.status === true && response.data) {
          this.orders = response.data
        } else {
          this.errorMessage = response.message || 'Failed to fetch orders';
        }
      },
      error: (error) => {
        this.errorMessage = error.message || 'Failed to fetch orders';
        console.error('Error fetching orders:', error);
      } 
    })
  }

}
