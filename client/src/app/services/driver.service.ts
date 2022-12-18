import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Driver } from '../models/Driver';

const DRIVER_API = 'http://localhost:8080/api/driver/';

@Injectable({
  providedIn: 'root'
})
export class DriverService {
  constructor(private httpClient: HttpClient) { 
  }

  login(driver: any): Observable<any> {
    return this.httpClient.post(DRIVER_API + 'login', {
      username: driver.username,
      password: driver.password
    });
  }

  fulfill(driver: Driver): Observable<any> {
    return this.httpClient.post(DRIVER_API + 'fulfill', {
      firstName: driver.firstName,
      lastName: driver.lastName,
      telephone: driver.telephone
    });
  }

  getAcceptedOrders(): Observable<any> {
    return this.httpClient.get(DRIVER_API + 'accepted-orders');
  }

  getOrderItems(orderId: number): Observable<any> {
    return this.httpClient.get(DRIVER_API + 'order-items/' + orderId);
  }

  getDriverData(): Observable<any> {
    return this.httpClient.get(DRIVER_API + 'driver-data');
  }

  startOrder(orderId: number): Observable<any> {
    return this.httpClient.get(DRIVER_API + 'start-order/' + orderId);
  }

  finishOrder(orderId: number): Observable<any> {
    return this.httpClient.get(DRIVER_API + 'finish-order/' + orderId);
  }

  getDriverOrders(): Observable<any> {
    return this.httpClient.get(DRIVER_API + 'driver-orders');
  }

  updatePassword(password: string): Observable<any> {
    return this.httpClient.get(DRIVER_API + 'update-password/' + password);
  }
}
