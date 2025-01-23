import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

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
  emailExists = false;
  phoneExists = false;
  termsAccepted = false; 
  termsTouched = false;    

  constructor(private authService: AuthService, private userService: UserService, private router: Router) {}

  hidePassword() {
    this.hide = !this.hide;
  }

  validatePasswordMatch() {
    this.passwordsDontMatch = this.user.password !== this.confirmPassword;
  }

  checkEmailExists() {
    this.userService.checkEmailExists(this.user.email).subscribe((exists) => {
      this.emailExists = exists;
    });
  }

  checkPhoneExists() {
    this.userService.checkPhoneExists(this.user.phone).subscribe((exists) => {
      this.phoneExists = exists;
    });
  }

  validateTerms() {
    this.termsTouched = true;
  }
  

  register() {
    if (this.passwordsDontMatch || this.emailExists || this.phoneExists || !this.termsAccepted) {
      return;
    }
  
    this.authService.register(this.user).subscribe({
      next: (response: any) => {
        // Log the response to verify its structure
        console.log('Response from server:', response);
  
        // Assuming the response is of type { message: string }
        alert("Registered Successfully!"); // Display the message in the popup

        this.router.navigate(['/login'])
      },
      error: (error) => {
        console.error('Error:', error);
        
        // Handle the error, and display the error message if available
        alert(error.error?.message || 'Error during registration!');
      }
    });
  }
  
  
  
}
