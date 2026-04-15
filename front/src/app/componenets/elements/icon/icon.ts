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
  | 'casa';

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

      </ng-container>
    </svg>
  `,
})
export class AppIcon {
  @Input() icon!: IconName;
  @Input() size: number = 20;
}
