import {
    Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID
} from '@angular/core';
import {
    isPlatformBrowser, CommonModule
} from '@angular/common';
import {
    ActivatedRoute, Router, RouterLink
} from '@angular/router';
import {
    FormsModule
} from '@angular/forms';
import {
    Header
} from '../../elements/header/header';
import {
    Footer
} from '../../elements/footer/footer';
import {
    ApiService
} from '../../../services/api-service';
import {
    AuthService
} from '../../../services/auth-service';
import {
    User, Rating
} from '../../../models/user.model';

@Component({
    selector: 'page-user-profile',
    standalone: true,
    imports: [CommonModule, RouterLink, FormsModule, Header, Footer],
    templateUrl: './user-profile.html',
    styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {
    
    // Inyección de servicios
    api = inject(ApiService);
    auth = inject(AuthService);
    route = inject(ActivatedRoute);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // Almacena la información del usuario que se muestra en el perfil, el estado de carga, si es favorito o no, y los estados relacionados con la calificación y el reporte
    user: User | null = null;
    loading = true;
    isFavorite = false;
    favLoading = false;

    // Estado para la calificación
    ratingValue = 0;
    ratingLoading = false;
    ratingSaved = false;
    existingRating: Rating | null = null;

    // Estado para el reporte
    showReportModal = false;
    reportReason = '';
    reportLoading = false;
    reportSuccess = '';
    reportError = '';

    // Al inicializar el componente, se verifica si se está en un entorno de navegador, se obtiene el ID del usuario desde la ruta y se carga la información del usuario correspondiente, además de inicializar la calificación y verificar si es favorito
    ngOnInit(): void {
        if (!isPlatformBrowser(this.platformId)) {
            this.loading = false; return;
        }

        const id = Number(this.route.snapshot.paramMap.get('id'));
        if (!id || id === this.currentUserId) {
            this.router.navigate(['/profile']);
            return;
        }

        this.api.getUser(id).subscribe({
            next: u => {
                this.user = u;
                this.loading = false;
                this.initRating();
                this.checkFavorite();
                this.cdr.detectChanges();
            },
            error: () => {
                this.loading = false; this.cdr.detectChanges();
            },
        });
    }

    // Getter para obtener el ID del usuario actual desde el servicio de autenticación, o null si no hay usuario logueado
    get currentUserId(): number | null {
        return this.auth.getUser()?.id ?? null;
    }

    // Getter para calcular la calificación promedio del usuario basado en las calificaciones recibidas
    get avgRating(): number {
        if (!this.user?.ratingsReceivedDTO?.length) return 0;
        const sum = this.user.ratingsReceivedDTO.reduce((a, r) => a + r.rating, 0);
        return sum / this.user.ratingsReceivedDTO.length;
    }

    // Getter para determinar si el usuario del perfil es conductor, basado en la presencia del año de licencia de conducir
    get isDriver(): boolean {
        return !!this.user?.drivingLicenseYear;
    }

    // Verifica si el usuario actual ha marcado al usuario del perfil como favorito, actualizando el estado de isFavorite en consecuencia
    checkFavorite() {
        if (!this.currentUserId || !this.user) return;
        this.api.isFavorite(this.currentUserId, this.user.id).subscribe({
            next: r => {
                this.isFavorite = r.isFavorite; this.cdr.detectChanges();
            },
        });
    }

    // Alterna el estado de favorito del usuario, enviando la solicitud correspondiente al backend para agregar o eliminar de favoritos
    toggleFavorite() {
        if (!this.currentUserId || !this.user || this.favLoading) return;
        this.favLoading = true;
        if (this.isFavorite) {
            this.api.removeFavorite(this.currentUserId, this.user.id).subscribe({
                next: () => {
                    this.isFavorite = false; this.favLoading = false; this.cdr.detectChanges();
                },
                error: () => {
                    this.favLoading = false; this.cdr.detectChanges();
                },
            });
        } else {
            this.api.addFavorite(this.currentUserId, this.user.id).subscribe({
                next: () => {
                    this.isFavorite = true; this.favLoading = false; this.cdr.detectChanges();
                },
                error: () => {
                    this.favLoading = false; this.cdr.detectChanges();
                },
            });
        }
    }

    // Inicializa el estado de la calificación verificando si el usuario actual ya ha calificado al usuario del perfil y estableciendo el valor de la calificación en consecuencia
    initRating() {
        if (!this.currentUserId || !this.user) return;
        const me = this.auth.getUser();
        if (me?.ratingsDoneDTO) {
            this.existingRating = me.ratingsDoneDTO.find(
                (r: Rating) => r.ratedUserDTO?.id === this.user!.id
            ) ?? null;
            if (this.existingRating) this.ratingValue = this.existingRating.rating;
        }
        if (this.user.ratingsReceivedDTO) {
            const found = this.user.ratingsReceivedDTO.find(
                r => r.userRateDTO?.id === this.currentUserId
            );
            if (found) {
                this.existingRating = found;
                this.ratingValue = found.rating;
            }
        }
    }

    // Maneja el clic en una estrella para calificar al usuario, actualizando o creando la calificación según corresponda
    clickStar(star: number) {
        if (!this.currentUserId || !this.user || this.ratingLoading) return;
        this.ratingValue = star;
        this.ratingLoading = true;
        this.ratingSaved = false;

        if (this.existingRating) {
            this.api.updateRating(this.existingRating.id, {
                rating: star
            }).subscribe({
                next: () => {
                    this.existingRating!.rating = star;
                    this.flashSaved();
                },
                error: () => {
                    this.ratingLoading = false; this.cdr.detectChanges();
                },
            });
        } else {
            this.api.createRating({
                rating: star,
                idUserRate: this.currentUserId,
                idRatedUser: this.user.id,
            }).subscribe({
                next: (r) => {
                    this.existingRating = r;
                    this.flashSaved();
                },
                error: () => {
                    this.ratingLoading = false; this.cdr.detectChanges();
                },
            });
        }
    }

    // Muestra un mensaje temporal de "Calificación guardada" después de guardar la calificación
    private flashSaved() {
        this.ratingLoading = false;
        this.ratingSaved = true;
        this.cdr.detectChanges();
        setTimeout(() => {
            this.ratingSaved = false;
            this.cdr.detectChanges();
        }, 1800);
    }

    // Abre el modal para reportar al usuario y mostrar el formulario de reporte
    openReport() {
        this.showReportModal = true;
    }

    // Cierra el modal de reporte y resetea los campos relacionados
    closeReport() {
        this.showReportModal = false; this.reportReason = ''; this.reportError = '';
    }

    // Envía el reporte al backend y maneja la respuesta para mostrar mensajes de éxito o error
    submitReport() {
        if (!this.currentUserId || !this.user || !this.reportReason.trim()) return;
        this.reportLoading = true;
        this.reportError = '';
        this.api.createReport({
            reason: this.reportReason.trim(),
            idUserReport: this.currentUserId,
            idReportedUser: this.user.id,
            date: new Date().toISOString().split('T')[0],
        }).subscribe({
            next: () => {
                this.reportLoading = false;
                this.reportSuccess = 'Reporte enviado correctamente.';
                this.showReportModal = false;
                this.reportReason = '';
                this.cdr.detectChanges();
                setTimeout(() => {
                    this.reportSuccess = ''; this.cdr.detectChanges();
                }, 4000);
            },
            error: (e) => {
                this.reportLoading = false;
                const msg = e?.error?.message || '';
                this.reportError = msg.includes('no puedes reportar')
                    ? 'Solo puedes reportar a usuarios con los que hayas compartido un viaje.'
                    : (msg || 'Error al enviar el reporte.');
                this.cdr.detectChanges();
            },
        });
    }

    // Formatea la fecha de nacimiento para mostrarla en el perfil
    formatDate(d: string): string {
        if (!d) return '';
        return new Date(d).toLocaleDateString('es-ES', {
            day: 'numeric', month: 'long', year: 'numeric'
        });
    }

    // Devuelve un array para las estrellas llenas según la calificación
    starsArray(n: number): number[] {
        return Array(Math.round(n)).fill(0);
    }

    // Devuelve un array para las estrellas vacías (hasta 5)
    emptyStarsArray(n: number): number[] {
        return Array(5 - Math.round(n)).fill(0);
    }
}
