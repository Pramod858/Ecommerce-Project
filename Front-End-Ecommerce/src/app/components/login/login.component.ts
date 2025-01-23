import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

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
      next: (response: any) => {
        console.log('Login successful:', response);
        localStorage.setItem('token', response.token); // Save JWT token
        localStorage.setItem('userName', response.userName);
        this.router.navigate(['/home']); // Redirect after login
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.loginFailed = true;
        alert(error.error?.message || 'Login Failed, please try  again!');
      }
    });
  }
}
