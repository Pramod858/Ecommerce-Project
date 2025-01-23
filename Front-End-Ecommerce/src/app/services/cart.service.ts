import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Cart } from '../models/cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private baseUrl = "http://localhost:8080/api/carts"

  constructor(private http: HttpClient) { }

  private getAuthHeaders(token: string) {
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    };
  }

  getCart(token: string): Observable<Cart> {
    return this.http.get<Cart>(`${this.baseUrl}/my-cart`, this.getAuthHeaders(token));
  }

  addProductToCart(productId: number, quantity: number, token: string): Observable<Cart> {
    return this.http.post<Cart>(`${this.baseUrl}/product/${productId}/${quantity}`, null, this.getAuthHeaders(token));
  }

  removeProductFromCart(productId: number, token: string): Observable<Cart> {
    return this.http.delete<Cart>(`${this.baseUrl}/my-cart/product/${productId}`, this.getAuthHeaders(token));
  }

  clearCart(token: string): Observable<Cart> {
    return this.http.delete<Cart>(`${this.baseUrl}/my-cart/clear`, this.getAuthHeaders(token));
  }
}
