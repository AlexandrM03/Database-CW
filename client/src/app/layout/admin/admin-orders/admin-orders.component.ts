import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { OrderInfo } from 'src/app/models/OrderInfo';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-orders',
  templateUrl: './admin-orders.component.html',
  styleUrls: ['./admin-orders.component.css']
})
export class AdminOrdersComponent implements OnInit {
  username: string;
  orders: OrderInfo[];
  isOrdersLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<OrderInfo>;

  constructor(private adminService: AdminService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.username = params['username'];
      if (this.username === 'all') {
        this.adminService.getAllOrders().subscribe(orders => {
          this.orders = orders;
          this.isOrdersLoaded = true;
        });
      } else {
        this.adminService.getAllOrdersByUser(this.username).subscribe(orders => {
          this.orders = orders;
          this.isOrdersLoaded = true;
        });
      }
    })
  }

  evaluatePrice(event: any, orderId: number): void {
    event.stopPropagation();
    this.adminService.updateOrderPrice(orderId).subscribe(() => {
      this.ngOnInit();
      this.table.renderRows();
    });
  }

  acceptOrder(event: any, orderId: number): void {
    event.stopPropagation();
    this.adminService.acceptOrder(orderId).subscribe(() => {
      this.ngOnInit();
      this.table.renderRows();
    });
  }

  rejectOrder(event: any, orderId: number): void {
    event.stopPropagation();
    this.adminService.rejectOrder(orderId).subscribe(() => {
      this.ngOnInit();
      this.table.renderRows();
    });
  }
}
