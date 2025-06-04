// src/app/pages/admin/admin-usuarios-listar.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-usuarios-listar',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Listar Usuarios</h3>
    <p>Aquí iría la tabla o lista de todos los usuarios.</p>
  `
})
export class AdminUsuariosListarComponent { }
