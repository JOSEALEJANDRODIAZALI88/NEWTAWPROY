// red-social-academica-frontend/src/app/pages/login/login.component.ts

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule    // ← Necesario para usar router.navigate en plantilla
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  form: FormGroup;
  errorMsg = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    public router: Router
  ) {
    this.form = this.fb.nonNullable.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMsg = '';
    this.auth.login(this.form.value).subscribe({
      next: res => {
        localStorage.setItem('token', res.token);
        this.router.navigate(['/home']);
      },
      error: (err: any) => {
        this.errorMsg = err.error?.message || 'Credenciales inválidas';
        this.loading = false;
      }
    });
  }
}

