// src/app/services/auth.service.ts
import { Injectable }    from '@angular/core';
import { HttpClient }    from '@angular/common/http';
import { Observable }    from 'rxjs';
import * as jwt_decode   from 'jwt-decode';

interface LoginResponse {
  token: string;
  // …otros campos que devuelva tu backend (id, username, email, roles, etc.)
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private API_URL = 'http://localhost:8080/api/auth';

  constructor(private _http: HttpClient) {}

  login(payload: { username: string; password: string; }): Observable<LoginResponse> {
    return this._http.post<LoginResponse>(`${this.API_URL}/login`, payload);
  }

  signup(payload: any): Observable<any> {
    return this._http.post<any>(`${this.API_URL}/signup`, payload);
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];
    const decoded = (jwt_decode as any)(token) as { roles: string[] };
    return decoded.roles || [];
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }
}
