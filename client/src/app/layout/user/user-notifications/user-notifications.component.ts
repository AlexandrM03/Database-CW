import { Component, OnInit, ViewChild } from '@angular/core';
import { Notification } from 'src/app/models/Notification';
import { UserServiceService } from 'src/app/services/user-service.service';
import { MatTable } from '@angular/material/table';

@Component({
  selector: 'app-user-notifications',
  templateUrl: './user-notifications.component.html',
  styleUrls: ['./user-notifications.component.css']
})
export class UserNotificationsComponent implements OnInit {
  notifications: Notification[];
  isNotificationsLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<Notification>;

  constructor(private userService: UserServiceService) { }

  ngOnInit(): void {
    this.userService.getNotifications().subscribe(data => {
      this.notifications = data;
      this.isNotificationsLoaded = true;
    });
  }

  deleteNotification(id: number): void {
    this.userService.removeNotification(id).subscribe(() => {
      this.userService.getNotifications().subscribe(data => {
        this.notifications = data;
        this.table.renderRows();
      });
    });
  }
}
