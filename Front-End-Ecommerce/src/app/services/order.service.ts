import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Payment } from '../models/payment.model';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api.response';
import { Order } from '../models/order.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private baseUrl = `${environment.apiUrl}/api/orders`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(token: string) {
    return {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    };
  }

  createOrder(token: string, addressId: number, payment: Payment): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/place-order/${addressId}`, payment, this.getAuthHeaders(token));
  }

  getAllOrders(): Observable<ApiResponse<Order[]>> {
    return this.http.get<ApiResponse<Order[]>>(`${this.baseUrl}`);
  }

  getOrders(token: string): Observable<ApiResponse<Order[]>> {
    return this.http.get<ApiResponse<Order[]>>(`${this.baseUrl}/my-orders`, this.getAuthHeaders(token));
  }
  
  getOrderById(orderId: number): Observable<ApiResponse<Order>> {
    return this.http.get<ApiResponse<Order>>(`${this.baseUrl}/my-order/${orderId}`);
  }

  updateOrderStatus(orderId: number, status: string): Observable<ApiResponse<any>> {
    const body = { status };
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/${orderId}/status`, body, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  deleteOrder(orderId: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/delete-order/${orderId}`);
  }
}
