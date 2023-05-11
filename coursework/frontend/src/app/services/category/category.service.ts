import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take, tap } from 'rxjs';
import { CategoryDto } from 'src/app/models/dto/category/category-dto';
import { CategoryResponse } from 'src/app/models/responses/category-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private readonly categoryUrl = `${environment.api.baseUrl}/${environment.api.categoryUrl}`;
  private readonly adminCategoryUrl = `${environment.api.baseUrl}/${environment.api.adminCategoryUrl}`;

  constructor(
    private http: HttpClient
  ) { }

  categories$ = <Observable<CategoryResponse>>this.http.get(this.categoryUrl)
    .pipe(
      tap(console.log)
    )

  categoryById$ = (id: string) => <Observable<CategoryResponse>>this.http.get(`${this.categoryUrl}/${id}`)
    .pipe(
      tap(console.log)
    )
}
