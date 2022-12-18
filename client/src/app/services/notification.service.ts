import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(private snackbar: MatSnackBar) {
  }

  public showSnackBar(message: string) {
    this.snackbar.open(message , 'OK', {
      duration: 3000,
    });
  }
}
