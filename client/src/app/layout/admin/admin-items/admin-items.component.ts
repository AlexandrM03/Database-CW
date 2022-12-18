import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderItem } from 'src/app/models/OrderItem';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-items',
  templateUrl: './admin-items.component.html',
  styleUrls: ['./admin-items.component.css']
})
export class AdminItemsComponent implements OnInit {
  orderId: number;
  orderItems: OrderItem[];
  isOrderItemsLoaded: boolean = false;

  constructor(private adminService: AdminService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.orderId = params['orderId'];
      this.adminService.getOrderItems(this.orderId).subscribe(data => {
        this.orderItems = data;
        this.isOrderItemsLoaded = true;
      });
    });
  }
}
