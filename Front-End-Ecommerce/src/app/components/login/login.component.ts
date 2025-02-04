import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ApiResponse } from '../../models/api.response';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = { email: '', password: '' };
  loginFailed = false;
  hide = true;

  constructor(private authService: AuthService, private router: Router) {}

  hidePassword() {
    this.hide = !this.hide;
  }

  login() {
    this.authService.login(this.credentials).subscribe({
      next: (response: ApiResponse<{ userName: string; token: string }>) => {
        if (response.status === true && response.data) {
          console.log('Login successful:', response);
          localStorage.setItem('token', response.data.token); // Save JWT token
          localStorage.setItem('userName', response.data.userName); // Save username
          this.router.navigate(['/home']); // Redirect after login
        } else {
          console.error('Login failed:', response.message);
          this.loginFailed = true;
          alert(response.message || 'Login failed, please try again!');
        }
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.loginFailed = true;
        alert(error.error?.message || 'Login failed, please try again!');
      }
    });
  }
}
