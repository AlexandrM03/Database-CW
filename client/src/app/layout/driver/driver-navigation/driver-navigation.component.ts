import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsernameStorageService } from 'src/app/services/username-storage.service';

@Component({
  selector: 'app-driver-navigation',
  templateUrl: './driver-navigation.component.html',
  styleUrls: ['./driver-navigation.component.css']
})
export class DriverNavigationComponent implements OnInit {
  username: string;
  isLoggedIn: boolean = false;

  constructor(private usernameStorage: UsernameStorageService,
              private router: Router) { }

  ngOnInit(): void {
    this.username = this.usernameStorage.getUsername();
    this.isLoggedIn = true;
  }

  logOut(): void {
    this.usernameStorage.logOut();
    this.router.navigate(['/login']);
  }
}
