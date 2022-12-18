import { Component, OnInit } from '@angular/core';
import { Driver } from 'src/app/models/Driver';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DriverService } from 'src/app/services/driver.service';
import { DriverEditComponent } from '../driver-edit/driver-edit.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-driver-profile',
  templateUrl: './driver-profile.component.html',
  styleUrls: ['./driver-profile.component.css']
})
export class DriverProfileComponent implements OnInit {
  driver: Driver;
  isDataLoaded: boolean = false;
  passwordForm: FormGroup;
  password: string = '';

  constructor(private driverService: DriverService,
              private dialog: MatDialog,
              private fb: FormBuilder) { }

  ngOnInit(): void {
    this.driverService.getDriverData().subscribe({
      next: (driver: Driver) => {
        this.driver = driver;
        this.isDataLoaded = true;
      }
    });
    this.passwordForm = this.createPasswordForm();
  }

  createPasswordForm(): FormGroup {
    return this.fb.group({
      password: [
        this.password,
        Validators.compose([Validators.required, Validators.minLength(3)])
      ]
    });
  }

  submit(): void {
    this.driverService.updatePassword(this.passwordForm.value.password).subscribe({
      next: () => {
        this.passwordForm.reset();
      },
      error: err => {
        console.log(err);
      }
    });
  }

  editDriver(): void {
    const dialogUserEditConfig = new MatDialogConfig();
    dialogUserEditConfig.width = '400px';
    dialogUserEditConfig.data = {
      driver: this.driver
    };

    this.dialog.open(DriverEditComponent, dialogUserEditConfig);
  }
}
