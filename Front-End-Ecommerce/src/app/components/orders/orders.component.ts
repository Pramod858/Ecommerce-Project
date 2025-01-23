import { Component } from '@angular/core';
import { Order } from '../../models/order.model';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-orders',
  standalone: false,
  
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {
  orders: Order[] = []
  token: string | null;

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
      next: (data) => {
        this.orders = data;
      },
      error: (error) => {
        console.error('Error fetching orders:', error);
        if (error.status === 401) {
          alert('Session expired. Please log in again.');
          this.router.navigate(['/login']);
        } else {
          alert('Failed to fetch orders. Try again.');
        }
      }
    })
  }

}
