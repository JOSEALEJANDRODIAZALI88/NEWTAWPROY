import { Component }    from '@angular/core';
import { Router }       from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  constructor(private router: Router) {}

  login() {
    // aquí iría tu lógica de autenticación…
    this.router.navigate(['/home']);
  }
}
