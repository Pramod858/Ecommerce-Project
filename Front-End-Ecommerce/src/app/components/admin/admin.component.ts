import { Component } from '@angular/core';
import { ApiResponse } from '../../models/api.response';
import { Order } from '../../models/order.model';
import { Category } from '../../models/category.model';
import { Product } from '../../models/product.model';
import { User } from '../../models/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OrderStatus } from '../../models/order-status.enum';
import { Address } from '../../models/address.model';
import { UserService } from '../../services/user.service';
import { CategoryService } from '../../services/category.service';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';
import { AddressService } from '../../services/address.service';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {
  errorMessage: string | null = null;
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

  orders: Order[] = [];
  orderForm: FormGroup;
  selectedOrder: Order | null = null;
  orderStatuses: string[] = Object.values(OrderStatus);

  address: Address | null = null;
  addressId: number | null = null;
  addressModalVisible: boolean = false;

  constructor(private fb: FormBuilder,
    private userService: UserService,
    private categoryService: CategoryService,
    private productService: ProductService, 
    private orderService: OrderService,
    private addressService: AddressService
    ) {
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
    this.orderForm = this.fb.group({
      status: ['', Validators.required]
    })
  }

  ngOnInit(): void {
    this.loadUsers();
    this.loadCategories();
    this.loadProducts();
    this.getAllOrders();
  }

  navigateTo(section: string) {
    console.log("Navigating to:", section);
    this.currentSection = section;
  }

  // --------------------------- Use Management Section ---------------------------

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (response: ApiResponse<User[]>) => {
        if (response.status === true && response.data) {
          console.log(response.message);
          this.users = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching users.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching users.';
        console.error('Error fetching users:', error);
      }
    });
  }

  openEditUserModal(user: User): void {
    this.isEditMode = true;
    this.selectedUser = user;
    this.userForm.patchValue(user);
  
    const modal = document.getElementById('editUserModal');
    if (modal) {
      if (modal) {
        modal.classList.add('show');
        modal.style.display = 'block';
      }
    }
  }

  closeUserModal(): void {
    const editModal = document.getElementById('editUserModal');

    if (editModal) {
      editModal.classList.remove('show');
      editModal.style.display = 'none';
    }
  }
  
  updateUser(): void {
    if (this.userForm.invalid) return;
    const userData = this.userForm.value;
  
    if (this.isEditMode && this.selectedUser) {
      this.userService.updateUser(this.selectedUser.id, userData).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            console.log("User updated successfully");
            this.loadUsers();
            this.closeUserModal();
          } else {
            this.errorMessage = response.message || 'An error occurred while updating the user.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while updating the user.';
          console.error('Error updating user:', error);
        }
      });
    }
  }

  deleteUser(id: number): void {
    if (!confirm("Are you sure you want to delete this user?")) return;
    this.userService.deleteUser(id).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status === true) {
          console.log(response.message);
          this.loadUsers();
        } else {
          this.errorMessage = response.message || 'An error occurred while deleting the user.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while deleting the user.';
        console.error('Error deleting user:', error);
      }
    });
  }


  // --------------------------- Product Management Section ---------------------------
  loadProducts() {
    this.productService.getProducts().subscribe({
      next: (response: ApiResponse<Product[]>) => {
        if (response.status === true && response.data) {
          console.log(response.message);
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

  openAddProductModal(): void {
    this.isEditMode = false;
    this.productForm.reset();
    this.selectedProduct = null;
    this.loadCategories();

    const modalElement = document.getElementById('addProductModal');
    if (modalElement) {
      modalElement.classList.add('show');
      modalElement.style.display = 'block';
    }
  }

  openEditProductModal(product: Product): void {
    this.isEditMode = true;
    this.selectedProduct = product;
    this.productForm.patchValue(product);

    const modalElement = document.getElementById('addProductModal');
    if (modalElement) {
      modalElement.classList.add('show');
      modalElement.style.display = 'block';
    }
  }

  closeProductModal(): void {
    const addModal = document.getElementById('addProductModal');
    const editModal = document.getElementById('editProductModal');
    if (addModal) {
      addModal.classList.remove('show');
      addModal.style.display = 'none';
    }
    if (editModal) {
      editModal.classList.remove('show');
      editModal.style.display = 'none';
    }
  }

  saveProduct(): void {
    if (this.productForm.invalid) return;
    const productData = this.productForm.value;
    const categoryId = productData.categoryId;

    if (this.isEditMode && this.selectedProduct) {
      this.productService.updateProduct(this.selectedProduct.id, productData).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            this.closeProductModal();
            this.loadProducts();
          } else {
            this.errorMessage = response.message || 'An error occurred while updating the product.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while updating the product.'
          console.error("Error updating product:", error);
        }
      });
    } else {
      this.productService.addProduct(categoryId, productData).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            this.closeProductModal();
            this.loadProducts();
          } else {
            this.errorMessage = response.message || 'An error occurred while adding the product.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while adding the product.'
          console.error('Error adding product:', error);
        }
      });
    }
  }

  deleteProduct(id: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            this.loadProducts();
          } else {
            this.errorMessage = response.message || 'An error occurred while deleting the product.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while deleting the product.'
          console.error('Error deleting product:', error);
        }
      });
    }
  }

  getProductsByCategory(categoryId: number) {
    if (categoryId) {
      this.productService.getProductsByCategory(categoryId).subscribe({
        next: (response: ApiResponse<Product[]>) => {
          if (response.status === true && response.data) {
            console.log(response.message);
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
      this.loadProducts();
    }
  }

  

  // --------------------------- Category Management Section ---------------------------

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (response: ApiResponse<Category[]>) => {
        if (response.status === true && response.data) {
          console.log(response.message);
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

  openAddCategoryModal(): void {
    this.isEditMode = false;
    this.categoryForm.reset();

    const modalElement = document.getElementById('addCategoryModal');
    if (modalElement) {
      modalElement.classList.add('show');
      modalElement.style.display = 'block';
    }
  }

  openEditCategoryModal(category: Category): void {
    this.isEditMode = true;
    this.selectedCategory = category;
    this.categoryForm.patchValue(category);

    const modalElement = document.getElementById('editCategoryModal');
    if (modalElement) {
      modalElement.classList.add('show');
      modalElement.style.display = 'block';
    }
  }

  closeCategoryModal(): void {
    const addModal = document.getElementById('addCategoryModal');
    const editModal = document.getElementById('editCategoryModal');
    if (addModal) {
      addModal.classList.remove('show');
      addModal.style.display = 'none';
    }
    if (editModal) {
      editModal.classList.remove('show');
      editModal.style.display = 'none';
    }
  }


  saveCategory(): void {
    if (this.categoryForm.invalid) return;
    const categoryData = this.categoryForm.value;

    if (this.isEditMode && this.selectedCategory) {
      this.categoryService.updateCategory(categoryData.id, categoryData).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            this.closeCategoryModal();
            this.loadCategories();
          } else {
            this.errorMessage = response.message || 'An error occurred while updating the category.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while updating the category.'
          console.error("Error updating category:", error);
        }
      });
    } else {
      this.categoryService.addCategory(categoryData).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            this.closeCategoryModal();
            this.loadCategories();
          } else {
            this.errorMessage = response.message || 'An error occurred while adding the category.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while adding the category.'
          console.error('Error adding category:', error);
        }
      });
    }
  }

  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            this.loadCategories();
          } else {
            this.errorMessage = response.message || 'An error occurred while deleting the category.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while deleting the category.'
          console.error('Error deleting category:', error);
        }
      });
    }
  }
  
  onCategoryChange(event: any) {
    const categoryId = event.target.value;
    this.getProductsByCategory(categoryId);
  }

  // --------------------------- Orders Management Section ---------------------------

  getAllOrders(): void {
    this.orderService.getAllOrders().subscribe({
      next: (response: ApiResponse<Order[]>) => {
        if (response.status === true && response.data) {
          console.log(response.message);
          this.orders = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching orders.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching orders.';
        console.error('Error fetching orders:', error);
      }
    });
  }

  openEditOrderModal(order: Order): void {
    this.isEditMode = true;
    this.selectedOrder = order;
    this.orderForm.patchValue({
      status: order.status
    });

    const modal = document.getElementById('editOrderModal');
    if (modal) {
      modal.classList.add('show');
      modal.style.display = 'block';
    }
  }

  closeOrderModal(): void {
    const modal = document.getElementById('editOrderModal');
    if (modal) {
      modal.classList.remove('show');
      modal.style.display = 'none';
    }
  }

  updateOrderStatus(): void {
    if (this.orderForm.invalid) {
      console.log("Form is invalid"); // Debugging
      return;
    }
  
    const updatedStatus = this.orderForm.value.status;
    console.log("Updating order status to:", updatedStatus); // Debugging
  
    if (this.isEditMode && this.selectedOrder) {
      this.orderService.updateOrderStatus(this.selectedOrder.id, updatedStatus).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.status === true) {
            console.log(response.message);
            this.closeOrderModal();
            this.getAllOrders();
          } else {
            this.errorMessage = response.message || 'An error occurred while updating the order status.';
          }
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'An error occurred while updating the order status.'
          console.error("Error updating order status:", error);
        }
      });
    }
  }
  
  deleteOrder(id: number): void {
    if (!confirm('Are you sure you want to delete this order?')) {
      return;
    }
    this.orderService.deleteOrder(id).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status === true) {
          console.log(response.message);
          this.getAllOrders();
        } else {
          this.errorMessage = response.message || 'An error occurred while deleting the order.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while deleting the order.'
        console.error('Error deleting order:', error);
      }
    });
  }

  // --------------------------- Address Management Section ---------------------------


  openAddressPopup(addressId: number) {
    this.address = null; // Reset previous address
    this.addressModalVisible = true; // Show modal
    this.getAddressById(addressId); // Fetch address
  }

  getAddressById(addressId: number) {
    this.addressService.getAddressById(addressId).subscribe({
      next: (response: ApiResponse<Address>) => {
        if (response.status === true && response.data) {
          console.log(response.message);
          this.address = response.data;
        } else {
          this.errorMessage = response.message || 'An error occurred while fetching address.';
        }
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'An error occurred while fetching address.';
        console.error('Error fetching address:', error);
      }
    });
  }

  closeAddressPopup() {
    this.addressModalVisible = false; // Hide the modal
  }
}
