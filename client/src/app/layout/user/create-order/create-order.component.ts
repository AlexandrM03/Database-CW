import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UserServiceService } from 'src/app/services/user-service.service';

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.css']
})
export class CreateOrderComponent implements OnInit {
  countriesFrom: string[];
  countriesTo: string[];
  isCountriesLoaded: boolean = false;
  citiesFrom: string[];
  citiesTo: string[];

  selectedPointFrom: string;
  selectedPointTo: string;

  orderCreationForm: FormGroup;

  constructor(private userService: UserServiceService,
              private fb: FormBuilder,
              private dialogRef: MatDialogRef<CreateOrderComponent>) { }

  ngOnInit(): void {
    this.orderCreationForm = this.createOrderForm();
    this.userService.getCountries().subscribe((data: string[]) => {
      this.countriesFrom = data;
      this.countriesTo = data;
    });
    this.isCountriesLoaded = true;
  }

  createOrderForm(): FormGroup {
    return this.fb.group({
      startPoint: [
        this.selectedPointFrom
      ],
      endPoint: [
        this.selectedPointTo
      ],
    });
  }

  getCities(country: any, point: string): void {
    console.log(country, point);
    if (country) {
      this.userService.getCities(country.value).subscribe((data: string[]) => {
        if (point === 'start') {
          this.citiesFrom = data;
        } else {
          this.citiesTo = data;
        }
      });
    }
  }

  setCity(city: any, point: string): void {
    console.log(city, point);
    if (point === 'start') {
      this.selectedPointFrom = city.value;
    } else {
      this.selectedPointTo = city.value;
    }
    console.log(this.selectedPointFrom, this.selectedPointTo);
  }

  submit(): void {
    this.userService.createOrder(
      this.selectedPointFrom,
      this.selectedPointTo
    ).subscribe({
      next: () => {
        this.closeDialog();
      },
      error: err => {
        console.log(err);
      }
    });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
