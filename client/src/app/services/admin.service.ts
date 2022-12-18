import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const ADMIN_API = 'http://localhost:8080/api/admin/';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  constructor(private httpClient: HttpClient) {
  }

  updateOrderPrice(orderId: number): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'update-order-price/' + orderId);
  }

  acceptOrder(orderId: number): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'accept-order/' + orderId);
  }

  getAllDrivers(): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-all-drivers');
  }

  getAllUsers(): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-all-users');
  }

  getAllOrders(): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-all-orders');
  }

  getAllOrdersByDriver(driver: string): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-all-orders-by-driver/' + driver);
  }

  getAllOrdersByUser(username: string): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-all-orders-by-user/' + username);
  }

  getOrderItems(orderId: number): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-order-items/' + orderId);
  }

  hireDriver(driver: any): Observable<any> {
    return this.httpClient.post(ADMIN_API + 'hire-driver', {
      username: driver.username,
      password: driver.password,
    });
  }

  rejectOrder(orderId: number): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'reject-order/' + orderId);
  }

  getAllNotifications(): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-all-notifications');
  }

  removeNotification(notificationId: number): Observable<any> {
    return this.httpClient.delete(ADMIN_API + 'remove-notification/' + notificationId);
  }

  confirmDriverRating(driverUsername: string): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'confirm-driver-rating/' + driverUsername);
  }

  removeDriverRating(ratingId: number): Observable<any> {
    return this.httpClient.delete(ADMIN_API + 'remove-driver-rating/' + ratingId);
  }

  getDriverRating(driverUsername: string): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-driver-rating/' + driverUsername);
  }

  getAllUsersPagination(page: number): Observable<any> {
    return this.httpClient.get(ADMIN_API + 'get-all-users-pagination/' + page);
  }
}
