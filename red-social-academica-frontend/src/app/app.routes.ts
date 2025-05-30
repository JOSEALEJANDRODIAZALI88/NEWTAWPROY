// src/app/app.routes.ts
import { Routes }        from '@angular/router';
import { LoginComponent }  from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { HomeComponent }   from './pages/home/home.component';
import { AdminComponent }  from './pages/admin/admin.component';

export const routes: Routes = [
  { path: '',      redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',  component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'home',   component: HomeComponent },
  { path: 'admin',  component: AdminComponent },
  { path: '**',     redirectTo: 'login' }
];
