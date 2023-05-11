import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { UserResponse } from 'src/app/models/responses/user-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly userUrl = `${environment.api.baseUrl}/${environment.api.userUrl}`;
  private readonly adminUserUrl = `${environment.api.baseUrl}/${environment.api.adminUserUrl}`;

  constructor(
    private http: HttpClient
  ) { }

  allActiveUsers$ = <Observable<UserResponse>>this.http.get(`${this.userUrl}`)
    .pipe(
      tap(console.log)
    );

  userById$ = (id: string) => <Observable<UserResponse>>this.http.get(`${this.userUrl}/${id}`)
    .pipe(
      tap(console.log)
    );
}
