import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthenticationResponseDto } from 'src/app/models/dto/authentication/authentication-response-dto';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  authorizedUser: AuthenticationResponseDto = null;

  constructor(private cdr: ChangeDetectorRef, private router: Router) { }

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
          const authResponse: AuthenticationResponseDto = JSON.parse(storedUser);
          const token = authResponse.token;
          if (token) {
            const jwtHelper = new JwtHelperService();
            const isTokenNonExpired = !jwtHelper.isTokenExpired(token);
            if (isTokenNonExpired) {
              this.authorizedUser = authResponse;
              this.cdr.detectChanges();
            }
          }
        }
      }
    });
  }

  onLogout() {
    localStorage.removeItem('user');
  }

  onTables() {
    this.router.navigate([`tables`]);
  }

  onProducts() {
    this.router.navigate([`products`]);
  }

  onOrders() {
    this.router.navigate([`orders`]);
  }

  onBills() {
    this.router.navigate([`bills`]);
  }

  onUser() {
    const userId = this.authorizedUser.authenticated.user.id;
    if (userId) this.router.navigate([`users/${userId}`]);
  }

  onAdmin() {
    this.router.navigate([`admin`]);
  }
}