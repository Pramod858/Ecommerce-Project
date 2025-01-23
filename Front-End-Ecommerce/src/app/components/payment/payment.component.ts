import { Component, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';

@Component({
  selector: 'app-payment',
  standalone: false,
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  stripe: any;
  elements: any;
  card: any;
  isProcessing: boolean = false;
  isPaymentSuccessful: boolean = false;

  async ngOnInit() {
    // Initialize Stripe and Elements
    this.stripe = await loadStripe('your-public-key-from-stripe');
    this.elements = this.stripe.elements();
    this.card = this.elements.create('card');
    this.card.mount('#card-element');
  }

  async handlePayment() {
    this.isProcessing = true;
    
    // Create payment method
    const {token, error} = await this.stripe.createToken(this.card);
    
    if (error) {
      console.error(error);
      this.isProcessing = false;
    } else {
      // Call backend to process payment with the token
      const response = await fetch('http://localhost:8080/process-payment', {
        method: 'POST',
        body: JSON.stringify({ token: token.id }),
        headers: { 'Content-Type': 'application/json' }
      });

      const data = await response.json();
      if (response.ok) {
        this.isPaymentSuccessful = true;
      } else {
        console.error('Payment failed:', data);
      }
      this.isProcessing = false;
    }
  }
}
