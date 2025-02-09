import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from '../models/api.response';
import { Category } from '../models/category.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private baseUrl = `${environment.apiUrl}/api/categories`;

  constructor(private http: HttpClient) { }

  getCategories(): Observable<ApiResponse<Category[]>> {
    return this.http.get<ApiResponse<Category[]>>(`${this.baseUrl}`);
  }
  addCategory(category: Category): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(this.baseUrl, category);
  }

  updateCategory(categoryId: number, category: Category): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/${categoryId}`, category);
  }

  deleteCategory(categoryId: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/${categoryId}`);
  }
}
