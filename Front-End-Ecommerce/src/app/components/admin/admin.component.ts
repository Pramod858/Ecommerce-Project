import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';
import * as bootstrap from 'bootstrap';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { Category } from '../../models/category.model';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  isEditMode: boolean = false;
  currentSection: String = 'categories';

  users: User[] = [];
  userForm: FormGroup;
  selectedUser: User | null = null;

  categories: Category[] = [];
  categoryForm: FormGroup;
  selectedCategory: Category | null = null;
  
  products: Product[] = [];
  productForm: FormGroup;
  selectedProduct: Product | null = null;

  constructor(private fb: FormBuilder, private productService: ProductService, private userService: UserService, private categoryService: CategoryService) {
    this.userForm = this.fb.group({
      id: [null],
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]]
    });
    this.categoryForm = this.fb.group({
      id: [null],
      name: ['', [Validators.required, Validators.minLength(3)]]
    });
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      stock: [0, [Validators.required, Validators.min(1)]],
      imageUrl: ['', [Validators.required, Validators.pattern('https?://.+')]],
      categoryId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
    this.loadCategories();
    this.loadProducts();
  }

  navigateTo(section: string) {
    console.log("Navigating to:", section);
    this.currentSection = section;
  }

  // --------------------------- Use Management Section ---------------------------

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (data) => {
        console.log("Users data:", data);
        this.users = data;
      }, 
      error: (error) => {
        console.error("Error loading users:", error);
      }
    });
  }

  openEditUserModal(user: User): void {
    this.isEditMode = true;
    this.selectedUser = user;
    this.userForm.patchValue(user);

    const modalElement = document.getElementById('editUserModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    } else {
      console.log('Modal element not found');
    }
  }

  updateUser(): void {
    if (this.userForm.invalid) return;
    const userData = this.userForm.value;

    if (this.isEditMode && this.selectedUser) {
      this.userService.updateUser(this.selectedUser.id, userData).subscribe({
        next: () => {
          console.log("User updated successfully");
          this.userForm.reset();
          const modalElement = document.getElementById('editUserModal');
          if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal?.hide();
          }
          this.loadUsers();
        },
        error: (error) => {
          console.error("Error updating user:", error);
        }
      });
    }
  }

  deleteUser(id: number): void {
    if (!confirm("Are you sure you want to delete this user?")) return;
    this.userService.deleteUser(id).subscribe({
      next: () => {
        console.log("User deleted successfully");
        this.loadUsers();
      },
      error: (error) => {
        console.error("Error deleting user:", error);
      }
    });
  }


  // --------------------------- Product Management Section ---------------------------
  loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
          console.log("Products data:", data);
        this.products = data;
      },
      error: (error) => {
        console.error("Error loading products:", error);
      }
    });
  }

  openAddProductModal(): void {
    this.isEditMode = false;
    this.productForm.reset();
    this.selectedProduct = null;
    this.loadCategories();

    const modalElement = document.getElementById('addProductModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    } else {
      console.log('Modal element not found');
    }
  }

  openEditProductModal(product: Product): void {
    this.isEditMode = true;
    this.selectedProduct = product;
    this.productForm.patchValue(product);

    const modalElement = document.getElementById('addProductModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    } else {
      console.error('Modal element not found');
    }
  }

  saveProduct(): void {
    if (this.productForm.invalid) return;
    const productData = this.productForm.value;
    const categoryId = productData.categoryId;

    if (this.isEditMode && this.selectedProduct) {
      this.productService.updateProduct(this.selectedProduct.id, productData).subscribe({
        next: () => {
          console.log("Product updated successfully");
          this.productForm.reset();
          const modalElement = document.getElementById('addProductModal');
          if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal?.hide();
          }
          this.loadProducts();
        },
        error: (error) => {
          console.error("Error updating product:", error);
        }
      });
    } else {
      this.productService.addProduct(categoryId, productData).subscribe({
        next: () => {
          console.log("Product added successfully");
          this.productForm.reset();
          const modalElement = document.getElementById('addProductModal');
          if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal?.hide();
          }
          this.loadProducts();
        },
        error: (error) => {
          console.error("Error adding product:", error);
        }
      });
    }
  }

  deleteProduct(id: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe(() => {
        this.loadProducts();
      });
    }
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
      this.loadProducts();
    }
  }

  

  // --------------------------- Category Management Section ---------------------------

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

  openAddCategoryModal(): void {
    this.isEditMode = false;
    this.categoryForm.reset();

    const modalElement = document.getElementById('addCategoryModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    } else {
      console.log('Modal element not found');
    }
  }

  openEditCategoryModal(category: Category): void {
    this.isEditMode = true;
    this.selectedCategory = category;
    this.categoryForm.patchValue(category);

    const modalElement = document.getElementById('addCategoryModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    } else {
      console.error('Modal element not found');
    }
  }

  saveCategory(): void {
    if (this.categoryForm.invalid) return;
    const categoryData = this.categoryForm.value;

    if (this.isEditMode && this.selectedCategory) {
      this.categoryService.updateCategory(categoryData.id, categoryData).subscribe({
        next: () => {
          console.log("Category updated successfully");
          this.categoryForm.reset();
          const modalElement = document.getElementById('addCategoryModal');
          if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal?.hide();
          }
          this.loadCategories();
        },
        error: (error) => {
          console.error("Error updating category:", error);
        }
      });
    } else {
      this.categoryService.addCategory(categoryData).subscribe({
        next: () => {
          console.log("Category added successfully");
          this.categoryForm.reset();
          const modalElement = document.getElementById('addCategoryModal');
          if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal?.hide();
          }
          this.loadCategories();
        },
        error: (error) => {
          console.error("Error adding category:", error);
        }
      });
    }
  }

  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => {
          this.loadCategories(); 
        },
        error: (error) => {
          console.error('Error deleting category:', error);
        }
      });
    }
  }
  
  onCategoryChange(event: any) {
    const categoryId = event.target.value;
    this.getProductsByCategory(categoryId);
  }
}