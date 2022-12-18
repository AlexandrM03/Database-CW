import { Component, OnInit, ViewChild } from '@angular/core';
import { DriverOrder } from 'src/app/models/DriverOrder';
import { DriverService } from 'src/app/services/driver.service';
import { MatTable } from '@angular/material/table';

@Component({
  selector: 'app-accepted-orders',
  templateUrl: './accepted-orders.component.html',
  styleUrls: ['./accepted-orders.component.css']
})
export class AcceptedOrdersComponent implements OnInit {
  orders: DriverOrder[];
  isOrdersLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<DriverOrder>;

  constructor(private driverService: DriverService) { }

  ngOnInit(): void {
    this.driverService.getAcceptedOrders().subscribe(orders => {
      this.orders = orders;
      this.isOrdersLoaded = true;
    });
  }

  startOrder(event: any, orderId: number): void {
    event.stopPropagation();
    this.driverService.startOrder(orderId).subscribe(() => {
      this.ngOnInit();
      this.table.renderRows();
    });
  }
}
