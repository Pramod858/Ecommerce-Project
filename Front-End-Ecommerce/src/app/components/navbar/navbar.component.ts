import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  constructor(private router: Router) {
  }

  searchQuery: string = '';
  userName: string = localStorage.getItem('userName') || '';

  cartCount: number = 0;

  logDropdownClick(): void {
    console.log('Dropdown clicked');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userName');
    window.location.reload();
  }

  isHomePage(): boolean {
    return this.router.url === '/home';
  }

  isCartPage(): boolean {
    return this.router.url.includes('/cart');
  }

  isCheckoutPage(): boolean {
    return this.router.url.includes('/checkout');
  }

  isOrdersPage(): boolean {
    return this.router.url.includes('/orders');
  }

  isLoginPage(): boolean {
    return this.router.url.includes('/login');
  }

  isSignupPage(): boolean {
    return this.router.url.includes('/signup');
  }
}
