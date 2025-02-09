import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { Address } from '../../models/address.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddressService } from '../../services/address.service';
import { ApiResponse } from '../../models/api.response';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-address',
  standalone: false,
  templateUrl: './address.component.html',
  styleUrl: './address.component.css'
})
export class AddressComponent {
  addresses: Address[] = [];
  selectedAddress: Address | null = null;
  token: string | null = null;

  addressForm: FormGroup;
  isEditMode: boolean = false;
  currentAddressId: number | null = null;
  errorMessage: string | null = null;

  constructor(
    private addressService: AddressService, 
    private fb: FormBuilder,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (isPlatformBrowser(this.platformId)) {
          this.token = localStorage.getItem('token');
        }
    this.addressForm = this.fb.group({
      streetAddress: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      zipCode: ['', Validators.required],
      country: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadAddresses();
  }

  loadAddresses(): void {
    if (!this.token) {
      if (isPlatformBrowser(this.platformId)) {
        alert('Please log in to view the cart.');
      }
      return;
    }
    this.addressService.getAllAddressesForUser(this.token).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status === true && response.data) {
          this.addresses = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching addresses.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching addresses.';
        console.error("Error fetching addresses:", error);
      }
    });
  }

  openAddAddressModal(): void {
    this.isEditMode = false;
    this.addressForm.reset();
    const modal = document.getElementById('addAddressModal');
    if (modal) {
      modal.classList.add('show');
      modal.style.display = 'block';
    }
  }

  openEditAddressModal(address: Address): void {
    this.isEditMode = true;
    this.currentAddressId = address.id;
    this.addressForm.patchValue(address);

    const modal = document.getElementById('editAddressModal');
    if (modal) {
      modal.classList.add('show');
      modal.style.display = 'block';
    }
  }

  closeModal(): void {
    const addModal = document.getElementById('addAddressModal');
    const editModal = document.getElementById('editAddressModal');
    if (addModal) {
      addModal.classList.remove('show');
      addModal.style.display = 'none';
    }
    if (editModal) {
      editModal.classList.remove('show');
      editModal.style.display = 'none';
    }
  }

  saveAddress(): void {
    if (this.addressForm.valid && this.token) {
      const addressData: Address = this.addressForm.value;
      if (this.isEditMode && this.currentAddressId) {
        this.addressService.updateAddress(this.currentAddressId, addressData, this.token).subscribe({
          next: (response: ApiResponse<any>) => {
            if (response.status === true) {
              this.closeModal();
              this.loadAddresses();
            } else {
              this.errorMessage = response.message || 'An error occurred while updating the address.';
            }
          },
          error: (error: any) => {
            this.errorMessage = error.message || 'An error occurred while updating the address.';
            console.error("Error updating address:", error);
          }
        });
      } else {
        this.addressService.addAddress(addressData, this.token).subscribe({
          next: (response: ApiResponse<any>) => {
            if (response.status === true) {
              this.closeModal();
              this.loadAddresses();
            } else {
              this.errorMessage = response.message || 'An error occurred while adding the address.';
            }
          },
          error: (error: any) => {
            this.errorMessage = error.message || 'An error occurred while adding the address.';
            console.error("Error adding address:", error);
          }
        });        
      }
    }
  }

  deleteAddress(addressId: number): void {
    if (!confirm('Are you sure you want to delete this address?')) return;
    if (!this.token) {
      if (isPlatformBrowser(this.platformId)) {
        alert('Please log in to view the cart.');
      }
      return;
    }
    this.addressService.deleteAddress(addressId, this.token).subscribe({
      next: (response: ApiResponse<any>) => { 
        if (response.status === true) {
          this.loadAddresses();
        } else {
          this.errorMessage = response.message || 'An error occurred while deleting the address.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while deleting the address.';
        console.error("Error deleting address:", error);
      }
    });
  }
}
