import { Injectable } from '@angular/core';

const USERNAME_KEY = 'username';
const ROLE_KEY = 'role';

@Injectable({
  providedIn: 'root'
})
export class UsernameStorageService {

  constructor() { }

  public saveUsername(username: string): void {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public saveRole(role: string): void {
    window.sessionStorage.removeItem(ROLE_KEY);
    window.sessionStorage.setItem(ROLE_KEY, role);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY)!;
  }

  public getRole(): string {
    return sessionStorage.getItem(ROLE_KEY)!;
  }

  public logOut(): void {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.removeItem(ROLE_KEY);
  }
}
