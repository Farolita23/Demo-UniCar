import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

@Component({
  selector: 'page-reset-password',
  standalone: true,
  imports: [RouterLink, Header, Footer],
  template: `
    <app-header></app-header>
    <div class="page-wrapper" style="display:flex;align-items:center;padding:2rem 0">
      <div class="container" style="max-width:460px;text-align:center">
        <h1>Restablecer contraseña</h1>
        <p style="color:var(--text-2);margin:1rem 0">Esta funcionalidad requiere token de email.</p>
        <a routerLink="/login" class="btn btn-primary">Volver al login</a>
      </div>
    </div>
    <app-footer></app-footer>
  `,
})
export class ResetPassword {}
