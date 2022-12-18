import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { User } from 'src/app/models/User';
import { UserServiceService } from 'src/app/services/user-service.service';
import { EditUserComponent } from '../edit-user/edit-user.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User;
  userDataLoaded: boolean = false;

  constructor(private userService: UserServiceService,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.userService.getUser().subscribe({
      next: (user: User) => {
        this.user = user;
        this.userDataLoaded = true;
      }
    });
  }

  editUser(): void {
    const dialogUserEditConfig = new MatDialogConfig();
    dialogUserEditConfig.width = '400px';
    dialogUserEditConfig.data = {
      user: this.user
    };

    this.dialog.open(EditUserComponent, dialogUserEditConfig);
  }
}
