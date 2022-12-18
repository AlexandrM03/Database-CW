import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { OrderItem } from 'src/app/models/OrderItem';
import { UserServiceService } from 'src/app/services/user-service.service';
import { AddItemComponent } from '../add-item/add-item.component';
import { MatTable } from '@angular/material/table';
import { Rate } from 'src/app/models/Rate';
import { RateComponent } from '../rate/rate.component';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  orderId: number;
  orderItems: OrderItem[];
  addOrderItem: OrderItem = {
    name: '',
    weight: 0,
    volume: 0
  };
  isOrdersItemsLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<OrderItem>

  rate: Rate;
  orderStatus: string;

  constructor(private userService: UserServiceService,
              private route: ActivatedRoute,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.orderId = params['orderId'];
      this.userService.getOrderItems(this.orderId).subscribe(data => {
        this.orderItems = data;
        this.rate = {
          orderId: this.orderId,
          rate: 5,
          message: 'your message'
        };
        this.isOrdersItemsLoaded = true;
      });

      this.userService.getOrderStatus(this.orderId).subscribe(data => {
        this.orderStatus = data.message;
      });
    });
    
    this.dialog._getAfterAllClosed().subscribe(() => {
      this.userService.getOrderItems(this.orderId).subscribe(data => {
        this.orderItems = data;
        this.table.renderRows();
      });
    });
  }

  openOrderItemDialog(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '400px';
    dialogConfig.data = {
      orderId: this.orderId,
      orderItem: this.addOrderItem
    };

    this.dialog.open(AddItemComponent, dialogConfig);
  }

  openRateDialog(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '400px';
    dialogConfig.data = {
      rate: this.rate
    };

    this.dialog.open(RateComponent, dialogConfig);
  }
}
