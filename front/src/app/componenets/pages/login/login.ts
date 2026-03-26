import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'page-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, Footer],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  fb = inject(FormBuilder);
  api = inject(ApiService);
  auth = inject(AuthService);
  router = inject(Router);
  route = inject(ActivatedRoute);

  form = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', [Validators.required]],
  });

  loading = false;
  error = '';
  showPassword = false;

  refer(): string {
    return this.route.snapshot.queryParams['refer'] || '/';
  }

  submit() {
    if (this.form.invalid || this.loading) return;
    this.loading = true;
    this.error = '';
    const { username, password } = this.form.value;
    this.api.login(username!, password!).subscribe({
      next: (res) => {
        this.auth.saveToken(res.token);
        // fetch user info - we'd need to decode JWT or call /me
        this.loading = false;
        this.router.navigate([this.refer()]);
      },
      error: () => {
        this.error = 'Credenciales incorrectas. Inténtalo de nuevo.';
        this.loading = false;
      }
    });
  }
}
