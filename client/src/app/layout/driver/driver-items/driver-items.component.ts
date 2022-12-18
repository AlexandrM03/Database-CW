import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderItem } from 'src/app/models/OrderItem';
import { DriverService } from 'src/app/services/driver.service';

@Component({
  selector: 'app-driver-items',
  templateUrl: './driver-items.component.html',
  styleUrls: ['./driver-items.component.css']
})
export class DriverItemsComponent implements OnInit {
  orderId: number;
  orderItems: OrderItem[];
  isOrderItemsLoaded: boolean = false;

  constructor(private driverService: DriverService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.orderId = params['orderId'];
      this.driverService.getOrderItems(this.orderId).subscribe(data => {
        this.orderItems = data;
        this.isOrderItemsLoaded = true;
      });
    });
  }
}
