import { Component } from '@angular/core';
import { Order } from '../../models/order.model';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-details',
  standalone: false,
  
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent {
  order: Order | null = null;

  constructor(private orderService: OrderService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const orderId = Number(this.route.snapshot.paramMap.get('id'));
    if (orderId) {
      this.orderService.getOrderById(orderId).subscribe({
        next: (order) => {
          this.order = order;
        },
        error: (error) => {
          console.error('Error fetching order details:', error);
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/orders']);
  }
}
