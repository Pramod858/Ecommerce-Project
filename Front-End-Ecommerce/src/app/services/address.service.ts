import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Address } from '../models/address.model';

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private baseUrl = "http://localhost:8080/api/addresses";

  constructor(private http: HttpClient) { }

  private getAuthHeaders(token: string) {
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    };
  }

  addAddress(address: Address, token: string): Observable<Address> {
    return this.http.post<Address>(`${this.baseUrl}/add-address`, address, this.getAuthHeaders(token));
  }

  updateAddress(addressId: number, address: Address, token: string): Observable<Address> {
    return this.http.put<Address>(`${this.baseUrl}/update-address/${addressId}`, address, this.getAuthHeaders(token));
  }

  deleteAddress(addressId: number, token: string): Observable<Address> {
    return this.http.delete<Address>(`${this.baseUrl}/delete-address/${addressId}`, this.getAuthHeaders(token));
  }

  getAllAddressesForUser(token: string): Observable<Address[]> {
    return this.http.get<Address[]>(`${this.baseUrl}/my-addresses`, this.getAuthHeaders(token));
  }

  getAddressById(addressId: number, token: string): Observable<Address> {
    return this.http.get<Address>(`${this.baseUrl}/my-address/${addressId}`, this.getAuthHeaders(token));
  }
}
