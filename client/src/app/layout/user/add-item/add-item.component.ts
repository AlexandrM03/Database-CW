import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OrderItem } from 'src/app/models/OrderItem';
import { UserServiceService } from 'src/app/services/user-service.service';

@Component({
  selector: 'app-add-item',
  templateUrl: './add-item.component.html',
  styleUrls: ['./add-item.component.css']
})
export class AddItemComponent implements OnInit {
  public addItemForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<AddItemComponent>,
              private fb: FormBuilder,
              private userService: UserServiceService,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.addItemForm = this.createAddItemForm();
  }

  createAddItemForm(): FormGroup {
    return this.fb.group({
      name: [
        this.data.orderItem.name,
        Validators.compose([Validators.required])
      ],
      weight: [
        this.data.orderItem.weight,
        Validators.compose([Validators.required, Validators.pattern('^\\d*\\.?\\d+$')])
      ],
      volume: [
        this.data.orderItem.volume,
        Validators.compose([Validators.required, Validators.pattern('^\\d*\\.?\\d+$')])
      ]
    });
  }

  submit(): void {
    this.userService.addOrderItem(this.data.orderId, this.addItem()).subscribe(data => {
      this.dialogRef.close();
    });
  }

  addItem(): OrderItem {
    this.data.orderItem.name = this.addItemForm.value.name;
    this.data.orderItem.weight = this.addItemForm.value.weight;
    this.data.orderItem.volume = this.addItemForm.value.volume;
    return this.data.orderItem;
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
