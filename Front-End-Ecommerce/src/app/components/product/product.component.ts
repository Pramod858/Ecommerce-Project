import { Component } from '@angular/core';
import { Product } from '../../models/product.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { ApiResponse } from '../../models/api.response';
import { error } from 'jquery';

@Component({
  selector: 'app-product',
  standalone: false,
  
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent {

  product!: Product;
  quantity: number = 1;
  token: string | null;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private router: Router
  ) {
    this.token = localStorage.getItem('token');
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
      alert('Please log in to add products to the cart.');
      this.router.navigate(['/login']);
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
