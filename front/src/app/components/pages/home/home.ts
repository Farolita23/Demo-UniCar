/**
 * Componente de Página de Inicio (Home)
 * 
 * Página principal de la aplicación que muestra:
 * - Sección hero con llamada a la acción
 * - Pasos de cómo usar la plataforma
 * - Viajes sugeridos (personalizados o generales)
 * - Estadísticas de la plataforma
 * - Sección de call-to-action final
 */

// Importaciones de Angular core
import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

// Importaciones de componentes compartidos
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';

// Importaciones de servicios
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';

// Importaciones de modelos
import { Trip } from '../../../models/trip.model';

//Importacion iconos
import { AppIcon, IconName } from '../../elements/icon/icon';

/**
 * Componente Home
 * 
 * Página de inicio que muestra información sobre la plataforma,
 * características principales y viajes sugeridos personalizados o generales.
 */
@Component({
  // Selector CSS para usar el componente en templates
  selector: 'page-home',
  // Componente standalone sin necesidad de módulo
  standalone: true,
  // Módulos y componentes importados
  imports: [RouterLink, CommonModule, Header, Footer, TripCard, AppIcon],
  // Archivo HTML de la plantilla
  templateUrl: './home.html',
  // Archivo CSS de estilos
  styleUrl: './home.css',
})
export class Home implements OnInit {
  // Inyección de servicios
  api        = inject(ApiService);
  auth       = inject(AuthService);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  /** Lista de viajes sugeridos a mostrar en la página */
  suggestedTrips: Trip[] = [];
  
  /** Indicador de carga de viajes sugeridos */
  loadingTrips = true;
  
  /** Indica si los viajes son personalizados (true) o generales (false) */
  isSuggested  = false;

  /**
   * Array de características principales de la plataforma
   * Cada objeto contiene: icon, title, desc
   */
features = [
  { icon: 'lupa'    as IconName, title: 'Busca viajes',    desc: 'Filtra por campus, localidad, fecha y dirección para encontrar tu pana de viaje perfecto.' },
  { icon: 'coche'   as IconName, title: 'Publica el tuyo', desc: 'Ofrece plazas libres en tu coche, fija un precio y gestiona quién viaja contigo.' },
  { icon: 'estrella' as IconName, title: 'Confirma y viaja', desc: 'Solicita plaza, el conductor acepta y compartes el camino. Así de fácil.' },
];

  /**
   * Hook de ciclo de vida de Angular
   * Se ejecuta al inicializar el componente
   * Carga viajes sugeridos o generales según autenticación
   */
  ngOnInit(): void {
    // Verificar si estamos en el navegador (no en servidor)
    if (!isPlatformBrowser(this.platformId)) { this.loadingTrips = false; return; }

    // Obtener usuario autenticado
    const user = this.auth.getUser();
    if (user?.id) {
      // Usuario logueado: cargar viajes personalizados
      this.isSuggested = true;
      this.api.getSuggestedTrips(user.id, 0, 6).subscribe({
        next: p => {
          this.suggestedTrips = p.content;
          this.isSuggested = p.content.length > 0;
          this.loadingTrips = false;
          this.cdr.detectChanges();
        },
        error: () => { this.loadFutureTrips(); },
      });
    } else {
      // Usuario no logueado: cargar viajes generales futuros
      this.loadFutureTrips();
    }
  }

  /**
   * Carga viajes futuros generales (sin personalización)
   * Se usa como fallback si no hay viajes sugeridos o usuario no autenticado
   * @private
   */
  private loadFutureTrips() {
    this.isSuggested = false;
    this.api.getFutureTrips(0, 6).subscribe({
      next: p => { this.suggestedTrips = p.content; this.loadingTrips = false; this.cdr.detectChanges(); },
      error: () => { this.loadingTrips = false; this.cdr.detectChanges(); },
    });
  }

  /**
   * Actualiza un viaje en la lista cuando cambia
   * Se usa cuando un viaje es modificado por el usuario
   * @param updated - Viaje actualizado
   * @param index - Índice del viaje en el array
   */
  updateTrip(updated: Trip, index: number) {
    this.suggestedTrips[index] = updated;
    this.cdr.detectChanges();
  }

  /**
   * Getter que devuelve el nombre del usuario autenticado
   * Extrae solo el primer nombre si está disponible
   * @returns Primer nombre del usuario o string vacío si no está autenticado
   */
  get userName(): string {
    return this.auth.getUser()?.name?.split(' ')[0] || '';
  }
}
