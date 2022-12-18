import { Component, OnInit, ViewChild } from '@angular/core';
import { AllDriverOrders } from 'src/app/models/AllDriverOrders';
import { DriverService } from 'src/app/services/driver.service';
import { MatTable } from '@angular/material/table';

@Component({
  selector: 'app-driver-orders',
  templateUrl: './driver-orders.component.html',
  styleUrls: ['./driver-orders.component.css']
})
export class DriverOrdersComponent implements OnInit {
  orders: AllDriverOrders[];
  isOrdersLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<AllDriverOrders>;

  constructor(private driverService: DriverService) { }

  ngOnInit(): void {
    this.driverService.getDriverOrders().subscribe(orders => {
      this.orders = orders;
      this.isOrdersLoaded = true;
    });
  }

  finishOrder(event: any, orderId: number): void {
    event.stopPropagation();
    this.driverService.finishOrder(orderId).subscribe(() => {
      this.ngOnInit();
      this.table.renderRows();
    });
  }
}
