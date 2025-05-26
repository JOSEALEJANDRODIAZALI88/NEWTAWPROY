import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

export interface LoginDTO  { username: string; password: string; }
export interface SignupDTO { username: string; email: string; password: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = 'http://localhost:3000/api';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(dto: LoginDTO): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.api}/login`, dto);
  }

  signup(dto: SignupDTO): Observable<any> {
    return this.http.post(`${this.api}/signup`, dto);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
