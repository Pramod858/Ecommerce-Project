import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from '../models/api.response';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = `${environment.apiUrl}/api/products`;

  constructor(private http: HttpClient) { }

  getProducts(): Observable<ApiResponse<Product[]>> { 
    return this.http.get<ApiResponse<Product[]>>(this.baseUrl); 
  }

  getProductById(id: number): Observable<ApiResponse<Product>> {
    return this.http.get<ApiResponse<Product>>(`${this.baseUrl}/${id}`);
  }

  addProduct(categoryId: number, product: Product): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/category/${categoryId}/product`, product);
  }

  updateProduct(id: number, product: Product): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/${id}`, product);
  }

  deleteProduct(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/${id}`);
  }

  getProductsByCategory(categoryId: number): Observable<ApiResponse<Product[]>> {
    return this.http.get<ApiResponse<Product[]>>(`${this.baseUrl}/category/${categoryId}/products`);
  }
}
