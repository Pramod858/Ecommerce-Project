import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { CategoryService } from '../../services/category.service';
import { Router } from '@angular/router';
import { ApiResponse } from '../../models/api.response';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']  // Corrected to plural "styleUrls"
})
export class HomeComponent {
  products: Product[] = [];
  categories: Category[] = [];
  token: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private productService: ProductService, 
    private categoryService: CategoryService, 
    private cartService: CartService, 
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { 
    // Only access localStorage if running in the browser
    if (isPlatformBrowser(this.platformId)) {
      this.token = localStorage.getItem('token');
    }
  }

  ngOnInit(): void {
    this.fetchProducts();
    this.loadCategories();
  }

  fetchProducts() {
    this.productService.getProducts().subscribe({
      next: (response: ApiResponse<Product[]>) => {
        if (response.status === true && response.data) {
          this.products = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching products.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching products.';
        console.error('Error fetching products:', error);
      }
    });
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (response: ApiResponse<Category[]>) => {
        if (response.status === true && response.data) {
          this.categories = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching categories.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching categories.';
        console.error('Error fetching categories:', error);
      }
    });
  }

  getProductsByCategory(categoryId: number) {
    if (categoryId) {
      this.productService.getProductsByCategory(categoryId).subscribe({
        next: (response: ApiResponse<Product[]>) => {
          if (response.status === true && response.data) {
            this.products = response.data;
          } else {
            this.errorMessage = response.message || 'An error occurred while fetching products.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while fetching products.';
          console.error('Error fetching products:', error);
        }
      });
    } else {
      this.fetchProducts();
    }
  }

  onCategoryChange(event: any) {
    const categoryId = event.target.value;
    this.getProductsByCategory(categoryId);
  }

  addToCart(productId: number, quantity: number) {
    if (!this.token) {
      alert('Please log in to add products to the cart.');
      this.router.navigate(['/sign-in']);
      return;
    }
    this.cartService.addProductToCart(productId, quantity, this.token).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status === true) {
          alert('Product added to cart successfully!');
        } else {
          this.errorMessage = response.message || 'An error occurred while adding the product to the cart.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while adding the product to the cart.';
        console.error('Error adding product to cart:', error);
      }
    });
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
    }
    this.token = null;
    this.router.navigate(['/sign-in']);
  }
}
