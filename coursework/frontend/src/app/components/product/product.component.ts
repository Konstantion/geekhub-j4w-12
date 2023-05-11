import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { BehaviorSubject, catchError, concatMap, of, map } from 'rxjs';
import { CreateProductRequestDto } from 'src/app/models/dto/product/create-product-dto';
import { ProductPageDto } from 'src/app/models/dto/product/product-page-dto';
import { CategoryResponse } from 'src/app/models/responses/category-response';
import { DataState } from 'src/app/models/state/enum/data-state';
import { ProductPageState } from 'src/app/models/state/pages/product-page-state';
import { CategoryService } from 'src/app/services/category/category.service';
import { ProductService } from 'src/app/services/product/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class ProductComponent implements OnInit {
  constructor(
    private activeRoute: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private categoryService: CategoryService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) { }

  private productId: string;
  private productPageSubject = new BehaviorSubject<ProductPageState>({});

  updateProductData: CreateProductRequestDto = {};

  pageState$ = this.productPageSubject.asObservable();

  readonly DataState = DataState

  ngOnInit(): void {
    this.productId = this.activeRoute.snapshot.paramMap.get('id');
    this.productPageSubject.next({ dataState: DataState.LOADING_STATE });
    this.productService.productById$(this.productId).pipe(
      concatMap(productResponse => {
        const state = this.productPageSubject.value;
        state.product = productResponse.data.product;
        state.dataState = DataState.LOADED_STATE;
        this.productPageSubject.next(state);
        if (productResponse.data.product.categoryId) {
          return this.categoryService.categoryById$(productResponse.data.product.categoryId)
        } else {
          return of<CategoryResponse>({ data: { category: null } });
        }

      }),
      map(categoryResponse => {
        const state = this.productPageSubject.value;
        state.category = categoryResponse.data.category;
        this.productPageSubject.next(state);
      }),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  onActivate() {

  }

  onDeactivate() {

  }

  onUpdate() {

  }

  onDelete() {

  }

  arrayBufferToUrl(arrayBuffer: ArrayBuffer): string {
    const altUrl = 'https://via.placeholder.com/100';
    if (arrayBuffer === null) {
      return altUrl;
    }
    return `data:image/png;base64,${arrayBuffer}`
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
}
