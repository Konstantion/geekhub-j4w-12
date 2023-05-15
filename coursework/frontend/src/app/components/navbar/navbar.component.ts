import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ConfirmationService, MessageService } from 'primeng/api';
import { BehaviorSubject, Observable, catchError, map, of } from 'rxjs';
import { AuthenticationResponseDto } from 'src/app/models/dto/authentication/authentication-response-dto';
import { CallDto } from 'src/app/models/dto/call/call-dto';
import { CallResponse } from 'src/app/models/responses/call-response';
import { DataState } from 'src/app/models/state/enum/data-state';
import { CallPageState } from 'src/app/models/state/pages/call-page-state';
import { CallService } from 'src/app/services/call/call.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class NavbarComponent implements OnInit {
  authorizedUser: AuthenticationResponseDto = null;
  overlayVisible = false;
  calls: CallDto[] = [];

  readonly DataState = DataState;
  constructor(
    private cdr: ChangeDetectorRef,
    private router: Router,
    private callsService: CallService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService) { }



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
            this.fetchCalls();
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

  onCalls() {
    if (!this.overlayVisible) this.fetchCalls();
    this.overlayVisible = !this.overlayVisible;
  }

  onCall(id: string) {
    this.callsService.closeCallById$(id).pipe(
      map(response => {
        this.fetchCalls();
      }),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  handleError(error: any) {
    let errorResponse = error.error;
    if (error.status === 403) {
      this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'Not enough authorities' });
    } else if (error.status === 400) {
      this.messageService.add({ severity: 'error', summary: 'Rejected', detail: errorResponse.message });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Rejected', detail: error.message });
    }
    return of();
  }

  fetchCalls() {
    let observable: Observable<CallResponse>;
    if (this.authorizedUser.authenticated.user.roles.includes('ADMIN')) observable = this.callsService.calls$;
    else observable = this.callsService.activeCalls$;
    observable.subscribe(response => {
      this.calls = response.data.calls;
    })
  }
}