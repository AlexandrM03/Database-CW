import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { DriverService } from 'src/app/services/driver.service';
import { NotificationService } from 'src/app/services/notification.service';
import { UsernameStorageService } from 'src/app/services/username-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;
  isDriver: boolean = false;

  constructor(private authService: AuthService,
              private usernameStorage: UsernameStorageService,
              private router: Router,
              private fb: FormBuilder,
              private driverService: DriverService,
              private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.loginForm = this.createLoginForm();
  }

  createLoginForm(): FormGroup {
    return this.fb.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])]
    });
  }

  submit(): void {
    if (this.isDriver) {
      this.driverService.login({
        username: this.loginForm.value.username,
        password: this.loginForm.value.password
      }).subscribe({
        next: data => {
          console.log(data);
          this.usernameStorage.saveUsername(data.username);
          this.router.navigate(['/accepted-orders']);
        },
        error: err => {
          this.notificationService.showSnackBar('Invalid username or password');
        }
      });
      return;
    }

    this.authService.login({
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    }).subscribe({
      next: data => {
        console.log(data);
        
        if (data.roleName === 'admin') {
          this.usernameStorage.saveRole(data.roleName);
          this.router.navigate(['/users/1']);
        } else {
          this.usernameStorage.saveUsername(data.username);
          this.router.navigate(['/index']);
        }
      },
      error: err => {
        this.notificationService.showSnackBar('Invalid username or password');
      }
    });
  }
}
