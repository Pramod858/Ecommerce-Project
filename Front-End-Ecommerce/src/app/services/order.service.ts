import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../models/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private baseUrl = "http://localhost:8080/api/orders"

  constructor(private http: HttpClient) { }

  private getAuthHeaders(token: string) {
    return {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    };
  }

  placeOrder(addressId: number, token: string): Observable<Order> {
    return this.http.post<Order>(`${this.baseUrl}/place-order/${addressId}`, this.getAuthHeaders(token));
  }

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}`);
  }

  getOrders(token: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}/my-orders`, this.getAuthHeaders(token));
  }
  
  getOrderById(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.baseUrl}/my-order/${orderId}`);
  }
}
