import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsernameStorageService } from 'src/app/services/username-storage.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
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
