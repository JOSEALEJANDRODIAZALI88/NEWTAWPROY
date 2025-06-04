// src/app/app.routes.ts
import type { Routes } from '@angular/router';

// Login / Signup / Home / NoAutorizado
import { LoginComponent }         from './pages/login/login.component';
import { SignupComponent }        from './pages/signup/signup.component';
import { HomeComponent }          from './pages/home/home.component';
import { NoAutorizadoComponent }  from './pages/no-autorizado/no-autorizado.component';

// Admin “padre”
import { AdminComponent }         from './pages/admin/admin.component';

// Todos los componentes de “admin-usuarios” (nota el subdirectorio “admin-usuarios/”)
import { AdminUsuariosListarComponent }   from './pages/admin/admin-usuarios/admin-usuarios-listar.component';
import { AdminUsuariosCrearComponent }    from './pages/admin/admin-usuarios/admin-usuarios-crear.component';
import { AdminUsuariosEditarComponent }   from './pages/admin/admin-usuarios/admin-usuarios-editar.component';
import { AdminUsuariosBajaComponent }     from './pages/admin/admin-usuarios/admin-usuarios-baja.component';
import { AdminUsuariosPerfilComponent }   from './pages/admin/admin-usuarios/admin-usuarios-perfil.component';
import { AdminUsuariosPorRolComponent }   from './pages/admin/admin-usuarios/admin-usuarios-por-rol.component';
import { AdminUsuariosBuscarComponent }   from './pages/admin/admin-usuarios/admin-usuarios-buscar.component';

export const routes: Routes = [
  { path: '',        redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',   component: LoginComponent },
  { path: 'signup',  component: SignupComponent },
  { path: 'home',    component: HomeComponent },
  { path: 'no-auth', component: NoAutorizadoComponent },

  {
    path: 'admin',
    component: AdminComponent,
    children: [
      { path: 'usuarios/listar',           component: AdminUsuariosListarComponent },
      { path: 'usuarios/crear',            component: AdminUsuariosCrearComponent },
      { path: 'usuarios/editar/:username', component: AdminUsuariosEditarComponent },
      { path: 'usuarios/baja/:username',   component: AdminUsuariosBajaComponent },
      { path: 'usuarios/perfil/:username', component: AdminUsuariosPerfilComponent },
      { path: 'usuarios/por-rol/:role',    component: AdminUsuariosPorRolComponent },
      { path: 'usuarios/buscar',           component: AdminUsuariosBuscarComponent },
      { path: '', redirectTo: 'usuarios/listar', pathMatch: 'full' }
    ]
  },

  // Cualquier ruta no reconocida -> /no-auth
  { path: '**', redirectTo: 'no-auth' }
];
