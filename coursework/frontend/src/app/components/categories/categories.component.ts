import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { BehaviorSubject, catchError, map, of, startWith } from 'rxjs';
import { CreateCategoryRequestDto } from 'src/app/models/dto/category/create-category-request-dto';
import { CreateCategoryState } from 'src/app/models/state/crud/create-category-state';
import { DataState } from 'src/app/models/state/enum/data-state';
import { CategoriesPageState } from 'src/app/models/state/pages/categories-page-state';
import { ObjectUtils } from 'src/app/models/util/object-utils';
import { CategoryService } from 'src/app/services/category/category.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class CategoriesComponent {
  constructor(
    private router: Router,
    private categoryService: CategoryService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) { }

  private categoryPageSubject = new BehaviorSubject<CategoriesPageState>({});
  private createCategorySubject = new BehaviorSubject<CreateCategoryState>({});

  pageState$ = this.categoryPageSubject.asObservable();
  createState$ = this.createCategorySubject.asObservable();

  createCategoryData: CreateCategoryRequestDto = {};
  create = false;

  readonly DataState = DataState;

  ngOnInit(): void {

    this.categoryService.categories$.pipe(
      map(response => {
        const state = this.categoryPageSubject.value;
        state.categories = response.data.categories;
        state.dataState = DataState.LOADED_STATE;
      }),
      startWith(this.categoryPageSubject.next({ dataState: DataState.LOADING_STATE })),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  onCreate() {
    this.createCategoryData = {};
    this.createCategorySubject.next({});
    this.create = true;
  }

  createCategory() {
    this.categoryService.createCategory$(ObjectUtils.replaceEmptyWithNull(this.createCategoryData)).pipe(
      map(response => {
        const state = this.categoryPageSubject.value;
        state.categories.push(response.data.category);

        this.categoryPageSubject.next(state);

        this.onClose();
      }),
      catchError(error => {
        if (error.status === 422) {
          const response = error.error;
          this.createCategorySubject.next({ invalid: true, violations: response.data })
          return of({});
        } else {
          return this.handleError(error);
        }
      })
    ).subscribe();
  }

  onClose() {
    this.create = false;
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

  onCard(id: string) {
    this.router.navigate([`categories/${id}`]);
  }
}
