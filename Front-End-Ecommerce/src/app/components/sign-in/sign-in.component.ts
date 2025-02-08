import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ApiResponse } from '../../models/api.response';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-sign-in',
  standalone: false,
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  credentials = { email: '', password: '' };
  loginFailed = false;
  hide = true;

  constructor(
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  hidePassword() {
    this.hide = !this.hide;
  }

  login() {
    this.authService.login(this.credentials).subscribe({
      next: (response: ApiResponse<{ userName: string; token: string }>) => {
        if (response.status === true && response.data) {
          console.log('Login successful:', response);

          // Check if running in the browser
          if (isPlatformBrowser(this.platformId)) {
            localStorage.setItem('token', response.data.token); // Save JWT token
            localStorage.setItem('userName', response.data.userName); // Save username
          }

          this.router.navigate(['/']);
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