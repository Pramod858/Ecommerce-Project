import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-login',
  standalone: false,
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.css'
})
export class AdminLoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private router: Router) {}

  ngOnInit(): void {}

  // Handle login action
  onLogin(): void {
    if (this.username === 'admin' && this.password === 'admin123') {
      // Login successful, redirect to admin home
      this.router.navigate(['/admin-home']);
    } else {
      // Invalid credentials
      this.errorMessage = 'Invalid username or password!';
    }
  }
}
