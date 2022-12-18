import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { AdminDriversComponent } from './layout/admin/admin-drivers/admin-drivers.component';
import { AdminItemsComponent } from './layout/admin/admin-items/admin-items.component';
import { AdminOrdersComponent } from './layout/admin/admin-orders/admin-orders.component';
import { AdminRatingComponent } from './layout/admin/admin-rating/admin-rating.component';
import { AdminUsersComponent } from './layout/admin/admin-users/admin-users.component';
import { NotificationsComponent } from './layout/admin/notifications/notifications.component';
import { AcceptedOrdersComponent } from './layout/driver/accepted-orders/accepted-orders.component';
import { DriverItemsComponent } from './layout/driver/driver-items/driver-items.component';
import { DriverOrdersComponent } from './layout/driver/driver-orders/driver-orders.component';
import { DriverProfileComponent } from './layout/driver/driver-profile/driver-profile.component';
import { IndexComponent } from './layout/user/index/index.component';
import { OrderComponent } from './layout/user/order/order.component';
import { ProfileComponent } from './layout/user/profile/profile.component';
import { UserNotificationsComponent } from './layout/user/user-notifications/user-notifications.component';
import { AdminGuardService } from './services/admin-guard.service';
import { AuthGuardService } from './services/auth-guard.service';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'index', component: IndexComponent, canActivate: [AuthGuardService]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService]},
  {path: 'order-items/:orderId', component: OrderComponent, canActivate: [AuthGuardService]},
  {path: 'user-notifications', component: UserNotificationsComponent, canActivate: [AuthGuardService]},
  // admin
  {path: 'users/:page', component: AdminUsersComponent, canActivate: [AdminGuardService]},
  {path: 'orders/:username', component: AdminOrdersComponent, canActivate: [AdminGuardService]},
  {path: 'order-info/:orderId', component: AdminItemsComponent, canActivate: [AdminGuardService]},
  {path: 'drivers', component: AdminDriversComponent, canActivate: [AdminGuardService]},
  {path: 'admin-notifications', component: NotificationsComponent, canActivate: [AdminGuardService]},
  {path: 'driver-rating/:username', component: AdminRatingComponent, canActivate: [AdminGuardService]},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  // driver
  {path: 'driver-profile', component: DriverProfileComponent, canActivate: [AuthGuardService]},
  {path: 'accepted-orders', component: AcceptedOrdersComponent, canActivate: [AuthGuardService]},
  {path: 'driver-items/:orderId', component: DriverItemsComponent, canActivate: [AuthGuardService]},
  {path: 'driver-orders', component: DriverOrdersComponent, canActivate: [AuthGuardService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }