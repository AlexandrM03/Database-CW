import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Driver } from 'src/app/models/Driver';
import { DriverService } from 'src/app/services/driver.service';

@Component({
  selector: 'app-driver-edit',
  templateUrl: './driver-edit.component.html',
  styleUrls: ['./driver-edit.component.css']
})
export class DriverEditComponent implements OnInit {
  driverEditForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<DriverEditComponent>,
              private fb: FormBuilder,
              private driverService: DriverService,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.driverEditForm = this.createDriverEditForm();
  }

  createDriverEditForm(): FormGroup {
    return this.fb.group({
      firstName: [
        this.data.driver.firstName,
        Validators.compose([Validators.required])
      ],
      lastName: [
        this.data.driver.lastName,
        Validators.compose([Validators.required])
      ],
      telephone: [
        this.data.driver.telephone,
        Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void {
    this.driverService.fulfill(this.updateDriver()).subscribe({
      next: () => {
        this.closeDialog();
      },
      error: err => {
        console.log(err);
      }
    });
  }

  updateDriver(): Driver {
    this.data.driver.firstName = this.driverEditForm.value.firstName;
    this.data.driver.lastName = this.driverEditForm.value.lastName;
    this.data.driver.telephone = this.driverEditForm.value.telephone;
    return this.data.driver;
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
