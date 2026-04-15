import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

@Component({
  selector: 'page-recovery-password',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, Footer],
  template: `
    <app-header></app-header>
    <div class="page-wrapper auth-page">
      <div class="container" style="max-width:460px">
        <div class="auth-card">
          <div class="auth-brand">
            <img src="/assets/media/icon-dark.png" alt="" style="width:56px;height:56px;object-fit:contain;margin-bottom:1rem">
            <h1>Recuperar contraseña</h1>
            <p style="color:var(--text-2);font-size:.9rem">Introduce tu email y te enviaremos instrucciones</p>
          </div>
          @if (!sent) {
            <form [formGroup]="form" (ngSubmit)="submit()" style="display:flex;flex-direction:column;gap:1rem;margin-top:1.5rem">
              <div class="form-group"><label class="label">Email</label><input type="email" formControlName="email" placeholder="tu@email.com"></div>
              <button type="submit" class="btn btn-primary" style="width:100%;justify-content:center;padding:.9rem">Enviar instrucciones</button>
              <p style="text-align:center;font-size:.875rem"><a routerLink="/login" style="color:var(--primary)">Volver al login</a></p>
            </form>
          } @else {
            <div style="text-align:center;padding:2rem 0;color:var(--success)">✓ Email enviado (funcionalidad no implementada en demo)</div>
          }
        </div>
      </div>
    </div>
    <app-footer></app-footer>
  `,
  styles: [`.auth-card{background:var(--surface);border:1px solid var(--border);border-radius:var(--radius-lg);padding:2.5rem}.auth-brand{text-align:center}`],
})
export class RecoveryPassword {
  fb = inject(FormBuilder);
  form = this.fb.group({ email: ['', [Validators.required, Validators.email]] });
  sent = false;
  submit() { if (this.form.valid) this.sent = true; }
}
