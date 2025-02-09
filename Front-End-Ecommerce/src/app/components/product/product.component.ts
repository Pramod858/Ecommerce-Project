import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { Product } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { ApiResponse } from '../../models/api.response';
import { ActivatedRoute, Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-product',
  standalone: false,
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent {
  product!: Product;
  quantity: number = 1;
  token: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (isPlatformBrowser(this.platformId)) {
          this.token = localStorage.getItem('token');
        }
  }

  ngOnInit(): void {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    if (productId) {
      this.productService.getProductById(productId).subscribe({
        next: (response: ApiResponse<Product>) => {
          if (response.status === true && response.data) {
            this.product = response.data;
          } else {
            this.errorMessage = response.message || 'An error occurred while fetching product';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.error?.message || 'An error occurred while fetching the product.';
          console.error('Error fetching product:', error);
        }
    });
    }
  }

  addToCart(productId: number, quantity: number) {
    if (!this.token) {
      if (isPlatformBrowser(this.platformId)) {
              alert('Please login first');
            }
      return;
    }
    this.cartService.addProductToCart(productId, quantity,  this.token).subscribe({
      next: (response: any) => {
        alert('Product added to cart successfully!');
        console.log('Product added to cart:', response);
      },
      error: (error) => {
        console.error('Error adding product to cart:', error);

        if (error.status === 401) {  
          alert('Your session has expired. Please log in again.');
          this.logout();
        } else {
          alert(error.error?.message || 'Please try again.');
        }
      }
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userName');
    window.location.reload();
  }
}
