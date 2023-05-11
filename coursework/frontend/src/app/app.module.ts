import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { MenuModule } from 'primeng/menu';
import { InputTextModule } from 'primeng/inputtext';
import { MessagesModule } from 'primeng/messages';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MessageModule } from 'primeng/message';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { HallComponent } from './components/hall/hall.component';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { SidebarModule } from 'primeng/sidebar';
import { CardModule } from 'primeng/card';
import { BadgeModule } from 'primeng/badge';
import { ToastModule } from 'primeng/toast';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { HttpInterceptorService } from './services/interceptor/http-interceptor.service';
import { TablesComponent } from './components/tables/tables.component';
import { ModalComponent } from './components/modal/modal.component';
import { TableCardComponent } from './components/table-card/table-card.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { TableComponent } from './components/table/table.component';
import { UserCardComponent } from './components/user-card/user-card.component';
import { AdminComponent } from './components/admin/admin.component';
import { ConfirmationService } from 'primeng/api';
import { OrderComponent } from './components/order/order.component';
import { ProductsComponent } from './components/products/products.component';
import { ProductsCardComponent } from './components/products-card/products-card.component';
import { ProductComponent } from './components/product/product.component';
import { SpinnerComponent } from './components/spinner/spinner.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HallComponent,
    TablesComponent,
    ModalComponent,
    TableCardComponent,
    NavbarComponent,
    TableComponent,
    UserCardComponent,
    AdminComponent,
    OrderComponent,
    ProductsComponent,
    ProductsCardComponent,
    ProductComponent, 
    SpinnerComponent
  ],
  imports: [
    BrowserModule,
    ConfirmPopupModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    RippleModule,
    ConfirmPopupModule,
    MenuModule,
    SidebarModule,
    HttpClientModule,
    MessagesModule,
    MessageModule,
    CardModule,
    BadgeModule,
    ToastModule,
    ProgressSpinnerModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
