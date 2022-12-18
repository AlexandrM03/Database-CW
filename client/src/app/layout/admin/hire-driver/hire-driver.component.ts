import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-hire-driver',
  templateUrl: './hire-driver.component.html',
  styleUrls: ['./hire-driver.component.css']
})
export class HireDriverComponent implements OnInit {
  username: string = '';
  password: string = '';

  hireForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<HireDriverComponent>,
              private fb: FormBuilder,
              private adminService: AdminService) { }

  ngOnInit(): void {
    this.hireForm = this.createHireForm();
  }

  createHireForm(): FormGroup {
    return this.fb.group({
      username: [
        this.username,
        Validators.compose([Validators.required, Validators.minLength(3)])
      ],
      password: [
        this.password,
        Validators.compose([Validators.required, Validators.minLength(3)])
      ]
    });
  }

  submit(): void {
    this.adminService.hireDriver(this.hireDriver()).subscribe(() => {
      this.closeDialog();
    });
  }

  hireDriver(): any {
    this.username = this.hireForm.value.username;
    this.password = this.hireForm.value.password;
    return {
      username: this.username,
      password: this.password
    }
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
