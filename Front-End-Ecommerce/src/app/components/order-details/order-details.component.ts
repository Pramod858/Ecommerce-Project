import { Component } from '@angular/core';
import { Order } from '../../models/order.model';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { ApiResponse } from '../../models/api.response';
import { error } from 'jquery';

@Component({
  selector: 'app-order-details',
  standalone: false,
  
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent {
  order: Order | null = null;
  errorMessage: string | null = null;

  constructor(private orderService: OrderService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const orderId = Number(this.route.snapshot.paramMap.get('id'));
    if (orderId) {
      this.orderService.getOrderById(orderId).subscribe({
        next: (response: ApiResponse<Order>) => {
          if (response.status === true && response.data) {
            this.order = response.data;
          } else {
            this.errorMessage = response.message || 'Order not found';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred';
          console.error('Error fetching order details:', error);
    
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/orders']);
  }
}
