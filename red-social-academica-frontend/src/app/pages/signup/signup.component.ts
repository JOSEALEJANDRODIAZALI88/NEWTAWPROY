// src/app/pages/signup/signup.component.ts
import { Component }              from '@angular/core';
import { CommonModule }           from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router }                 from '@angular/router';
import { AuthService }            from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  form: FormGroup;
  loading = false;
  errorMsg = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username:        ['', Validators.required],
      ru:              ['', Validators.required],
      email:           ['', [Validators.required, Validators.email]],
      password:        ['', Validators.required],
      passwordConfirm: ['', Validators.required]
      // Si tu DTO pide más campos (name, lastName, profilePictureUrl, bio, career, birthdate),
      // agrégalos aquí y pásalos en el “payload” a signup().
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.errorMsg = 'Completa todos los campos obligatorios.';
      return;
    }
    if (this.form.value.password !== this.form.value.passwordConfirm) {
      this.errorMsg = 'Las contraseñas no coinciden.';
      return;
    }
    this.loading = true;
    this.errorMsg = '';

    const payload = {
      username:        this.form.value.username,
      ru:              this.form.value.ru,
      email:           this.form.value.email,
      password:        this.form.value.password,
      passwordConfirm: this.form.value.passwordConfirm,
      // Si tu backend espera otros campos, agrégalos aquí:
      name:             '',
      lastName:         '',
      profilePictureUrl: '',
      bio:              '',
      career:           '',
      birthdate:        null
    };

    this.auth.signup(payload).subscribe({
      next: res => {
        // Asumimos que el backend devuelve código 201 y crea el usuario con éxito.
        this.router.navigate(['/login']);
      },
      error: err => {
        this.errorMsg = err.error?.message || 'Error de registro';
        this.loading = false;
      }
    });
  }
}
