import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HallComponent } from './components/hall/hall.component';
import { AccessGuardService } from './services/guard/access-guard.service';
import { TablesComponent } from './components/tables/tables.component';
import { TableComponent } from './components/table/table.component';
import { AdminComponent } from './components/admin/admin.component';
import { AdminAccessGuardService } from './services/admin-guard/admin-access-guard.service';
import { OrderComponent } from './components/order/order.component';
import { ProductsComponent } from './components/products/products.component';
import { ProductComponent } from './components/product/product.component';
import { UserComponent } from './components/user/user.component';
import { BillComponent } from './components/bill/bill.component';
import { OrdersComponent } from './components/orders/orders.component';
import { BillsComponent } from './components/bills/bills.component';
import { GuestComponent } from './components/guest/guest.component';

const routes: Routes = [{
  path: '',
  redirectTo: 'tables',
  pathMatch: 'full'
},
{
  path: 'login',
  component: LoginComponent
},
{
  path: 'halls/:id',
  component: HallComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'users/:id',
  component: UserComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'tables/:id',
  component: TableComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'orders/:id',
  component: OrderComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'orders',
  component: OrdersComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'bills',
  component: BillsComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'bills/:id',
  component: BillComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'products/:id',
  component: ProductComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'guests/:id',
  component: GuestComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'tables',
  component: TablesComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'products',
  component: ProductsComponent,
  canActivate: [AccessGuardService]
},
{
  path: 'admin',
  component: AdminComponent,
  canActivate: [AdminAccessGuardService]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
