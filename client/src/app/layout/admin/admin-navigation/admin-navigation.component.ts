import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsernameStorageService } from 'src/app/services/username-storage.service';

@Component({
  selector: 'app-admin-navigation',
  templateUrl: './admin-navigation.component.html',
  styleUrls: ['./admin-navigation.component.css']
})
export class AdminNavigationComponent implements OnInit {

  constructor(private usernameStorage: UsernameStorageService,
              private router: Router) { 
  }

  ngOnInit(): void {
  }

  logout(): void {
    this.usernameStorage.logOut();
    this.router.navigate(['/login']);
  }
}
