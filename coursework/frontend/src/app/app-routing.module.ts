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
  path: 'products/:id',
  component: ProductComponent,
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
