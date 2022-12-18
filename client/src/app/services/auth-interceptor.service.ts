import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UsernameStorageService } from './username-storage.service';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private usernameService: UsernameStorageService) { 
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    const username = this.usernameService.getUsername();
    if (username) {
      authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, username) });
    }

    return next.handle(authReq);
  }
}

export const authInterceptorService = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
];