import { Component, OnInit, ViewChild } from '@angular/core';
import { Notification } from 'src/app/models/Notification';
import { MatTable } from '@angular/material/table';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  notifications: Notification[];
  isNotificationsLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<Notification>;

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.adminService.getAllNotifications().subscribe(data => {
      this.notifications = data;
      this.isNotificationsLoaded = true;
    });
  }

  deleteNotification(id: number): void {
    this.adminService.removeNotification(id).subscribe(() => {
      this.adminService.getAllNotifications().subscribe(data => {
        this.notifications = data;
        this.table.renderRows();
      });
    });
  }
}
