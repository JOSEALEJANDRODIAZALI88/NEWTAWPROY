import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

export interface LoginDTO {
  username: string;
  password: string;
}

export interface SignupDTO {
  name: string;
  lastName: string;
  username: string;
  email: string;
  profilePictureUrl?: string;
  bio?: string;
  career?: string;
  birthdate: string;       // ISO yyyy-MM-dd
  password: string;
  passwordConfirm: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_AUTH = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  login(dto: LoginDTO): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(
      `${this.API_AUTH}/login`,
      dto
    );
  }

  signup(dto: SignupDTO): Observable<any> {
    return this.http.post(
      `${this.API_AUTH}/signup`,
      dto
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
