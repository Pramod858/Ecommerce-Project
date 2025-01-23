import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {

  baseUrl = "http://localhost:8080/api/users";

  constructor(private http: HttpClient) {}

  checkEmailExists(email: string): Observable<boolean> {
    return this.http.get<boolean>(`/api/users/check-email?email=${email}`);
  }

  checkPhoneExists(phone: string): Observable<boolean> {
    return this.http.get<boolean>(`/api/users/check-phone?phone=${phone}`);
  }

  getUsers(): Observable<User[]> {
      return this.http.get<User[]>(`${this.baseUrl}`);
  }

  updateUser(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/${id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
