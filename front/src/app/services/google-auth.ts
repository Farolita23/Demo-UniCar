import { Injectable } from '@angular/core';
import { env } from '../../environments/env';

/** Datos que se pasan de login a signup cuando la cuenta de Google aún no está registrada. */
export interface GoogleSignupState {
  idToken: string;
  email: string;
  name: string;
  picture?: string;
}

declare const google: any;

@Injectable({ providedIn: 'root' })
export class GoogleAuthService {

  /**
   * Dibuja el botón oficial "Iniciar sesión con Google" en el contenedor indicado.
   * El script de Google se carga en index.html; si aún no está listo se reintenta.
   */
  renderButton(container: HTMLElement, onCredential: (idToken: string) => void, width = 320, tries = 0): void {
    if (typeof google === 'undefined' || !google?.accounts?.id) {
      if (tries < 50) setTimeout(() => this.renderButton(container, onCredential, width, tries + 1), 100);
      return;
    }
    google.accounts.id.initialize({
      client_id: env.GOOGLE_CLIENT_ID,
      callback: (res: { credential: string }) => onCredential(res.credential),
    });
    google.accounts.id.renderButton(container, { theme: 'outline', size: 'large', text: 'continue_with', shape: 'pill', width, locale: 'es' });
  }
}
