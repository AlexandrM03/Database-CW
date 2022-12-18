import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderItem } from '../models/OrderItem';
import { Rate } from '../models/Rate';
import { User } from '../models/User';

const USER_API = 'http://localhost:8080/api/user/';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  constructor(private httpClient: HttpClient) { 
  }

  getUser(): Observable<any> {
    return this.httpClient.get(USER_API + 'get');
  }

  getUserOrders(): Observable<any> {
    return this.httpClient.get(USER_API + 'get-orders');
  }

  editUserDetails(user: User): Observable<any> {
    return this.httpClient.post(USER_API + 'fulfill', {
      firstName: user.firstName,
      lastName: user.lastName,
      telephone: user.telephone
    })
  }

  getOrderItems(orderId: number): Observable<any> {
    return this.httpClient.get(USER_API + 'get-order-items/' + orderId);
  }

  addOrderItem(orderId: number, orderItem: OrderItem): Observable<any> {
    return this.httpClient.post(USER_API + 'add-item', {
      name: orderItem.name,
      orderId: orderId,
      weight: +orderItem.weight,
      volume: +orderItem.volume
    });
  }

  getCountries(): Observable<any> {
    return this.httpClient.get(USER_API + 'get-countries');
  }

  getCities(country: string): Observable<any> {
    return this.httpClient.get(USER_API + 'get-cities/' + country);
  }

  createOrder(startPoint: string, endPoint: string): Observable<any> {
    return this.httpClient.post(USER_API + 'create-order', {
      startPoint: startPoint,
      endPoint: endPoint
    });
  }

  rateDriver(rate: Rate): Observable<any> {
    return this.httpClient.post(USER_API + 'rate-driver', {
      orderId: rate.orderId,
      rate: rate.rate,
      message: rate.message
    });
  }

  getNotifications(): Observable<any> {
    return this.httpClient.get(USER_API + 'get-notifications');
  }

  removeNotification(notificationId: number): Observable<any> {
    return this.httpClient.delete(USER_API + 'remove-notification/' + notificationId);
  }

  getOrderStatus(orderId: number): Observable<any> {
    return this.httpClient.get(USER_API + 'get-order-status/' + orderId);
  }
}
