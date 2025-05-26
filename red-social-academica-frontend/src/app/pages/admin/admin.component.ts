// src/app/pages/admin/admin.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'app-admin',
  standalone: true,
  template: `
    <h1>Panel Administrador</h1>
    <button (click)="logout()">Cerrar sesión</button>
  `
})
export class AdminComponent {
  constructor(private auth: AuthService) {}
  logout() { this.auth.logout(); }
}
