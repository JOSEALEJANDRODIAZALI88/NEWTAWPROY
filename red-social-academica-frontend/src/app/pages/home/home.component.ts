// src/app/pages/home/home.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'app-home',
  standalone: true,
  template: `
    <h1>Bienvenido, Usuario</h1>
    <button (click)="logout()">Cerrar sesión</button>
  `
})
export class HomeComponent {
  constructor(private auth: AuthService) {}
  logout() { this.auth.logout(); }
}
