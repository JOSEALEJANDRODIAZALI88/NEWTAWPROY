// red-social-academica-frontend/src/app/pages/signup/signup.component.ts

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors
} from '@angular/forms';
import { RouterModule, Router } from '@angular/router';    // ← Importar RouterModule + Router
import { AuthService, SignupDTO } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule      // ← Añadir RouterModule aquí
  ],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  form: FormGroup;
  errorMsg = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.nonNullable.group({
      name:              ['', Validators.required],
      lastName:          ['', Validators.required],
      username:          ['', Validators.required],
      email:             ['', [Validators.required, Validators.email]],
      profilePictureUrl: [''],
      bio:               [''],
      career:            [''],
      birthdate:         ['', Validators.required],
      password:          ['', [Validators.required, Validators.minLength(6)]],
      passwordConfirm:   ['', Validators.required],
    }, {
      validators: this.mustMatch('password', 'passwordConfirm')
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMsg = '';
    const dto: SignupDTO = this.form.value;
    this.auth.signup(dto).subscribe({
      next: () => this.router.navigate(['/login']),
      error: err => {
        this.errorMsg = err.error?.message || 'Error de registro';
        this.loading = false;
      }
    });
  }

  private mustMatch(controlName: string, matchingControlName: string) {
    return (group: AbstractControl): ValidationErrors | null => {
      const formGroup = group as FormGroup;
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
        return { mustMatch: true };
      } else {
        if (matchingControl.errors?.['mustMatch']) {
          delete matchingControl.errors['mustMatch'];
          if (!Object.keys(matchingControl.errors!).length) {
            matchingControl.setErrors(null);
          }
        }
        return null;
      }
    };
  }
}
