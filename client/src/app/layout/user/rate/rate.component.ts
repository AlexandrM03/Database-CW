import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Rate } from 'src/app/models/Rate';
import { UserServiceService } from 'src/app/services/user-service.service';

@Component({
  selector: 'app-rate',
  templateUrl: './rate.component.html',
  styleUrls: ['./rate.component.css']
})
export class RateComponent implements OnInit {
  public rateForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<RateComponent>,
              private fb: FormBuilder,
              private userService: UserServiceService,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.rateForm = this.createRateForm();
  }

  createRateForm(): FormGroup {
    return this.fb.group({
      rating: [
        this.data.rate.rate,
        Validators.compose([Validators.required, Validators.min(1), Validators.max(5)])
      ],
      message: [
        this.data.rate.message,
        Validators.compose([Validators.required, Validators.maxLength(300)])
      ]
    });
  }

  submit(): void {
    this.userService.rateDriver(this.createRate()).subscribe(() => {
      this.closeDialog();
    });
  }

  createRate(): Rate {
    this.data.rate.rate = this.rateForm.value.rating;
    this.data.rate.message = this.rateForm.value.message;
    return this.data.rate;
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
