import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { BehaviorSubject, catchError, concatMap, map, of } from 'rxjs';
import { OrderProductRequestDto } from 'src/app/models/dto/order/order-product-request-dto';
import { ProductDto } from 'src/app/models/dto/product/product-dto';
import { TableDto } from 'src/app/models/dto/table/table-dto';
import { TableResponse } from 'src/app/models/responses/table-response';
import { UserResponse } from 'src/app/models/responses/user-response';
import { DataState } from 'src/app/models/state/enum/data-state';
import { OrderPageState } from 'src/app/models/state/pages/order-page-state';
import { OrderService } from 'src/app/services/order/order.service';
import { TableService } from 'src/app/services/table/table.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class OrderComponent implements OnInit {

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private orderService: OrderService,
    private tableService: TableService,
    private userService: UserService,
    private router: Router,
    private activeRoute: ActivatedRoute
  ) { }

  private orderId = '';
  private orderPageSubject = new BehaviorSubject<OrderPageState>({});

  addModal = false;
  removeModal = false;
  transferModal = false;
  transferToId = '';
  tables: TableDto[] = [];
  orderProductData: OrderProductRequestDto = {};

  pageState$ = this.orderPageSubject.asObservable();

  readonly DataState = DataState;

  ngOnInit(): void {
    this.orderId = this.activeRoute.snapshot.paramMap.get('id');
    this.orderPageSubject.next({ orderState: DataState.LOADING_STATE, productsState: DataState.LOADING_STATE });
    this.orderService.orderById$(this.orderId).pipe(
      concatMap(orderResponse => {
        const state = this.orderPageSubject.value;
        state.order = orderResponse.data.order;
        this.orderPageSubject.next(state);
        return of(orderResponse.data.order);
      }),
      concatMap(order => {
        if (order.tableId) {
          return this.tableService.findById$(order.tableId)
        }
        return of<TableResponse>(null);
      }),
      concatMap(tableResponse => {
        const state = this.orderPageSubject.value;
        if (tableResponse) {
          const table = tableResponse.data.table;
          state.table = table;
          this.orderPageSubject.next(state);
        }

        if (state.order.userId) {
          return this.userService.userById$(state.order.userId)
        }
        return of<UserResponse>(null);
      }),
      concatMap(userResponse => {
        const state = this.orderPageSubject.value;
        if (userResponse) {
          const user = userResponse.data.user;
          state.user = user;
        }
        state.orderState = DataState.LOADED_STATE;
        this.orderPageSubject.next(state);
        return this.orderService.orderProductsById$(this.orderId);
      }),
      concatMap(productsResponse => {
        const state = this.orderPageSubject.value;
        state.productsList = productsResponse.data.products.content;
        state.products = this.getProductsMapFromList(productsResponse.data.products.content);
        state.productsState = DataState.LOADED_STATE;

        return of();
      }),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  delete() {
    this.orderService.deleteOrderById$(this.orderId).pipe(
      map(response => {
        this.router.navigate([`tables`])
      }),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  onDelete() {
    this.confirmationService.confirm({
      target: event.target,
      message: 'Are you sure that you want to delete table?',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.delete();
      },
      reject: () => {

      }
    });
  }

  onClose() {
    this.orderService.closeOrderById$(this.orderId).pipe(
      map(response => {
        if (response.data.order.productsId.length === 0) {
          this.router.navigate([`tables`]);
        }
        const state = this.orderPageSubject.value;
        state.order = response.data.order;
      }),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  onAddProduct() {
    this.addModal = true;
  }

  onTransfer() {
    this.transferModal = true;
    this.tableService.activeTables$.pipe(
      map(response => {
        this.tables = response.data.tables.filter(table => table.orderId === null && table.active)
      }),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  onCloseAdd() {
    this.orderProductData = {};
    this.addModal = false;
  }

  onRemoveProduct() {
    this.removeModal = true;
  }

  onTableClick() {
    this.router.navigate([`tables/${this.orderPageSubject.value.order.tableId}`]);
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

  getProductsMapFromList(products: ProductDto[]): Map<ProductDto, number> {
    const productIdsMap = new Map<string, number>();
    products.forEach(product => {
      if (productIdsMap.has(product.id)) {
        productIdsMap.set(product.id, productIdsMap.get(product.id) + 1);
      } else {
        productIdsMap.set(product.id, 1);
      }
    });
    const productsMap = new Map<ProductDto, number>();
    productIdsMap.forEach((quantity, id) => {
      const product = products.find(product => product.id == id);
      productsMap.set(product, quantity);
    })

    return productsMap;
  }

  onProductClick(event: string) {
    this.orderProductData.productId = event;
  }

  addProduct() {
    this.orderService.addProductsToOrderById$(this.orderId, this.orderProductData)
      .pipe(
        concatMap(response => {
          const state = this.orderPageSubject.value;
          state.order = response.data.order;
          this.orderPageSubject.next(state);
          this.onCloseAdd();
          return this.orderService.orderProductsById$(this.orderId);
        }),
        map(response => {
          const state = this.orderPageSubject.value;
          state.productsList = response.data.products.content;
          state.products = this.getProductsMapFromList(response.data.products.content);
          state.productsState = DataState.LOADED_STATE;
        }),
        catchError(error => this.handleError(error))
      ).subscribe();
  }

  removeProduct() {
    this.orderService.removeProductsFromOrderById$(this.orderId, this.orderProductData)
      .pipe(
        concatMap(response => {
          const state = this.orderPageSubject.value;
          state.order = response.data.order;
          this.orderPageSubject.next(state);
          this.onCloseRemove();
          return this.orderService.orderProductsById$(this.orderId);
        }),
        map(response => {
          const state = this.orderPageSubject.value;
          state.productsList = response.data.products.content;
          state.products = this.getProductsMapFromList(response.data.products.content);
          state.productsState = DataState.LOADED_STATE;
        }),
        catchError(error => this.handleError(error))
      ).subscribe();
  }

  onCloseRemove() {
    this.orderProductData = {};
    this.removeModal = false;
  }

  onCloseTransfer() {
    this.transferToId = '';
    this.transferModal = false;
  }

  removeSelect(product: ProductDto) {
    this.orderProductData.productId = product.id;
  }

  transfer() {
    this.orderService.transferOrderToTableById$(this.orderId, this.transferToId).pipe(
      concatMap(response => {
        const state = this.orderPageSubject.value;
        state.order = response.data.order;
        this.onCloseTransfer();
        return this.tableService.findById$(response.data.order.tableId);
      }),
      map(tableResponse => {
        const state = this.orderPageSubject.value;
        state.table = tableResponse.data.table;
      }),
      catchError(error => this.handleError(error))).subscribe();
  }
}
