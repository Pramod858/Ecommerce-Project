import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { ApiResponse } from '../../models/api.response';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user = { username: '', email: '', phone: '', password: '' };
  confirmPassword = '';
  passwordsDontMatch = false;
  hide = true;
  termsAccepted = false; 
  termsTouched = false;    

  constructor(private authService: AuthService, private userService: UserService, private router: Router) {}

  hidePassword() {
    this.hide = !this.hide;
  }

  validatePasswordMatch() {
    this.passwordsDontMatch = this.user.password !== this.confirmPassword;
  }

  validateTerms() {
    this.termsTouched = true;
  }

  isStrongPassword(password: string): boolean {
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password);
    return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
  }
  
  register() {
    if (!this.isStrongPassword(this.user.password)) {
      alert('Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.');
      return;
    }

    if (this.passwordsDontMatch || !this.termsAccepted) {
      return;
    }
  
    this.authService.register(this.user).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status === true) {
          console.log('Registration successful:', response);
          this.router.navigate(['/login']); // Redirect after registration
        } else {
          console.error('Registration failed:', response.message);
          alert(response.message || 'Registration failed, please try again!');
        }
      },
      error: (error) => {
        console.error('Registration failed:', error);
        alert(error.error?.message || 'Registration failed, please try again!');
      }
    });
  }
}
