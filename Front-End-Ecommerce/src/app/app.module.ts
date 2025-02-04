import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MAT_DIALOG_DEFAULT_OPTIONS, MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AdminComponent } from './components/admin/admin.component';
import { ProductComponent } from './components/product/product.component';
import { CartComponent } from './components/cart/cart.component';
import { OrdersComponent } from './components/orders/orders.component';
import { ProfileComponent } from './components/profile/profile.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { PaymentComponent } from './components/payment/payment.component';
import { AddressComponent } from './components/address/address.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ErrorComponent } from './components/error/error.component';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AdminLoginComponent } from './components/admin-login/admin-login.component';
import { FooterComponent } from './components/footer/footer.component';




@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    HomeComponent,
    NavbarComponent,
    AdminComponent,
    ProductComponent,
    CartComponent,
    OrdersComponent,
    ProfileComponent,
    OrderDetailsComponent,
    PaymentComponent,
    AddressComponent,
    CheckoutComponent,
    ErrorComponent,
    AdminLoginComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    ModalModule.forRoot(),
    NgbModule
  ],
  providers: [
    provideHttpClient(), {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}}, provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
