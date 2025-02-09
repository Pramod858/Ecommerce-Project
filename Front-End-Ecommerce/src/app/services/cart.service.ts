import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api.response';
import { Cart } from '../models/cart.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private baseUrl = `${environment.apiUrl}/api/carts`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(token: string) {
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    };
  }

  getCart(token: string): Observable<ApiResponse<Cart>> {
    return this.http.get<ApiResponse<Cart>>(`${this.baseUrl}/my-cart`, this.getAuthHeaders(token));
  }

  addProductToCart(productId: number, quantity: number, token: string): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/product/${productId}/${quantity}`, null, this.getAuthHeaders(token));
  }

  removeProductFromCart(productId: number, token: string): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/my-cart/product/${productId}`, this.getAuthHeaders(token));
  }

  clearCart(token: string): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/my-cart/clear`, this.getAuthHeaders(token));
  }
}
