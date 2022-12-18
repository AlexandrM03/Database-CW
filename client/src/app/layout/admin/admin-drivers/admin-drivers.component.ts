import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { Driver } from 'src/app/models/Driver';
import { AdminService } from 'src/app/services/admin.service';
import { HireDriverComponent } from '../hire-driver/hire-driver.component';

@Component({
  selector: 'app-admin-drivers',
  templateUrl: './admin-drivers.component.html',
  styleUrls: ['./admin-drivers.component.css']
})
export class AdminDriversComponent implements OnInit {
  drivers: Driver[];
  isDriversLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<Driver>;

  constructor(private adminService: AdminService,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.adminService.getAllDrivers().subscribe(drivers => {
      this.drivers = drivers;
      this.isDriversLoaded = true;
    });
    this.dialog._getAfterAllClosed().subscribe(() => {
      this.adminService.getAllDrivers().subscribe(drivers => {
        this.drivers = drivers;
        this.table.renderRows();
      });
    });
  }

  hireDriver(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '400px';

    this.dialog.open(HireDriverComponent, dialogConfig);
  }

  evaluateDriverRating(event: any, username: string): void {
    event.stopPropagation();
    this.adminService.confirmDriverRating(username).subscribe(() => {
      this.adminService.getAllDrivers().subscribe(drivers => {
        this.drivers = drivers;
        this.table.renderRows();
      });
    });
  }
}
