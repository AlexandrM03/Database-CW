import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Order } from 'src/app/models/Order';
import { UserServiceService } from 'src/app/services/user-service.service';
import { UsernameStorageService } from 'src/app/services/username-storage.service';
import { CreateOrderComponent } from '../create-order/create-order.component';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {
  orders: Order[];
  isOrdersLoaded: boolean = false;
  username: string;

  constructor(private userService: UserServiceService,
              private usernameStorage: UsernameStorageService,
              private router: Router,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.username = this.usernameStorage.getUsername();
    this.userService.getUserOrders()
      .subscribe({
        next: data => {
          this.orders = data;
          this.isOrdersLoaded = true;
        }
      });
  }

  createOrder(): void {
    this.dialog.open(CreateOrderComponent);
  }

  getOrderItems(order: Order) {
    this.router.navigate(['/order-items', order.id]);
  }
}
