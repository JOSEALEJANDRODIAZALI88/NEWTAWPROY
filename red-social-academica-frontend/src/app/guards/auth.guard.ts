// src/app/guards/auth.guard.ts
import { Injectable }       from '@angular/core';
import {
  CanActivate, Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { AuthService }      from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (!this.auth.isLogged()) {
      this.router.navigate(['/login']);
      return false;
    }
    const roles = route.data['roles'] as string[]|undefined;
    const role  = this.auth.getRole();
    if (roles && !roles.includes(role!)) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
