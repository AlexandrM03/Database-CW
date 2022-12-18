import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { User } from 'src/app/models/User';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {
  users: User[];
  isUsersLoaded: boolean = false;
  page: number;

  constructor(private adminService: AdminService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.page = params['page'];
      this.adminService.getAllUsersPagination(this.page).subscribe(users => {
        this.users = users;
        this.isUsersLoaded = true;
      });
    });
  }
}
