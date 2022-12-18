import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from './matetial-module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { authInterceptorService } from './services/auth-interceptor.service';
import { IndexComponent } from './layout/user/index/index.component';
import { ProfileComponent } from './layout/user/profile/profile.component';
import { OrderComponent } from './layout/user/order/order.component';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { NavigationComponent } from './layout/user/navigation/navigation.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { EditUserComponent } from './layout/user/edit-user/edit-user.component';
import { MatDialogModule } from '@angular/material/dialog';
import { AddItemComponent } from './layout/user/add-item/add-item.component';
import { MatDividerModule } from '@angular/material/divider';
import { CreateOrderComponent } from './layout/user/create-order/create-order.component';
import { MatSelectModule } from '@angular/material/select';
import { NotificationsComponent } from './layout/admin/notifications/notifications.component';
import { AdminNavigationComponent } from './layout/admin/admin-navigation/admin-navigation.component';
import { AdminOrdersComponent } from './layout/admin/admin-orders/admin-orders.component';
import { AdminUsersComponent } from './layout/admin/admin-users/admin-users.component';
import { AdminDriversComponent } from './layout/admin/admin-drivers/admin-drivers.component';
import { AdminItemsComponent } from './layout/admin/admin-items/admin-items.component';
import { HireDriverComponent } from './layout/admin/hire-driver/hire-driver.component';
import { UserNotificationsComponent } from './layout/user/user-notifications/user-notifications.component';
import { RateComponent } from './layout/user/rate/rate.component';
import { AdminRatingComponent } from './layout/admin/admin-rating/admin-rating.component';
import { AcceptedOrdersComponent } from './layout/driver/accepted-orders/accepted-orders.component';
import { DriverOrdersComponent } from './layout/driver/driver-orders/driver-orders.component';
import { DriverNavigationComponent } from './layout/driver/driver-navigation/driver-navigation.component';
import { DriverItemsComponent } from './layout/driver/driver-items/driver-items.component';
import { DriverProfileComponent } from './layout/driver/driver-profile/driver-profile.component';
import { DriverEditComponent } from './layout/driver/driver-edit/driver-edit.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBarModule } from '@angular/material/snack-bar';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    IndexComponent,
    ProfileComponent,
    OrderComponent,
    NavigationComponent,
    EditUserComponent,
    AddItemComponent,
    CreateOrderComponent,
    NotificationsComponent,
    AdminNavigationComponent,
    AdminOrdersComponent,
    AdminUsersComponent,
    AdminDriversComponent,
    AdminItemsComponent,
    HireDriverComponent,
    UserNotificationsComponent,
    RateComponent,
    AdminRatingComponent,
    AcceptedOrdersComponent,
    DriverOrdersComponent,
    DriverNavigationComponent,
    DriverItemsComponent,
    DriverProfileComponent,
    DriverEditComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    MatCardModule,
    MatTableModule,
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    MatDialogModule,
    MatDividerModule,
    MatSelectModule,
    MatCheckboxModule,
    MatSnackBarModule
  ],
  providers: [authInterceptorService],
  bootstrap: [AppComponent]
})
export class AppModule { }
