import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']  // Updated property name
})
export class NavbarComponent implements OnInit {

  // Initialize properties without accessing localStorage immediately.
  userName: string = '';
  cartCount: number = 0;

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit(): void {
    // Only access localStorage if running in the browser.
    if (isPlatformBrowser(this.platformId)) {
      this.userName = localStorage.getItem('userName') || '';
    }
  }

  logDropdownClick(): void {
    console.log('Dropdown clicked');
  }

  isLoggedIn(): boolean {
    // Check for token only on the browser.
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('token');
    }
    return false;
  }

  logout(): void {
    // Remove tokens only if running in the browser.
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
      localStorage.removeItem('userName');
      window.location.reload();
    }
  }

  isHomePage(): boolean {
    return this.router.url === '/home';
  }

  isCartPage(): boolean {
    return this.router.url.includes('/my-cart');
  }

  isCheckoutPage(): boolean {
    return this.router.url.includes('/checkout');
  }

  isOrdersPage(): boolean {
    return this.router.url.includes('/orders');
  }

  isLoginPage(): boolean {
    return this.router.url.includes('/sign-in');
  }

  isSignupPage(): boolean {
    return this.router.url.includes('/sign-up');
  }
}
