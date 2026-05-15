/**
 * Componente de Página de Favoritos
 * 
 * Gestiona la visualización de usuarios favoritos y viajes de estos usuarios.
 * Los usuarios pueden ver sus favoritos organizados en dos tabs: personas y viajes.
 */

// Importaciones de Angular core
import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';

// Importaciones de componentes compartidos
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';

// Importaciones de servicios
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';

// Importaciones de modelos
import { Favorite } from '../../../models/user.model';
import { Trip } from '../../../models/trip.model';

/**
 * Componente Favorites
 * 
 * Muestra los usuarios favoritos del usuario autenticado y sus viajes.
 * Permite filtrar entre favoritos (personas) y viajes de usuarios favoritos.
 */
@Component({
  // Selector CSS para usar el componente en templates
  selector: 'page-favorites',
  // Componente standalone sin necesidad de módulo
  standalone: true,
  // Módulos y componentes importados
  imports: [CommonModule, RouterLink, Header, Footer, TripCard],
  // Archivo HTML de la plantilla
  templateUrl: './favorites.html',
  // Archivo CSS de estilos
  styleUrl: './favorites.css',
})
export class Favorites implements OnInit {
  // Inyección de servicios
  api        = inject(ApiService);
  auth       = inject(AuthService);
  router     = inject(Router);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  /** Lista de usuarios favoritos del usuario autenticado */
  favorites: Favorite[] = [];
  
  /** Lista de viajes de los usuarios favoritos */
  favoriteTrips: Trip[]  = [];
  
  /** Indicador de carga inicial de favoritos */
  loading     = true;
  
  /** Indicador de carga de viajes de favoritos */
  loadingTrips = true;
  
  /** Tab activo: 'people' para favoritos o 'trips' para viajes */
  activeTab: 'people' | 'trips' = 'people';

  /**
   * Hook de ciclo de vida de Angular
   * Se ejecuta al inicializar el componente
   * Carga los favoritos del usuario autenticado
   */
  ngOnInit(): void {
    // Verificar si estamos en el navegador (no en servidor)
    if (!isPlatformBrowser(this.platformId)) { this.loading = false; return; }
    
    // Obtener ID del usuario autenticado
    const userId = this.auth.getUser()?.id;
    if (!userId) { this.loading = false; return; }

    // Cargar lista de favoritos del usuario
    this.api.getFavorites(userId).subscribe({
      next: favs => {
        this.favorites = favs;
        this.loading = false;
        this.loadFavoriteTrips(favs);
        this.cdr.detectChanges();
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); },
    });
  }

  /**
   * Carga los viajes de los usuarios favoritos
   * Para cada usuario favorito que sea conductor, obtiene sus viajes
   * @private
   * @param favs - Lista de usuarios favoritos
   */
  private loadFavoriteTrips(favs: Favorite[]) {
    // Si no hay favoritos, finalizar carga de viajes
    if (favs.length === 0) { this.loadingTrips = false; return; }

    // Contador para saber cuándo han terminado todas las llamadas
    let pending = favs.length;
    const allTrips: Trip[] = [];

    // Para cada usuario favorito que sea conductor
    for (const fav of favs) {
      this.api.getTripsAsDriver(fav.favoriteUserDTO.id).subscribe({
        next: page => {
          // Extraer viajes de la respuesta paginada
          const raw = page as any;
          const trips = raw.content ?? [];
          allTrips.push(...trips);
          pending--;
          
          // Cuando todos los viajes se hayan cargado
          if (pending === 0) {
            // Ordenar por fecha descendente (más recientes primero)
            this.favoriteTrips = allTrips
              .sort((a, b) => new Date(b.departureDate).getTime() - new Date(a.departureDate).getTime());
            this.loadingTrips = false;
            this.cdr.detectChanges();
          }
        },
        error: () => {
          pending--;
          if (pending === 0) {
            this.favoriteTrips = allTrips;
            this.loadingTrips = false;
            this.cdr.detectChanges();
          }
        },
      });
    }
  }

  /**
   * Elimina un usuario de la lista de favoritos
   * @param fav - Usuario favorito a eliminar
   */
  removeFavorite(fav: Favorite) {
    const userId = this.auth.getUser()?.id;
    if (!userId) return;
    
    this.api.removeFavorite(userId, fav.favoriteUserDTO.id).subscribe({
      next: () => {
        // Eliminar del array local después de confirmar en servidor
        this.favorites = this.favorites.filter(f => f.id !== fav.id);
        this.cdr.detectChanges();
      },
    });
  }

  /**
   * Navega al perfil de un usuario específico
   * @param id - ID del usuario cuyo perfil se desea ver
   */
  goToProfile(id: number) {
    this.router.navigate(['/user', id]);
  }

  /**
   * Actualiza un viaje en la lista local cuando cambia
   * @param updated - Viaje actualizado
   * @param i - Índice del viaje en el array
   */
  updateTrip(updated: Trip, i: number) { this.favoriteTrips[i] = updated; }
}
