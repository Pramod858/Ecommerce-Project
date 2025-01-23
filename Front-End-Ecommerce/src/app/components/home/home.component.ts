import { Component } from '@angular/core';
import { Product } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { Category } from '../../models/category.model';
import { CategoryService } from '../../services/category.service';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: false,
  
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  products: Product[] = [];
  categories: Category[] = [];
  token: string | null; // Get JWT token from localStorage

  constructor(private productService: ProductService, private categoryService: CategoryService, private cartService: CartService, private router: Router) { 
    this.token = localStorage.getItem('token');
  }

  ngOnInit(): void {
    this.fetchProducts();
    this.loadCategories();
  }

  fetchProducts() {
    this.productService.getProducts().subscribe({
      next: (data: Product[]) => {
        this.products = data;
      },
      error: (error) => {
        console.error('Error fetching products:', error);
      }
    });
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        console.log("Categories data:", data);
        this.categories = data;
      },
      error: (error) => {
        console.error("Error loading categories:", error);
      }
    });
  }

  getProductsByCategory(categoryId: number) {
    if (categoryId) {
      this.productService.getProductsByCategory(categoryId).subscribe({
        next: (data) => {
          console.log("Products by category:", data);
          this.products = data;
        },
        error: (error) => {
          console.error("Error loading products by category:", error);
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

        if (error.status === 401) {  // Unauthorized (token expired)
          alert('Your session has expired. Please log in again.');
          this.logout();
        } else {
          alert(error.error?.message || 'Please try again.');
        }
      }
    });
  }

  logout() {
    localStorage.removeItem('token'); 
    this.token = null; 
    this.router.navigate(['/login']);  
  }

  
  
}
