import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from 'src/app/models/User';
import { UserServiceService } from 'src/app/services/user-service.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  public profileEditForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<EditUserComponent>,
              private fb: FormBuilder,
              private userService: UserServiceService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
}

  ngOnInit(): void {
    this.profileEditForm = this.createProfileForm();
  }

  createProfileForm(): FormGroup {
    return this.fb.group({
      firstName: [
        this.data.user.firstName,
        Validators.compose([Validators.required])
      ],
      lastName: [
        this.data.user.lastName,
        Validators.compose([Validators.required])
      ],
      telephone: [
        this.data.user.telephone,
        Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void {
    this.userService.editUserDetails(this.updateUser()).subscribe({
      next: () => {
        this.closeDialog();
      },
      error: err => {
        console.log(err);
      }
    });
  }

  public updateUser(): User {
    this.data.user.firstName = this.profileEditForm.value.firstName;
    this.data.user.lastName = this.profileEditForm.value.lastName;
    this.data.user.telephone = this.profileEditForm.value.telephone;
    return this.data.user;
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
