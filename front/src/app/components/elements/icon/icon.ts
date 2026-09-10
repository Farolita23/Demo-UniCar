import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export type IconName =
  | 'birrete'
  | 'eco'
  | 'coche'
  | 'pin'
  | 'reloj'
  | 'asiento'
  | 'busqueda'
  | 'calendario'
  | 'estrella'
  | 'casa'
  | 'lupa'
  | 'usuario'
  | 'admin'
  | 'logout'
  | 'ajustes'
  | 'repetir'
  | 'papelera'
  | 'ojo'
  | 'ojo-cerrado'
  | 'moneda'
  | 'check'
  | 'cerrar'
  | 'bandera'
  | 'prohibido'
  | 'rayo'
  | 'camara'
  | 'alerta'
  | 'documento';

@Component({
  selector: 'app-icon',
  standalone: true,
  imports: [CommonModule],
  template: `
    <svg
      [attr.width]="size"
      [attr.height]="size"
      viewBox="0 0 48 48"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      [style.display]="'inline-block'"
      [style.vertical-align]="'middle'"
    >
      <ng-container [ngSwitch]="icon">

        <!-- BIRRETE -->
        <ng-container *ngSwitchCase="'birrete'">
          <polygon points="24,8 44,18 24,28 4,18" fill="#A32D2D"/>
          <polygon points="24,8 44,18 24,28 4,18" fill="#791F1F" opacity="0.3"/>
          <path d="M14 22v10c0 3 4.5 6 10 6s10-3 10-6V22L24 28Z" fill="#791F1F"/>
          <path d="M14 22v10c0 3 4.5 6 10 6s10-3 10-6V22L24 28Z" fill="#A32D2D" opacity="0.4"/>
          <line x1="44" y1="18" x2="44" y2="30" stroke="#501313" stroke-width="2.5" stroke-linecap="round"/>
          <circle cx="44" cy="31" r="2.5" fill="#E24B4A"/>
        </ng-container>

        <!-- ECO / HOJA -->
        <ng-container *ngSwitchCase="'eco'">
          <path d="M38 6C38 6 12 8 8 30c0 0 8-8 18-8" fill="#A32D2D"/>
          <path d="M38 6C38 6 12 8 8 30c0 0 8-8 18-8" fill="#791F1F" opacity="0.35"/>
          <path d="M26 22C26 22 30 30 24 40" stroke="#501313" stroke-width="2.5" stroke-linecap="round" fill="none"/>
          <path d="M18 28C16 36 20 42 24 40" stroke="#791F1F" stroke-width="2" stroke-linecap="round" fill="none"/>
          <circle cx="38" cy="6" r="3" fill="#E24B4A"/>
        </ng-container>

        <!-- COCHE -->
        <ng-container *ngSwitchCase="'coche'">
          <path d="M6 28h36v6a2 2 0 01-2 2H8a2 2 0 01-2-2v-6Z" fill="#791F1F"/>
          <path d="M10 28l4-10h20l4 10Z" fill="#A32D2D"/>
          <path d="M14 18h20l-2-4H16l-2 4Z" fill="#791F1F" opacity="0.5"/>
          <rect x="15" y="20" width="8" height="6" rx="2" fill="#F7C1C1" opacity="0.85"/>
          <rect x="25" y="20" width="8" height="6" rx="2" fill="#F7C1C1" opacity="0.85"/>
          <circle cx="14" cy="36" r="4" fill="#501313"/>
          <circle cx="14" cy="36" r="2" fill="#F09595"/>
          <circle cx="34" cy="36" r="4" fill="#501313"/>
          <circle cx="34" cy="36" r="2" fill="#F09595"/>
        </ng-container>

        <!-- PIN UBICACIÓN -->
        <ng-container *ngSwitchCase="'pin'">
          <path d="M24 6C16.27 6 10 12.27 10 20c0 10 14 26 14 26S38 30 38 20c0-7.73-6.27-14-14-14Z" fill="#A32D2D"/>
          <path d="M24 6C16.27 6 10 12.27 10 20c0 10 14 26 14 26S38 30 38 20c0-7.73-6.27-14-14-14Z" fill="#791F1F" opacity="0.3"/>
          <circle cx="24" cy="20" r="6" fill="#FCEBEB"/>
          <circle cx="24" cy="20" r="3" fill="#A32D2D"/>
        </ng-container>

        <!-- RELOJ -->
        <ng-container *ngSwitchCase="'reloj'">
          <circle cx="24" cy="24" r="18" fill="#A32D2D"/>
          <circle cx="24" cy="24" r="18" fill="#791F1F" opacity="0.25"/>
          <circle cx="24" cy="24" r="14" fill="#FCEBEB" opacity="0.12"/>
          <line x1="24" y1="24" x2="24" y2="13" stroke="#FCEBEB" stroke-width="2.5" stroke-linecap="round"/>
          <line x1="24" y1="24" x2="32" y2="28" stroke="#FCEBEB" stroke-width="2" stroke-linecap="round"/>
          <circle cx="24" cy="24" r="2.5" fill="#FCEBEB"/>
          <circle cx="24" cy="10" r="1.5" fill="#F09595"/>
          <circle cx="24" cy="38" r="1.5" fill="#F09595"/>
          <circle cx="10" cy="24" r="1.5" fill="#F09595"/>
          <circle cx="38" cy="24" r="1.5" fill="#F09595"/>
        </ng-container>

        <!-- ASIENTO / PERSONA -->
        <ng-container *ngSwitchCase="'asiento'">
          <circle cx="24" cy="13" r="7" fill="#A32D2D"/>
          <path d="M14 30c0-5.52 4.48-10 10-10s10 4.48 10 10v2H14v-2Z" fill="#A32D2D"/>
          <rect x="12" y="32" width="24" height="5" rx="2.5" fill="#791F1F"/>
          <rect x="10" y="37" width="5" height="6" rx="2" fill="#791F1F"/>
          <rect x="33" y="37" width="5" height="6" rx="2" fill="#791F1F"/>
        </ng-container>

        <!-- BÚSQUEDA -->
        <ng-container *ngSwitchCase="'busqueda'">
          <circle cx="22" cy="21" r="12" fill="#A32D2D"/>
          <circle cx="22" cy="21" r="8" fill="#791F1F" opacity="0.4"/>
          <circle cx="22" cy="21" r="5" fill="#FCEBEB" opacity="0.15"/>
          <line x1="30.5" y1="30.5" x2="40" y2="40" stroke="#501313" stroke-width="4" stroke-linecap="round"/>
        </ng-container>

        <!-- CALENDARIO -->
        <ng-container *ngSwitchCase="'calendario'">
          <rect x="7" y="12" width="34" height="30" rx="5" fill="#A32D2D"/>
          <rect x="7" y="12" width="34" height="12" rx="5" fill="#791F1F"/>
          <rect x="7" y="20" width="34" height="4" fill="#791F1F"/>
          <line x1="16" y1="8" x2="16" y2="18" stroke="#501313" stroke-width="3" stroke-linecap="round"/>
          <line x1="32" y1="8" x2="32" y2="18" stroke="#501313" stroke-width="3" stroke-linecap="round"/>
          <rect x="14" y="30" width="6" height="6" rx="1.5" fill="#F09595" opacity="0.8"/>
          <rect x="22" y="30" width="6" height="6" rx="1.5" fill="#F09595" opacity="0.8"/>
          <rect x="30" y="30" width="6" height="6" rx="1.5" fill="#F09595" opacity="0.5"/>
        </ng-container>

        <!-- ESTRELLA -->
        <ng-container *ngSwitchCase="'estrella'">
          <polygon points="24,6 29,18 42,18 32,26 36,38 24,30 12,38 16,26 6,18 19,18" fill="#A32D2D"/>
          <polygon points="24,6 29,18 42,18 32,26 36,38 24,30 12,38 16,26 6,18 19,18" fill="#791F1F" opacity="0.3"/>
          <polygon points="24,12 28,20 36,20 30,25 32,33 24,28 16,33 18,25 12,20 20,20" fill="#E24B4A" opacity="0.6"/>
        </ng-container>

        <!-- CASA (desde campus) -->
        <ng-container *ngSwitchCase="'casa'">
          <path d="M24 8L42 24H36v14a2 2 0 01-2 2H14a2 2 0 01-2-2V24H6L24 8Z" fill="#A32D2D"/>
          <path d="M24 8L42 24H36v14a2 2 0 01-2 2H14a2 2 0 01-2-2V24H6L24 8Z" fill="#791F1F" opacity="0.3"/>
          <rect x="19" y="28" width="10" height="12" rx="2" fill="#F09595" opacity="0.7"/>
          <rect x="14" y="24" width="8" height="7" rx="1.5" fill="#F09595" opacity="0.5"/>
          <rect x="26" y="24" width="8" height="7" rx="1.5" fill="#F09595" opacity="0.5"/>
        </ng-container>
        <!-- LUPA -->
        <ng-container *ngSwitchCase="'lupa'">
          <circle cx="21" cy="21" r="13" fill="#A32D2D"/>
          <circle cx="21" cy="21" r="13" fill="#791F1F" opacity="0.25"/>
          <circle cx="21" cy="21" r="9"  fill="#791F1F" opacity="0.35"/>
          <circle cx="21" cy="21" r="5.5" fill="#FCEBEB" opacity="0.13"/>
          <circle cx="17" cy="17" r="3.5" fill="#FCEBEB" opacity="0.08"/>
          <line x1="30.5" y1="30.5" x2="40.5" y2="40.5"
                stroke="#501313" stroke-width="4.5" stroke-linecap="round"/>
          <line x1="30.5" y1="30.5" x2="40.5" y2="40.5"
                stroke="#791F1F" stroke-width="2" stroke-linecap="round" opacity="0.4"/>
          <circle cx="38" cy="10" r="2.5" fill="#E24B4A"/>
        </ng-container>
        <!-- USUARIO -->
<ng-container *ngSwitchCase="'usuario'">
  <circle cx="24" cy="18" r="9" fill="#FCEBEB"/>
  <path d="M10 42 C10 30 38 30 38 42" fill="#FCEBEB"/>
  <circle cx="24" cy="18" r="11" fill="#A32D2D"/>
  <circle cx="24" cy="18" r="11" fill="#791F1F" opacity="0.25"/>
  <circle cx="24" cy="17" r="7" fill="#FCEBEB"/>
  <path d="M13 36 C13 27 35 27 35 36" fill="#FCEBEB"/>
  <path d="M13 36 C13 27 35 27 35 36" fill="#F09595" opacity="0.3"/>
</ng-container>

<!-- ADMIN -->
<ng-container *ngSwitchCase="'admin'">
  <path d="M24 4 L42 12 L42 24 C42 34 34 41 24 44 C14 41 6 34 6 24 L6 12 Z" fill="#A32D2D"/>
  <path d="M24 4 L42 12 L42 24 C42 34 34 41 24 44 C14 41 6 34 6 24 L6 12 Z" fill="#791F1F" opacity="0.25"/>
  <path d="M24 10 L38 17 L38 24 C38 31 32 37 24 40 C16 37 10 31 10 24 L10 17 Z" fill="#791F1F" opacity="0.3"/>
  <polygon points="24,13 27,21 36,21 29,26 32,34 24,29 16,34 19,26 12,21 21,21" fill="#FCEBEB" opacity="0.9"/>
</ng-container>

<!-- LOGOUT -->
<ng-container *ngSwitchCase="'logout'">
  <rect x="6" y="8" width="22" height="32" rx="4" fill="#A32D2D"/>
  <rect x="6" y="8" width="22" height="32" rx="4" fill="#501313" opacity="0.3"/>
  <circle cx="17" cy="24" r="2.5" fill="#F09595" opacity="0.7"/>
  <rect x="26" y="8" width="4" height="32" fill="#791F1F" opacity="0.5"/>
  <line x1="30" y1="24" x2="43" y2="24" stroke="#A32D2D" stroke-width="2.5" stroke-linecap="round"/>
  <polyline points="37,18 43,24 37,30" fill="none" stroke="#A32D2D" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
</ng-container>

<!-- AJUSTES / ENGRANAJE -->
<ng-container *ngSwitchCase="'ajustes'">
  <circle cx="24" cy="24" r="11" fill="#A32D2D"/>
  <circle cx="24" cy="24" r="11" fill="#791F1F" opacity="0.28"/>
  <circle cx="24" cy="6"  r="3.5" fill="#A32D2D"/>
  <circle cx="24" cy="42" r="3.5" fill="#A32D2D"/>
  <circle cx="6"  cy="24" r="3.5" fill="#A32D2D"/>
  <circle cx="42" cy="24" r="3.5" fill="#A32D2D"/>
  <circle cx="11.5" cy="11.5" r="3.5" fill="#A32D2D"/>
  <circle cx="36.5" cy="11.5" r="3.5" fill="#A32D2D"/>
  <circle cx="11.5" cy="36.5" r="3.5" fill="#A32D2D"/>
  <circle cx="36.5" cy="36.5" r="3.5" fill="#A32D2D"/>
  <circle cx="24" cy="24" r="4.5" fill="#FCEBEB"/>
</ng-container>

<!-- REPETIR / PERIÓDICO -->
<ng-container *ngSwitchCase="'repetir'">
  <path d="M10 21a14 14 0 0 1 24-8" fill="none" stroke="#A32D2D" stroke-width="4" stroke-linecap="round"/>
  <polygon points="34,5 37,15 27,14" fill="#A32D2D"/>
  <path d="M38 27a14 14 0 0 1-24 8" fill="none" stroke="#A32D2D" stroke-width="4" stroke-linecap="round"/>
  <polygon points="14,43 11,33 21,34" fill="#A32D2D"/>
  <path d="M38 27a14 14 0 0 1-24 8" fill="none" stroke="#791F1F" stroke-width="1.6" stroke-linecap="round" opacity="0.4"/>
</ng-container>

<!-- PAPELERA -->
<ng-container *ngSwitchCase="'papelera'">
  <rect x="12" y="14" width="24" height="28" rx="3" fill="#A32D2D"/>
  <rect x="12" y="14" width="24" height="28" rx="3" fill="#791F1F" opacity="0.25"/>
  <rect x="8" y="9" width="32" height="5" rx="2.5" fill="#791F1F"/>
  <rect x="19" y="4" width="10" height="6" rx="2" fill="#791F1F"/>
  <rect x="17" y="20" width="3.5" height="16" rx="1.75" fill="#FCEBEB" opacity="0.85"/>
  <rect x="22.25" y="20" width="3.5" height="16" rx="1.75" fill="#FCEBEB" opacity="0.85"/>
  <rect x="27.5" y="20" width="3.5" height="16" rx="1.75" fill="#FCEBEB" opacity="0.85"/>
</ng-container>

<!-- OJO -->
<ng-container *ngSwitchCase="'ojo'">
  <path d="M4 24s8-13 20-13 20 13 20 13-8 13-20 13S4 24 4 24Z" fill="#A32D2D"/>
  <path d="M4 24s8-13 20-13 20 13 20 13-8 13-20 13S4 24 4 24Z" fill="#791F1F" opacity="0.3"/>
  <circle cx="24" cy="24" r="7" fill="#FCEBEB"/>
  <circle cx="24" cy="24" r="3.5" fill="#A32D2D"/>
</ng-container>

<!-- OJO CERRADO -->
<ng-container *ngSwitchCase="'ojo-cerrado'">
  <path d="M4 24s8-13 20-13 20 13 20 13-8 13-20 13S4 24 4 24Z" fill="#A32D2D" opacity="0.45"/>
  <circle cx="24" cy="24" r="7" fill="#FCEBEB" opacity="0.55"/>
  <circle cx="24" cy="24" r="3.5" fill="#A32D2D" opacity="0.55"/>
  <line x1="8" y1="40" x2="40" y2="8" stroke="#501313" stroke-width="4" stroke-linecap="round"/>
</ng-container>

<!-- MONEDA / PRECIO -->
<ng-container *ngSwitchCase="'moneda'">
  <circle cx="24" cy="24" r="18" fill="#A32D2D"/>
  <circle cx="24" cy="24" r="18" fill="#791F1F" opacity="0.25"/>
  <circle cx="24" cy="24" r="13" fill="#791F1F" opacity="0.3"/>
  <path d="M31 16a9 9 0 1 0 0 16" fill="none" stroke="#FCEBEB" stroke-width="3" stroke-linecap="round"/>
  <line x1="15" y1="22" x2="28" y2="22" stroke="#FCEBEB" stroke-width="3" stroke-linecap="round"/>
  <line x1="15" y1="27" x2="28" y2="27" stroke="#FCEBEB" stroke-width="3" stroke-linecap="round"/>
</ng-container>

<!-- CHECK / OK -->
<ng-container *ngSwitchCase="'check'">
  <circle cx="24" cy="24" r="18" fill="#A32D2D"/>
  <circle cx="24" cy="24" r="18" fill="#791F1F" opacity="0.25"/>
  <polyline points="15,25 21,31 33,17" fill="none" stroke="#FCEBEB" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/>
</ng-container>

<!-- CERRAR / X -->
<ng-container *ngSwitchCase="'cerrar'">
  <circle cx="24" cy="24" r="18" fill="#A32D2D"/>
  <circle cx="24" cy="24" r="18" fill="#791F1F" opacity="0.25"/>
  <line x1="17" y1="17" x2="31" y2="31" stroke="#FCEBEB" stroke-width="4" stroke-linecap="round"/>
  <line x1="31" y1="17" x2="17" y2="31" stroke="#FCEBEB" stroke-width="4" stroke-linecap="round"/>
</ng-container>

<!-- BANDERA / REPORTE -->
<ng-container *ngSwitchCase="'bandera'">
  <line x1="12" y1="6" x2="12" y2="43" stroke="#501313" stroke-width="3.5" stroke-linecap="round"/>
  <path d="M12 8h23l-6 8 6 8H12Z" fill="#A32D2D"/>
  <path d="M12 8h23l-6 8 6 8H12Z" fill="#791F1F" opacity="0.3"/>
</ng-container>

<!-- PROHIBIDO -->
<ng-container *ngSwitchCase="'prohibido'">
  <circle cx="24" cy="24" r="18" fill="#A32D2D"/>
  <circle cx="24" cy="24" r="18" fill="#791F1F" opacity="0.25"/>
  <circle cx="24" cy="24" r="12" fill="none" stroke="#FCEBEB" stroke-width="4"/>
  <line x1="15" y1="15" x2="33" y2="33" stroke="#FCEBEB" stroke-width="4" stroke-linecap="round"/>
</ng-container>

<!-- RAYO / STRIKE -->
<ng-container *ngSwitchCase="'rayo'">
  <polygon points="26,4 10,26 22,26 18,44 38,20 24,20" fill="#A32D2D"/>
  <polygon points="26,4 10,26 22,26 18,44 38,20 24,20" fill="#791F1F" opacity="0.3"/>
  <polygon points="24,12 16,24 24,24 22,34 32,21 25,21" fill="#E24B4A" opacity="0.55"/>
</ng-container>

<!-- CÁMARA -->
<ng-container *ngSwitchCase="'camara'">
  <rect x="5" y="14" width="38" height="26" rx="5" fill="#A32D2D"/>
  <rect x="5" y="14" width="38" height="26" rx="5" fill="#791F1F" opacity="0.25"/>
  <path d="M17 14l3-5h8l3 5Z" fill="#791F1F"/>
  <circle cx="24" cy="27" r="8" fill="#FCEBEB"/>
  <circle cx="24" cy="27" r="4" fill="#A32D2D"/>
  <circle cx="37" cy="19" r="1.8" fill="#F09595"/>
</ng-container>

<!-- ALERTA / AVISO -->
<ng-container *ngSwitchCase="'alerta'">
  <path d="M24 6 44 40H4Z" fill="#A32D2D"/>
  <path d="M24 6 44 40H4Z" fill="#791F1F" opacity="0.3"/>
  <rect x="21.5" y="18" width="5" height="12" rx="2.5" fill="#FCEBEB"/>
  <circle cx="24" cy="35" r="2.75" fill="#FCEBEB"/>
</ng-container>

<!-- DOCUMENTO / MATRÍCULA -->
<ng-container *ngSwitchCase="'documento'">
  <path d="M12 5h15l9 9v28a1 1 0 0 1-1 1H12a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1Z" fill="#A32D2D"/>
  <path d="M12 5h15l9 9v28a1 1 0 0 1-1 1H12a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1Z" fill="#791F1F" opacity="0.25"/>
  <path d="M27 5l9 9h-9Z" fill="#FCEBEB" opacity="0.5"/>
  <rect x="16" y="22" width="16" height="3" rx="1.5" fill="#FCEBEB" opacity="0.85"/>
  <rect x="16" y="29" width="16" height="3" rx="1.5" fill="#FCEBEB" opacity="0.85"/>
  <rect x="16" y="36" width="10" height="3" rx="1.5" fill="#FCEBEB" opacity="0.6"/>
</ng-container>

      </ng-container>
    </svg>
  `,
})
export class AppIcon {
  @Input() icon!: IconName;
  @Input() size: number = 20;
}
